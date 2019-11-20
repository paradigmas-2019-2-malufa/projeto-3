

package main;

import model.Pedido;
import util.ComunicadorAPI;
import util.Gerador;

public class Main {

    public static void main(String[] args) {
        ComunicadorAPI.enviarDados();
//        ComunicadorAPI.enviarDados("echo '"+Gerador.novoPedidoJSON()+"'");
        ComunicadorAPI.enviarDados("echo '"+Gerador.novoPedidoJSON()+"'");
    }
}
