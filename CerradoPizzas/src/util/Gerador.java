

package util;

import java.util.Random;

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
            "Beijinho"
        
        };
    
        public static String novaPizza(){
            int pos = random.nextInt(pizzas.length);
            
            return pizzas[pos];
        }
        public static String novoNome(){
            int pos = random.nextInt(nomes.length);
            return nomes[pos];
        }
}
