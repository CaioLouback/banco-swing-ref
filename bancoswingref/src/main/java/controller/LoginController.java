package controller;

import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import utils.LoginService;
import view.Cadastramento;
import view.TelaCliente;


public class LoginController extends LoginService{
    
    public static void controladorLogin(String cpf, String senha){
        boolean vazio = verificarVazio(cpf, senha);
        boolean loginValidado = false;
        
        if(vazio){
            JOptionPane.showMessageDialog(null,"Há informações em branco. Favor inserir um cadastro válido!", "Atenção!", JOptionPane.WARNING_MESSAGE);
        } else {
            loginValidado = autenticadorLogin(cpf, senha);
            if(!loginValidado){
                JOptionPane.showMessageDialog(null, "CPF ou senha incorretos. Tente novamente!", "Atenção!", JOptionPane.WARNING_MESSAGE);
            } else {
                telaCliente();
            }     
        }       
    }
    
    public static void mostrarSenha(JCheckBox check, JPasswordField senha){
         if(check.isSelected())
            senha.setEchoChar((char)0);
        else
            senha.setEchoChar('*');
    }
    
    public static void telaCadastramento(){
        Cadastramento cad = new Cadastramento();
        cad.setLocationRelativeTo(null);
        cad.setVisible(true);
    }
    
    private static void telaCliente(){
        TelaCliente tela = new TelaCliente();
        tela.setLocationRelativeTo(null);
        tela.setVisible(true);
    }
}
