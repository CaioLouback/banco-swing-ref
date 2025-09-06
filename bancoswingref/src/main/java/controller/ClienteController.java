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
import view.Credito;
import view.Extrato;
import view.Login;
import view.TelaCliente;


/**
 * Controlador responsável pelo gerenciamento das operações do cliente.
 * 
 * <p>
 * Esta classe fornece métodos estáticos para:
 * <ul>
 *     <li>Exibir informações do cliente (nome, saldo);</li>
 *     <li>Abrir telas de crédito e extrato;</li>
 *     <li>Gerenciar termos de empréstimo;</li>
 *     <li>Controlar solicitações de crédito;</li>
 *     <li>Deslogar o cliente;</li>
 *     <li>Atualizar valores e campos relacionados a sliders e checkboxes.</li>
 * </ul>
 * </p>
 * 
 * @author Caio
 * @version 1.0
 * @since 2025-09-06
 */

public class ClienteController{
    private static Cliente cliente; /** Cliente atualmente logado */
    private static TelaCliente tela; /** Tela principal do cliente */
    
    /**
     * Exibe informações do cliente (nome e saldo) em labels.
     * 
     * @param cpf CPF do cliente
     * @param nomeCliente JLabel que exibirá o nome do cliente
     * @param saldo JLabel que exibirá o saldo do cliente
     */
    
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
    
    /**
     * Mostra ou oculta o saldo do cliente conforme o estado do checkbox.
     * 
     * @param saldo JLabel do saldo
     * @param checkBoxSaldo JCheckBox para mostrar/ocultar saldo
     */
    
    public static void mostrarSaldoCliente(JLabel saldo, JCheckBox checkBoxSaldo){
        if (checkBoxSaldo.isSelected()) {
            saldo.setIcon(null);
            NumberFormat formato = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            saldo.setText(formato.format(cliente.getSaldo())); 
        } else {
            saldo.setText("***"); 
        }
    }
    
    /**
     * Desloga o cliente e abre a tela de login.
     */
    
    public static void deslogar(){
        Login login = new Login();
        login.setVisible(true);
        login.setLocationRelativeTo(null);
    }
    
    /**
     * Armazena a referência da tela principal do cliente.
     * 
     * @param tela1 Instância da {@link TelaCliente} a ser armazenada
     */
    
    public static void instaciaTelaCliente(TelaCliente tela1){
        tela = tela1;
    }
    
    /**
     * Abre a tela de solicitação de crédito.
     */
    
    public static void telaCredito(){
        //String cpf = cliente.getCPF();
        Credito telaCredito = new Credito(tela, true);
        telaCredito.setLocationRelativeTo(tela);
        telaCredito.setVisible(true);   
    }
    
    /**
     * Abre a tela de extrato bancário.
     */
    
    public static void telaExtrato(){
        Extrato telaExtrato = new Extrato(tela,true);
        telaExtrato.setLocationRelativeTo(tela);
        telaExtrato.setVisible(true);
    }
    
    /**
     * Carrega o extrato do cliente e exibe em um JTextArea.
     * 
     * @param area JTextArea onde o extrato será exibido
     */
    
    public static void mostrarExtrato(JTextArea area){
        String extrato = Json.obterExtrato(cliente.getCPF());
        System.out.println("Extrato carregado: " + extrato);  
        area.setText(extrato);
    }
    
    /**
     * Atualiza o painel de termos de crédito de acordo com o valor do slider.
     * 
     * @param slider JSlider com o valor do empréstimo
     * @param painel JTextPane que exibirá os termos
     */
    
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
    
    /**
     * Atualiza o valor do JLabel e os termos do crédito conforme o slider.
     * 
     * @param lbl JLabel que exibirá o valor
     * @param slider JSlider com o valor do empréstimo
     * @param painel JTextPane com os termos
     */
    
    public  static void setValor(JLabel lbl, JSlider slider, JTextPane painel){
        lbl.setText(String.valueOf(slider.getValue()));
        termos(slider, painel);
    }
    
    /**
     * Habilita ou desabilita o botão de solicitação de crédito conforme o checkbox.
     * 
     * @param btnSolicitar JButton para solicitar crédito
     * @param checkAceito JCheckBox indicando se o cliente aceitou os termos
     */
    
    public static void setTermos(JButton btnSolicitar, JCheckBox checkAceito){
        btnSolicitar.setEnabled(checkAceito.isSelected());
    }
    
    /**
     * Solicita crédito para o cliente de acordo com o valor do slider.
     * 
     * @param slider JSlider com o valor do crédito solicitado
     */
    
    public static void solicitarCredito(JSlider slider){
        double valor = slider.getValue(); 
        boolean resultado = credito(cliente, valor);
        if(resultado)
            JOptionPane.showMessageDialog(null,"Sua solicitação foi enviada com sucesso! Basta aguardar a liberação pelo gerente. ","Agora é só aguardar!", JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(null,"ERRO!","ERROR!!", JOptionPane.ERROR_MESSAGE);
    }
    
}
