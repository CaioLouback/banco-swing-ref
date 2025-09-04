package utils;

import static dao.Json.lerUsuarios;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import user.Usuarios;
import view.Cadastramento;
import view.TelaCliente;

public class LoginService {
    
    private LoginService(){}; // Evitar instaciar 
    
    public static boolean autenticadorLogin(String cpfDigitado, String senhaDigitada){ 
        boolean loginValido = false;
        List<Usuarios> listaUsuarios = lerUsuarios();
        for(Usuarios var : listaUsuarios) // Percorrendo os usuários cadastrados no JSON
            System.out.println("Usuários no JSON: " + var.getNome() + " " + var.getCPF()  + " Tipo: " + var.getTipo());
     
        for(Usuarios var : listaUsuarios){ // Valiando o login e senha
            if(var.getCPF().equals(cpfDigitado) && var.verificarSenha(senhaDigitada)){
                loginValido = true;
                System.out.println("Logou!");
                if(var.getTipo().trim().equalsIgnoreCase("Cliente")){
                    System.out.println("Entrou como Cliente!");
                    return loginValido;  
                } else if (var.getTipo().trim().equalsIgnoreCase("Caixa")){
                    System.out.println("Entrou como Caixa!");
                    return loginValido;
                } else {
                    System.out.println("Entrou como Gerente!");
                    return loginValido;
                }
            }
        } 
        return loginValido;      
    }
    
    private static boolean verificarVazio(String cpfDigitado, String senhaDigitada){
        if(cpfDigitado.trim().isEmpty() || senhaDigitada.length() == 0){
            JOptionPane.showMessageDialog(null,"Há informações em branco. Favor inserir um cadastro válido!", "Erro", JOptionPane.ERROR_MESSAGE);
            return true;
        }
        return false;
    }
    
    public static void menu(String cpf, String senha){
        boolean vazio = verificarVazio(cpf, senha);
        boolean loginValidado = false;
        
        if(!vazio)
            loginValidado = autenticadorLogin(cpf, senha);
        
        if(loginValidado)
            JOptionPane.showMessageDialog(null, "CPF ou senha incorretos. Tente novamente!", "Erro", JOptionPane.ERROR_MESSAGE);
        else
            telaCliente();
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
