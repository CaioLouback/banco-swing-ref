package user;

public class Cliente extends Usuarios {
    private double saldo;
    private double valorCredito;
    
    public Cliente(String nome, String senha, String cpf, double saldo) {
        super(nome, senha, cpf, "Cliente");
        this.saldo = saldo;
    }
    
    public double getSaldo(){
        return saldo;
    }
    
    public void setSaldo(double saldo){
        this.saldo = saldo;
    }
    
    public double getValorCredito(){
        return valorCredito;
    }
    
    public void setValorCredito(double valor){
        valorCredito = valor;
    }
}
