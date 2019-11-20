package model;

import java.util.StringJoiner;

public class Pedido {

    private String nome;
    private String pizza;

    public Pedido(String nome, String pizza) {
        this.nome = nome;
        this.pizza = pizza;
    }

    private String getNomeJSON() {
        return "\"nome\": \"" + nome + "\"";
    }

    private String getPizzaJSON() {
        return "\"pizza\": \"" + pizza + "\"";
    }

    public String toJSON() {
        StringJoiner sj = new StringJoiner(",","{","}");
        sj.setEmptyValue("{}");
        
        sj.add(getNomeJSON());
        sj.add(getPizzaJSON());
        
        
        String json = sj.toString();
        
        return json;
    }
}
