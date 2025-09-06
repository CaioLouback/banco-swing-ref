package controller;

import static dao.Json.lerSolicitacaoCredito;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import utils.Movimentacao;


public class GerenteController {
    
    
    public static void pesquisar(JTable tbTabelaCredito){
        List<Movimentacao> soli = lerSolicitacaoCredito();
        atualizarTabelaCredito(tbTabelaCredito);
        
    }
    
    
    private static void atualizarTabelaCredito(JTable tbTabelaCredito) {
        List<Movimentacao> solicitacoesCredito = lerSolicitacaoCredito();

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Tipo");
        model.addColumn("Valor");

        for (Movimentacao mov : solicitacoesCredito) {
            model.addRow(new Object[]{mov.getTipo(), mov.getValor()});
        }

        tbTabelaCredito.setModel(model);
    }
}
