package utils;

import dao.Json;
import static dao.Json.lerUsuarios;
import java.util.Arrays;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JPasswordField;
import user.Caixa;
import user.Cliente;
import user.Gerente;
import user.Usuarios;

public class CadastramentoService {
    protected CadastramentoService(){};
    
    protected static void cadastrar(String nome, String cpf, String senha, String tipo){
        if(null != tipo){
            switch (tipo) {
            case "Cliente":
                Cliente cliente = new Cliente(nome, senha, cpf, 0);
                Json.salvarUsuarios(cliente);
                break;
            case "Caixa":
                Caixa caixa = new Caixa(nome, senha, cpf);
                Json.salvarUsuarios(caixa);
                break;
            case "Gerente":
                Gerente gerente = new Gerente(nome, senha, cpf);
                Json.salvarUsuarios(gerente);
                break;
            default:
                break;
            }  
        } 
    }
    
    protected static boolean confirmacaoSenha(JPasswordField senha,JPasswordField confSenha){
        if(Arrays.equals(senha.getPassword(), confSenha.getPassword()))
            return true;
        return false;
    }
    
    protected static int verificarVazio(String nome, String cpf,JPasswordField senha, JPasswordField confSenha){
        if(nome.trim().isEmpty())
            return 1;
        if(cpf.trim().isEmpty())
            return 2;
        if(senha.getPassword().length == 0)  
            return 3;
        if(confSenha.getPassword().length == 0)
            return 4;
        return 0;
    }
    
    public static void mostrarSenha(JCheckBox check, JPasswordField senha, JPasswordField confSenha){
        if(check.isSelected()){
           senha.setEchoChar((char)0);
           confSenha.setEchoChar((char)0);
        }else {
           senha.setEchoChar('*');
           confSenha.setEchoChar('*');
        }    
    }
    
    
    protected static boolean cpfCadastrado(String cpf){
        List<Usuarios> listaUsuarios = lerUsuarios();
        for(Usuarios var: listaUsuarios){
            if(var.getCPF().equals(cpf))
                return true;
        }
        return false;
    }
    
    
    
}
