package controller;

import dao.Json;
import static dao.Json.credito;
import static dao.Json.lerUsuarios;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import user.Cliente;
import user.Usuarios;
import utils.ClienteService;
import view.Credito;
import view.Extrato;
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
    
    public static void telaExtrato(){
        Extrato telaExtrato = new Extrato(tela,true);
        telaExtrato.setLocationRelativeTo(tela);
        telaExtrato.setVisible(true);
    }
    
    public static void mostrarExtrato(JTextArea area){
        String extrato = Json.obterExtrato(cliente.getCPF());
        System.out.println("Extrato carregado: " + extrato);  
        area.setText(extrato);
    }
    
    public static void termos(JSlider slider, JTextPane painel){
        int valor = slider.getValue();  

        String termoAceite = """
        <html>
        <p>Eu, <b>%s</b>, inscrito no CPF<b>%s</b>, declaro que li e estou ciente das condições do empréstimo solicitado 
        junto ao <b>Banco Louback</b>, aceitando integralmente os seguintes termos:</p>
        <h3>1. Valor e Condições do Empréstimo</h3>
        <ul>
            <li>Valor solicitado: <b>R$ %d</b></li>  
            <li>Taxa de juros: <b>3%% ao mês</b></li>  
        </ul>
        <h3>2. Condições Gerais</h3>
        <ul>
            <li>O empréstimo está sujeito à análise de crédito e demais políticas do banco.</li>
            <li>O pagamento das parcelas deverá ser efetuado nas datas acordadas, sob pena de incidência de juros e multa por atraso.</li>
            <li>Em caso de inadimplência, poderão ser tomadas medidas administrativas e judiciais cabíveis para a cobrança da dívida.</li>
            <li>O cliente autoriza o banco a debitar automaticamente as parcelas em conta corrente, caso aplicável.</li>
            <li>O cliente declara que os dados fornecidos são verídicos e atualizados.</li>
        </ul>
        <h3>3. Proteção de Dados</h3>
        <p>O cliente autoriza o tratamento de seus dados para fins de análise e concessão do crédito, conforme a legislação vigente.</p>
        <br/>
        
        </html>
    """;
        String nomeCliente = cliente.getNome();
        String cpfCliente = cliente.getCPF();
        
        String termoFormatado = String.format(termoAceite, nomeCliente, cpfCliente, valor);

        painel.setContentType("text/html"); 
        painel.setText(termoFormatado);
    }
    
    public  static void setValor(JLabel lbl, JSlider slider, JTextPane painel){
        lbl.setText(String.valueOf(slider.getValue()));
        termos(slider, painel);
    }
    
    public static void setTermos(JButton btnSolicitar, JCheckBox checkAceito){
        btnSolicitar.setEnabled(checkAceito.isSelected());
    }
    
    public static void solicitarCredito(JSlider slider){
        double valor = slider.getValue(); 
        boolean resultado = credito(cliente, valor);
        if(resultado)
            JOptionPane.showMessageDialog(null,"Sua solicitação foi enviada com sucesso! Basta aguardar a liberação pelo gerente. ","Agora é só aguardar!", JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(null,"ERRO!","ERROR!!", JOptionPane.ERROR_MESSAGE);
    }
    
}
