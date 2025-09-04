package user;

public abstract class Usuarios {
    private String nome, senha, cpf, tipo;
    
    public Usuarios(String nome, String senha, String cpf, String tipo){
        this.nome = nome;
        this.senha = senha;
        this.cpf = cpf;
        this.tipo = tipo;
    }
    
    public String getTipo(){
        return tipo;
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
    
    public boolean verificarSenha(String senha){
        return senha.equals(getSenha());
    }
}
