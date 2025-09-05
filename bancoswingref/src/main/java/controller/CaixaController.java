package controller;

import static dao.Json.lerUsuarios;
import java.util.List;
import javax.swing.JLabel;
import user.Caixa;
import user.Usuarios;
import view.TelaCaixa;
import view.TelaTransferencia;


public class CaixaController {
    private static Caixa caixa;
    private static TelaCaixa tela;
    
    public static void nomeCaixa(JLabel nome, String cpf){
        List<Usuarios> listaUsuarios = lerUsuarios();
        
        for(Usuarios var : listaUsuarios){
            if(cpf.equals(var.getCPF())){
                if(var instanceof Caixa){
                    caixa = (Caixa) var;
                    nome.setText(caixa.getNome());
                }  
            }      
        }  
    }
    
    public static void instanciaTelaCaixa(TelaCaixa tela1){
        tela = tela1;
    }
    
    public static void abrirTelaTransferencia(){
        TelaTransferencia telaT = new TelaTransferencia(tela ,true);
        telaT.setLocationRelativeTo(tela);
        telaT.setVisible(true);
    }
    
    
    
    
    
    
}
