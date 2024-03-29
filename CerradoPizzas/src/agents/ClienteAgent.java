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

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Pedido;
import util.Gerador;

public class ClienteAgent extends Agent {

    private static final long serialVersionUID = 1L;
    // The title of the book to buy
    private String pedidoNome;
    private int pedidosAtentidos = 0;
    // The list of known seller agents
    private AID[] pizzariaAgents;
    private ArrayList<Pedido> pedidos = null;
    private static int QUANTIDADE = 13;
    // Put agent initializations here

    protected void setup() {
        // Printout a welcome message
        System.out.println("Olá! Cliente " + getAID().getName() + " está pronto para fazer um pedido!");

        // Get the title of the book to buy as a start-up argument
//		Object[] args = getArguments();
        pedidos = Pedido.geraPedidos(QUANTIDADE);
        if (pedidos != null && pedidos.size() > 0) {
            pedidoNome = pedidos.get(0).getPizza();
            System.out.println("Tentando comprar a pizza pedida eh " + pedidoNome);

            // Add a TickerBehaviour that schedules a request to seller agents every 10 seconds
            addBehaviour(new TickerBehaviour(this, Gerador.random.nextInt(1500) + 1000) {

                private static final long serialVersionUID = 1L;

                protected void onTick() {
                    System.out.println("Tentando pedir a pizza " + pedidoNome);
                    // Update the list of seller agents
                    DFAgentDescription template = new DFAgentDescription();
                    ServiceDescription sd = new ServiceDescription();
                    sd.setType("pizza-selling");
                    template.addServices(sd);
                    try {
                        DFAgentDescription[] result = DFService.search(myAgent, template);
                        System.out.println("Pizzas disponiveis no menu:");
                        pizzariaAgents = new AID[result.length];
                        for (int i = 0; i < result.length; ++i) {
                            pizzariaAgents[i] = result[i].getName();
                            System.out.println(pizzariaAgents[i].getName());
                        }
                    } catch (FIPAException fe) {
                        fe.printStackTrace();
                    }

                    // Perform the request
                    myAgent.addBehaviour(new RequestPerformer());
                }
            });
        } else {
            // Make the agent terminate
            System.out.println("A pizza nao foi anotada corretamente!");
            doDelete();
        }
    }

    /**
     * Inner class RequestPerformer. This is the behaviour used by Book-buyer
     * agents to request seller agents the target book.
     */
    /**
     * Inner class RequestPerformer. This is the behaviour used by Book-buyer
     * agents to request seller agents the target book.
     */
    private class RequestPerformer extends Behaviour {

        private static final long serialVersionUID = 1L;
        private AID melhorOfertaPizza; // The agent who provides the best offer 
        private int pizzaMaisBarata;  // The best offered price
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
                    cfp.setContent(pedidoNome);
                    cfp.setConversationId("pedido-pizza");
                    cfp.setReplyWith("cfp" + System.currentTimeMillis()); // Unique value
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
                            if (melhorOfertaPizza == null || price < pizzaMaisBarata) {
                                // This is the best offer at present
                                pizzaMaisBarata = price;
                                melhorOfertaPizza = reply.getSender();
                            }
                        }
                        repliesCnt++;
                        if (repliesCnt >= pizzariaAgents.length) {
                            // We received all replies
                            step = 2;
                        }
                    } else {
                        block();
                    }
                    break;
                case 2:
                    // Send the purchase order to the seller that provided the best offer
                    ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                    order.addReceiver(melhorOfertaPizza);
                    order.setContent(pedidoNome);
                    order.setConversationId("pedido-pizza");
                    order.setReplyWith("order" + System.currentTimeMillis());
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
                            System.out.println(pedidoNome + " vendida por  " + reply.getSender().getName());
                            System.out.println("Preço = " + pizzaMaisBarata);
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(ClienteAgent.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if (pedidos.size() > 0) {
                                pedidoNome = pedidos.get(0).getPizza();
                            }
                            System.out.println("Novo pedido: " + pedidoNome);
                            if (pedidos.size() != 0) {
                                pedidos.remove(0);
                            } else {
                                System.out.println("Não há mais pedidos ");
                            }

                            if (pedidosAtentidos == QUANTIDADE) {
                                myAgent.doDelete();
                            } else {
                                System.err.println("Pedidos atendidos " + (pedidosAtentidos++ + 1 ));
                            }
                        } else {
                            System.out.println("Attempt failed: Pizza já vendida.");
                        }

                        step = 4;
                    } else {
                        block();
                    }
                    break;
            }
        }

        public boolean done() {

            if (step == 2 && melhorOfertaPizza == null) {
                System.out.println("Attempt failed: " + pedidoNome + " pedido não disponível");
            }

            boolean pizzaIsNotAvailable = (step == 2 && melhorOfertaPizza == null);
            boolean negotiationIsConcluded = (step == 4);

            boolean isDone = false;
            if (pizzaIsNotAvailable || negotiationIsConcluded) {
                isDone = true;
            } else {
                isDone = false;
            }

            return isDone;
            //return ((step == 2 && bestSeller == null) || step == 4);
        }
    }  // End of inner class RequestPerformer

    // Put agent clean-up operations here
    protected void takeDown() {
        // Printout a dismissal message
        System.out.println("Cliente " + getAID().getName() + " satisfeito!");
    }

}
