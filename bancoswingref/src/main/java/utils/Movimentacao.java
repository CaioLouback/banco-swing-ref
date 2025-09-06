package utils;


public class Movimentacao {
    private String tipo;
    private double valor;

    public Movimentacao(String tipo, double valor) {
        this.tipo = tipo;
        this.valor = valor;
    }

    public String getTipo() {
        return tipo;
    }

    public double getValor() {
        return valor;
    }
}