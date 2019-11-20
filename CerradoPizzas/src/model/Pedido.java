package model;

import java.util.StringJoiner;

public class Pedido {

    private String nome;
    private String pizza;

    public Pedido(String nome, String pizza) {
        this.nome = nome;
        this.pizza = pizza;
    }

    private String getNomeJson() {
        return "\"nome\": \"" + nome + "\"";
    }

    private String getPizzaJson() {
        return "\"pizza\": \"" + pizza + "\"";
    }

    public String toJson() {
        StringJoiner sj = new StringJoiner(",","{","}");
        sj.setEmptyValue("{}");
        
        sj.add(getNomeJson());
        sj.add(getPizzaJson());
        
        
        String json = sj.toString();
        
        return json;
    }
}
