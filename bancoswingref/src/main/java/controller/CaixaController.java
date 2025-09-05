package controller;

import static dao.Json.lerUsuarios;
import java.util.List;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import user.Caixa;
import user.Cliente;
import user.Usuarios;
import utils.CaixaService;
import view.TelaCaixa;
import view.TelaTransferencia;


public class CaixaController extends CaixaService{
    private static Caixa caixa;
    private static TelaCaixa tela;
    private static Cliente cliente_origem;
    private static Cliente cliente_destino;
    
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
    
    public static void controladorTransferencia(JFormattedTextField origem,JFormattedTextField destino, JFormattedTextField v){
        double valor = formatarValor(v.getText());

        if(origem.getText() == null || destino.getText() == null){
            JOptionPane.showMessageDialog(null, "Favor preencher corretamente todos os campos!", "Atenção!", JOptionPane.WARNING_MESSAGE);
        } else if (valor == 0){
            JOptionPane.showMessageDialog(null, "Insira um valor que não seja 0.", "Atenção!", JOptionPane.WARNING_MESSAGE);
        } else if (valor < 0){
            JOptionPane.showMessageDialog(null, "Favor preencher corretamente o campo valor!", "Atenção!", JOptionPane.WARNING_MESSAGE);
        } else {
            if(origem.getText() != null || destino.getText() != null){
                boolean cadastrado = verificarCadastro(origem.getText(),destino.getText());
                if(!cadastrado)
                    JOptionPane.showMessageDialog(null, "CPF inválido! Favor inserir um CPF que tenha conta no banco.", "Atenção!", JOptionPane.WARNING_MESSAGE);
                else {
                    String cpf_origem = origem.getText();
                    String cpf_Destino = destino.getText();
                    Cliente cliente_origem = retornaCliente(cpf_origem);
                    Cliente cliente_destino = retornaCliente(cpf_origem);

                    if (cliente_origem.getSaldo() < valor) {
                        JOptionPane.showMessageDialog(null, "Saldo insuficiente!", "Atenção!", JOptionPane.WARNING_MESSAGE);
                    } else {
                        transferir(cliente_origem, cliente_destino, valor);

                        JOptionPane.showMessageDialog(null, "Transferência realizada com sucesso!", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }   
        }  
    }       
                
            
       
    
    
    
    
    
}
