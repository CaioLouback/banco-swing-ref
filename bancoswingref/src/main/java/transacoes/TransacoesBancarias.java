package transacoes;


public interface TransacoesBancarias {
    public void transferencia(String cpfOrigem, String cpfDestino, double valor);
        
    
        
    public void deposito(String cpf, double valor);
        
    
    
    public void saque(String cpf, double valor);
        
    
    
    public void emprestimo(String cpf, double valor);
        
    
    
    
}
