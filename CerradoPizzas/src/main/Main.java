

package main;

import model.Pedido;
import util.ComunicadorAPI;
import util.Gerador;

public class Main {

    public static void main(String[] args) {
        ComunicadorAPI.enviarDados();
//        ComunicadorAPI.enviarDados("echo '"+Gerador.novoPedidoJSON()+"'");
        for(int i = 0; i < 10; i++)
			try {
				ComunicadorAPI.enviarDados("echo '"+Gerador.novoPedidoJSON()+"'");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
}
