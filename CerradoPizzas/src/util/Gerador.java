

package util;

import java.util.Random;
import model.Pedido;

public class Gerador {
        
        private static Random random = new Random();

        private static String[] nomes = {
            "Lucas","Marcelo","Matheus","Fabiana","Milene","Romeu","Gabriela",
            "Micaella","João","Keanu Reeves"
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
            "A moda do chefe ;)",
            "Quatro queijos",
            "Vegetariana",
            "Americana",
            "Rapadura",
            "Beijinho",
            "Romeu e Julieta",
            "Marguerita",
            "Muçarela",
            "Brigadeiro"            
        };
    
        public static String newPizza(){
            int pos = random.nextInt(pizzas.length);
            
            return pizzas[pos];
        }
        public static String newNome(){
            int pos = random.nextInt(nomes.length);
            return nomes[pos];
        }
        
        public static String novoPedidoJSON(){
            return new Pedido(newNome(),newPizza()).toJSON();
        }
}
