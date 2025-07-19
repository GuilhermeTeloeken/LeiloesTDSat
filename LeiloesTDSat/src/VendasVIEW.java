import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class VendasVIEW extends JFrame {
    private JTable tblVendas;
    private JButton btnVoltar;

    public VendasVIEW() {
        super("Produtos Vendidos");
        initComponents();
        loadVendas();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Cabeçalho com botão de voltar
        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topo.add(new JLabel("Lista de Produtos Vendidos"));
        btnVoltar = new JButton("Voltar");
        btnVoltar.addActionListener(this::onVoltar);
        topo.add(btnVoltar);
        add(topo, BorderLayout.NORTH);

        // Tabela de vendas
        tblVendas = new JTable(new DefaultTableModel(
            new Object[][] {},
            new String[] {"ID", "Nome", "Valor", "Status"}
        ));
        add(new JScrollPane(tblVendas), BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
    }

    private void loadVendas() {
        try {
            ProdutosDAO dao = new ProdutosDAO();
            List<ProdutosDTO> vendidos = dao.listarProdutosVendidos();
            DefaultTableModel model = (DefaultTableModel) tblVendas.getModel();
            model.setRowCount(0);
            for (ProdutosDTO p : vendidos) {
                model.addRow(new Object[]{
                    p.getId(), p.getNome(), p.getValor(), p.getStatus()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao carregar vendas: " + e.getMessage());
        }
    }

    private void onVoltar(ActionEvent evt) {
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VendasVIEW().setVisible(true));
    }
}
