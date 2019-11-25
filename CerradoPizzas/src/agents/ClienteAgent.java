package agents;
import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class ClienteAgent extends Agent {

	private static final long serialVersionUID = 1L;
	// The title of the book to buy
	private String pizzaName;
	// The list of known seller agents
	private AID[] pizzariaAgents;

	// Put agent initializations here
	protected void setup() {
		// Printout a welcome message
		System.out.println("Olá! Cliente "+getAID().getName()+" está pronto para fazer um pedido!");

		// Get the title of the book to buy as a start-up argument
		Object[] args = getArguments();
		if (args != null && args.length > 0) {
			pizzaName = (String) args[0];
			System.out.println("A pizza pedida eh "+pizzaName);

			// Add a TickerBehaviour that schedules a request to seller agents every 10 seconds
			addBehaviour(new TickerBehaviour(this, 10000) {

				private static final long serialVersionUID = 1L;

				protected void onTick() {
					System.out.println("Tentando pedir a pizza "+pizzaName);
					// Update the list of seller agents
					DFAgentDescription template = new DFAgentDescription();
					ServiceDescription sd = new ServiceDescription();
					sd.setType("venda-pizza");
					template.addServices(sd);
					try {
						DFAgentDescription[] result = DFService.search(myAgent, template); 
						System.out.println("Pizzas disponiveis no menu:");
						pizzariaAgents = new AID[result.length];
						for (int i = 0; i < result.length; ++i) {
							pizzariaAgents[i] = result[i].getName();
							System.out.println(pizzariaAgents[i].getName());
						}
					}
					catch (FIPAException fe) {
						fe.printStackTrace();
					}

					// Perform the request
					myAgent.addBehaviour(new RequestPerformer());
				}
			} );
		}
		else {
			// Make the agent terminate
			System.out.println("A pizza nao foi anotada corretamente!");
			doDelete();
		}
	}


	/**
	   Inner class RequestPerformer.
	   This is the behaviour used by Book-buyer agents to request seller 
	   agents the target book.
	 */
	private class RequestPerformer extends Behaviour {

		private static final long serialVersionUID = 1L;
		private AID melhorPizzaria; // The agent who provides the best offer 
		private int pizzaMaisVendida;  // The best offered price
		private int repliesCnt = 0; // The counter of replies from seller agents
		private MessageTemplate mt; // The template to receive replies
		private int step = 0;

		public void action() {
			switch (step) {
			case 0:
				// Send the cfp to all sellers
				ACLMessage cfp = new ACLMessage(ACLMessage.CFP);
				for (int i = 0; i < pizzariaAgents.length; ++i) {
					cfp.addReceiver(pizzariaAgents[i]);
				} 
				cfp.setContent(pizzaName);
				cfp.setConversationId("pedido-pizza");
				cfp.setReplyWith("cfp"+System.currentTimeMillis()); // Unique value
				myAgent.send(cfp);
				// Prepare the template to get proposals
				mt = MessageTemplate.and(MessageTemplate.MatchConversationId("pedido-pizza"),
						MessageTemplate.MatchInReplyTo(cfp.getReplyWith()));
				step = 1;
				break;
			case 1:
				// Receive all proposals/refusals from seller agents
				ACLMessage reply = myAgent.receive(mt);
				if (reply != null) {
					// Reply received
					if (reply.getPerformative() == ACLMessage.PROPOSE) {
						// This is an offer 
						int price = Integer.parseInt(reply.getContent());
						if (melhorPizzaria == null || price < pizzaMaisVendida) {
							// This is the best offer at present
							pizzaMaisVendida = price;
							melhorPizzaria = reply.getSender();
						}
					}
					repliesCnt++;
					if (repliesCnt >= pizzariaAgents.length) {
						// We received all replies
						step = 2; 
					}
				}
				else {
					block();
				}
				break;
			case 2:
				// Send the purchase order to the seller that provided the best offer
				ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
				order.addReceiver(melhorPizzaria);
				order.setContent(pizzaName);
				order.setConversationId("pedido-pizza");
				order.setReplyWith("order"+System.currentTimeMillis());
				myAgent.send(order);
				// Prepare the template to get the purchase order reply
				mt = MessageTemplate.and(MessageTemplate.MatchConversationId("pedido-pizza"),
						MessageTemplate.MatchInReplyTo(order.getReplyWith()));
				step = 3;
				break;
			case 3:      
				// Receive the purchase order reply
				reply = myAgent.receive(mt);
				if (reply != null) {
					// Purchase order reply received
					if (reply.getPerformative() == ACLMessage.INFORM) {
						// Purchase successful. We can terminate
						System.out.println(pizzaName+" successfully purchased from agent "+reply.getSender().getName());
						System.out.println("Preço = "+pizzaMaisVendida);
						myAgent.doDelete();
					}
					else {
						System.out.println("Attempt failed: requested book already sold.");
					}

					step = 4;
				}
				else {
					block();
				}
				break;
			}        
		}

		public boolean done() {
			
			if (step == 2 && melhorPizzaria == null) {
				System.out.println("Attempt failed: "+pizzaName+" not available for sale");
			}
			
			boolean bookIsNotAvailable = (step == 2 && melhorPizzaria == null);
			boolean negotiationIsConcluded = (step == 4);
			
			boolean isDone = false;
			if (bookIsNotAvailable || negotiationIsConcluded) {
				isDone = true;
			}
			else {
				isDone = false;
			}
			
			return isDone;
			//return ((step == 2 && bestSeller == null) || step == 4);
		}
	}  // End of inner class RequestPerformer
	
	
	// Put agent clean-up operations here
	protected void takeDown() {
		// Printout a dismissal message
		System.out.println("Cliente "+getAID().getName()+" foi embora!");
	}
	
}

