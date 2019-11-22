package model;

import java.util.StringJoiner;

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
}
