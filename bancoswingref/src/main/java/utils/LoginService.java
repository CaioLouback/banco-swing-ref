package utils;

import static dao.Json.lerUsuarios;
import java.util.List;
import user.Usuarios;

public class LoginService {
    
    protected LoginService(){}; // Evitar instaciar 
    
    protected static boolean autenticadorLogin(String cpfDigitado, String senhaDigitada){ 
        boolean loginValido = false;
        List<Usuarios> listaUsuarios = lerUsuarios();
       
        for(Usuarios var : listaUsuarios){ // Valiando o login e senha
            System.out.println("Usuários no JSON: " + var.getNome() + " " + var.getCPF()  + " Tipo: " + var.getTipo());
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
    
    protected static boolean verificarVazio(String cpfDigitado, String senhaDigitada){
        if(cpfDigitado.trim().isEmpty() || senhaDigitada.length() == 0){
            return true;
        }
        return false;
    }

}
