package agents;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import java.util.*;
import util.Gerador;

public class PizzariaAgent extends Agent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Hashtable<String,Integer> menu;
	
        protected void setup() {
		// Create the catalogue
		menu = Gerador.newCatalogo();
                System.out.println("Menu da "+ this.getName());
                System.out.println(menu.toString());
		
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("pizza-selling");
		sd.setName("JADE-pizza-trading");
		dfd.addServices(sd);	
		try {
			DFService.register(this, dfd);
		}
		catch (Exception fe) {
			fe.printStackTrace();
		}

		// Servidor de Requisi��es de Ofertas
		addBehaviour(new OfferRequestsServer());

		// Servidor de Pedidos de Compras
		addBehaviour(new PurchaseOrdersServer());
	}

	
	private class OfferRequestsServer extends CyclicBehaviour {

		private static final long serialVersionUID = 1L;

		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				// CFP Message received. Process it
				String pizzaName = msg.getContent();
				ACLMessage reply = msg.createReply();

				int price =  menu.get(pizzaName);
				if (price != 0) {
					// The requested book is available for sale. Reply with the price
					reply.setPerformative(ACLMessage.PROPOSE);
					reply.setContent(String.valueOf(price));
				}
				else {
					// The requested book is NOT available for sale.
					reply.setPerformative(ACLMessage.REFUSE);
					reply.setContent("Não esta disponivel!");
				}
				myAgent.send(reply);
			}
			else {
				block();
			}
		}
	} 

	// Servidor de Pedidos de Compras
	private class PurchaseOrdersServer extends CyclicBehaviour {

		private static final long serialVersionUID = 1L;

		public void action() {
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
			ACLMessage msg = myAgent.receive(mt);
			if (msg != null) {
				String pizzaName = msg.getContent();
				ACLMessage reply = msg.createReply();

				int price = menu.get(pizzaName);
				if (price != 0) {
					reply.setPerformative(ACLMessage.INFORM);
					System.out.println(pizzaName+" sold to agent "+msg.getSender().getName());
				}
				else {
					reply.setPerformative(ACLMessage.FAILURE);
					reply.setContent("Não esta disponivel!");
				}
				myAgent.send(reply);
			}
			else {
				block();
			}
		}
	} 
        
	public void updateCatalogue(final String pizzaName, final int price) {
		addBehaviour(new OneShotBehaviour() {

			private static final long serialVersionUID = 1L;

			public void action() {
				menu.put(pizzaName, new Integer(price));
				System.out.println(pizzaName+" inserido no menu. Preço = "+price);
			}
		} );
	}
	
	
		protected void takeDown() {
			try {
				DFService.deregister(this);
			}
			catch (FIPAException fe) {
				fe.printStackTrace();
			}
			System.out.println("Pizzaria "+getAID().getName()+" fechada!");
		}
}

