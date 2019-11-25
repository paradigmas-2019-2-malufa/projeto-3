package model;

import java.util.ArrayList;
import java.util.StringJoiner;
import util.Gerador;

public class Pedido {

    private String nome;
    private String pizza;
    private int mesa;

    public Pedido(String nome, String pizza, int mesa) {
        this.nome = nome;
        this.pizza = pizza;
        this.mesa = mesa;
    }

    private String getNomeJSON() {
        return "\"nome\": \"" + nome + "\"";
    }

    private String getPizzaJSON() {
        return "\"pizza\": \"" + pizza + "\"";
    }
    
    private String getMesaJSON() {
    	return "\"mesa\": \"" + mesa + "\"";
    }

    public String toJSON() {
        StringJoiner sj = new StringJoiner(",","{","}");
        sj.setEmptyValue("{}");
        
        sj.add(getNomeJSON());
        sj.add(getPizzaJSON());
        sj.add(getMesaJSON());
        
        
        String json = sj.toString();
        
        return json;
    }
    
    public static  ArrayList<Pedido> geraPedidos(int quantidade){
           ArrayList<Pedido> pedidos = new ArrayList<>();
           for(int i = 0 ; i< quantidade; i++){
               pedidos.add( new Pedido(Gerador.newNome(),Gerador.newPizza(),Gerador.newMesa()));
             
           }
           return pedidos;
    }
    
}
