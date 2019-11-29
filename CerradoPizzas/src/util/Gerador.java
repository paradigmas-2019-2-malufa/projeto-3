

package util;

import java.util.Hashtable;
import java.util.Random;
import model.Pedido;

public class Gerador {
        
        public static Random random = new Random();
        private static final int PRECOBASE = 15;
        private static String[] nomes = {
            "Lucas","Marcelo","Matheus","Fabiana","Milene","Romeu","Gabriela",
            "Micaella","João","Keanu Reeves","Ester","Eduarda","Péricles"
        };
    
        private static String [] pizzas = {
            "Calabresa",
            "Frango",
            "Calabresa",
            "Frango com catupiry",
            "Chocolate",
            "Califórnia",
            "Bacon",
            "Portuguesa",
            "Moda da casa",
            "A moda do chefe",
            "Quatro queijos",
            "Vegetariana",
            "Americana",
            "Rapadura",
            "Beijinho",
            "Romeu e Julieta",
            "Marguerita",
            "Muçarela",
            "Brigadeiro",
            "Tradicional",
            "Escarola",
            "Mexicana",
            "Carioca",
            "Lombo com catupiry",
            "Vegetariana",
            "Parmegiana",
            "Do sogro",
            "Do chefe",
            "Quatro queijos estravaganza"
        };
    
        public static String newPizza(){
            int pos = random.nextInt(pizzas.length);
            
            return pizzas[pos];
        }
        public static String newNome(){
            int pos = random.nextInt(nomes.length);
            return nomes[pos];
        }
        
        public static int newMesa() {
        	return random.nextInt(10)+1;
		}
        
        public static String novoPedidoJSON(){
        	return new Pedido(newNome(),newPizza(),"Pizza Ratão").toJSON();
            
        }
        public static Hashtable<String,Integer> newCatalogo(){
            Hashtable<String,Integer>  menu = new Hashtable<>();
            
            for(String s :pizzas)
                menu.put(s,PRECOBASE + random.nextInt(15));            
            return menu;            
        }
}
