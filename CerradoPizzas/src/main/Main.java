package main;

import model.Pedido;
import util.ComunicadorAPI;
import util.Gerador;

public class Main {

    public static void main(String[] args) {
       ComunicadorAPI.enviarAPI(Gerador.novoPedidoJSON());
    }
}
