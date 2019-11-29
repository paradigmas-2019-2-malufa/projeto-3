package model;

import java.util.ArrayList;
import java.util.StringJoiner;
import util.Gerador;

public class Pedido {

    private String nome;
    private String pizza;
    private int mesa = 0;
    private String Pizzaria;

    public Pedido(String nome, String pizza, String Pizzaria) {
        this.nome = nome;
        this.pizza = pizza;
        this.Pizzaria = Pizzaria;
    }
    

    public Pedido(String nome, String pizza, int mesa) {
        this.nome = nome;
        this.pizza = pizza;
        this.mesa = mesa;
    }

    public String getNome() {
        return nome;
    }

    public String getPizza() {
        return pizza;
    }

    public int getMesa() {
        return mesa;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPizza(String pizza) {
        this.pizza = pizza;
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

    public void setPizzaria(String Pizzaria) {
        this.Pizzaria = Pizzaria;
    }
     private String getPizzariaJSON() {
        return "\"Pizzaria\": \"" + Pizzaria + "\"";
    }
    
    

    public String toJSON() {
        StringJoiner sj = new StringJoiner(",","{","}");
        sj.setEmptyValue("{}");
        
        sj.add(getNomeJSON());
        sj.add(getPizzaJSON());
        sj.add(getPizzariaJSON());
        
        
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
