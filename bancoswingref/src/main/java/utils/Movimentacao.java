package utils;

/**
 * Representa uma movimentação financeira de um cliente.
 * <p>
 * Cada movimentação possui um tipo (por exemplo, "Saque", "Depósito", "Transferência", "Crédito") 
 * e um valor associado.
 * </p>
 * 
 * Exemplo de uso:
 * <pre>
 * Movimentacao mov = new Movimentacao("Depósito (+)", 150.0);
 * System.out.println(mov.getTipo());
 * System.out.println(mov.getValor());
 * </pre>
 * 
 * Autor: Caio Louback
 * Versão: 1.0
 */

public class Movimentacao {
    private String tipo; /** Tipo da movimentação (Saque, Depósito, Transferência, Crédito etc.) */
    private double valor; /** Valor da movimentação */

    /**
     * Construtor para criar uma nova movimentação.
     * 
     * @param tipo Tipo da movimentação.
     * @param valor Valor da movimentação.
     */
    
    public Movimentacao(String tipo, double valor) {
        this.tipo = tipo;
        this.valor = valor;
    }
    
    /**
     * Retorna o tipo da movimentação.
     * 
     * @return String representando o tipo da movimentação.
     */
    
    public String getTipo() {
        return tipo;
    }
    
    /**
     * Retorna o valor da movimentação.
     * 
     * @return double representando o valor da movimentação.
     */
    
    public double getValor() {
        return valor;
    }
}