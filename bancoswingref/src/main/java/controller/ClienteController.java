package controller;

import static dao.Json.lerUsuarios;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import user.Cliente;
import user.Usuarios;
import utils.ClienteService;
import view.Credito;
import view.Login;
import view.TelaCliente;


public class ClienteController extends ClienteService{
    private static Cliente cliente;
    private static TelaCliente tela;
    
    public static void mostrarInfoCliente(String cpf, JLabel nomeCliente, JLabel saldo){
        List<Usuarios> listaUsuarios = lerUsuarios();
        
        for(Usuarios var : listaUsuarios){
            if(cpf.equals(var.getCPF())){
                if(var instanceof Cliente){
                    cliente = (Cliente) var;
                    nomeCliente.setText(cliente.getNome());
                    NumberFormat formato = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                    saldo.setText(formato.format(cliente.getSaldo()));
                }  
            }      
        }
    }
    
    
    public static void mostrarSaldoCliente(JLabel saldo, JCheckBox checkBoxSaldo){
        if (checkBoxSaldo.isSelected()) {
            saldo.setIcon(null);
            NumberFormat formato = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            saldo.setText(formato.format(cliente.getSaldo())); 
        } else {
            saldo.setText("***"); 
        }
    }
    
    public static void deslogar(){
        Login login = new Login();
        login.setVisible(true);
        login.setLocationRelativeTo(null);
    }
    
    public static void instaciaTelaCliente(TelaCliente tela1){
        tela = tela1;
    }
    
    public static void telaCredito(){
        //String cpf = cliente.getCPF();
        Credito telaCredito = new Credito(tela, true);
        telaCredito.setLocationRelativeTo(tela);
        telaCredito.setVisible(true);
        
        
    }
     
}
