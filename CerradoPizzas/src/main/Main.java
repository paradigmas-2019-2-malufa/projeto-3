package main;

import model.Pedido;
import util.ComunicadorAPI;
import util.Gerador;

public class Main {

    public static void main(String[] args) {
        final String prompt = "bash";
        final String op = "-c";
        String json;
        String commmandSend;
        final String url = "http://localhost:3000/post/";
        ComunicadorAPI.enviarDados();
//        ComunicadorAPI.enviarDados("echo '"+Gerador.novoPedidoJSON()+"'");
        for(int i = 0; i < 10; i++)
            try {
                json = Gerador.novoPedidoJSON();
                commmandSend = "curl --header \"Content-Type: application/json\" "
                + "  --request POST "
                + "  --data '" + json + "' ";
                commmandSend += url;
                ComunicadorAPI.enviarDados(prompt, op, commmandSend);
                ComunicadorAPI.enviarDados("echo '"+Gerador.novoPedidoJSON()+"'");
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                e.printStackTrace();
                }
    }
}
