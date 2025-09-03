package utils;

import dao.Json;
import java.util.Arrays;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import user.Caixa;
import user.Cliente;
import user.Gerente;

public class CadastramentoService {
    private CadastramentoService(){};
    
    private static void cadastrar(String nome, String cpf, String senha, String tipo){
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
            JOptionPane.showMessageDialog(null, "Cadastro realizado com sucesso!");
        }  
    }
    
    private static boolean confirmacaoSenha(JPasswordField senha,JPasswordField confSenha){
        if(Arrays.equals(senha.getPassword(), confSenha.getPassword()))
            return true;
        return false;
    }
    
    private static boolean verificarVazio(String nome, String cpf,JPasswordField senha, JPasswordField confSenha){
        if(nome.trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "Favor inserir um nome!");
            return true;
        }
        
        if(cpf.trim().isEmpty()){
            JOptionPane.showMessageDialog(null, "Favor inserir um CPF!");
            return true;
        }
        
        if(senha.getPassword().length == 0){
            JOptionPane.showMessageDialog(null, "Favor inserir uma senha!");
            return true;
        }
        
        if(confSenha.getPassword().length == 0){
            JOptionPane.showMessageDialog(null, "Favor confirmar a sua senha!");
            return true;
        }
        return false;
    }
    
    public static void menu(String nome, String cpf, JPasswordField senha, JPasswordField confSenha, String tipo){
        boolean vazio = verificarVazio(nome, cpf, senha, confSenha);

        if(!vazio){
            boolean senhaConfirmada = confirmacaoSenha(senha, confSenha);
            if(senhaConfirmada){
                String pass = new String(senha.getPassword());
                cadastrar(nome, cpf, pass, tipo);
            } else{
                JOptionPane.showMessageDialog(null, "Sua confirmação de senha está incorreta!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    
    
    
    
    
    
}
