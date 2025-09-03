package utils;

import static dao.Json.lerUsuarios;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import user.Usuarios;

public class LoginService {
    
    private LoginService(){}; // Evitar instaciar 
    
    public static void autenticadorLogin(String cpfDigitado, String senhaDigitada){
        if(cpfDigitado.trim().isEmpty() || senhaDigitada.length() == 0){
            JOptionPane.showMessageDialog(null,"Há informações em branco. Favor inserir um cadastro válido!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        List<Usuarios> listaUsuarios = lerUsuarios();
        for(Usuarios var : listaUsuarios) // Percorrendo os usuários cadastrados no JSON
            System.out.println("Usuários no JSON: " + var.getNome() + " " + var.getCPF()  + " Tipo: " + var.getTipo());
        
        boolean loginValido = false;
        for(Usuarios var : listaUsuarios){ // Valiando o login e senha
            if(var.getCPF().equals(cpfDigitado) && var.verificarSenha(senhaDigitada)){
                loginValido = true;
                System.out.println("Logou!");
                if(var.getTipo().trim().equalsIgnoreCase("Cliente")){
                    System.out.println("Entrou como Cliente!");
                    
                    
                    
                    
                } else if (var.getTipo().trim().equalsIgnoreCase("Caixa")){
                    System.out.println("Entrou como Caixa!");
                    
                    
                    
                } else {
                    System.out.println("Entrou como Gerente!");
                }
            }
        }
        
        if(!loginValido)
            JOptionPane.showMessageDialog(null, "CPF ou senha incorretos. Tente novamente!", "Erro", JOptionPane.ERROR_MESSAGE);
    }
    
    public static void mostrarSenha(JCheckBox check, JPasswordField senha){
         if(check.isSelected())
            senha.setEchoChar((char)0);
        else
            senha.setEchoChar('*');
    }
    
    
    
    
    
    
    
}
