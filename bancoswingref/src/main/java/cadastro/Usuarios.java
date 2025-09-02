package cadastro;

public abstract class Usuarios {
    private String nome, senha, cpf;
    
    public Usuarios(String nome, String senha, String cpf){
        this.nome = nome;
        this.senha = senha;
        this.cpf = cpf;
    }
    
    public String getNome(){
        return nome;
    }
    
    public String getCPF(){
        return cpf;
    }
    
    private String getSenha(){
        return senha;
    }
    
    
    
    
}
