package leiloestdsat;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class cadastroVIEW extends JFrame {
    private JTextField txtNome;
    private JTextField txtValor;
    private JButton btnCadastrar;
    private JButton btnProdutos;
    private JButton btnVender;
    private JButton btnConsultarVendas;
    private JTable tblProdutos;

    public cadastroVIEW() {
        super("Sistema de Leilões");
        initComponents();
    }

    private void initComponents() {
        // Layout principal
        setLayout(new BorderLayout(10, 10));

        // Painel de cadastro e ações
        JPanel painel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        painel.add(new JLabel("Nome:"));
        txtNome = new JTextField(15);
        painel.add(txtNome);
        painel.add(new JLabel("Valor:"));
        txtValor = new JTextField(8);
        painel.add(txtValor);

        btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.addActionListener(this::onCadastrar);
        painel.add(btnCadastrar);

        btnProdutos = new JButton("Consultar Produtos");
        btnProdutos.addActionListener(this::onConsultar);
        painel.add(btnProdutos);

        btnVender = new JButton("Vender");
        btnVender.addActionListener(this::onVender);
        painel.add(btnVender);

        btnConsultarVendas = new JButton("Consultar Vendas");
        btnConsultarVendas.addActionListener(this::onConsultarVendas);
        painel.add(btnConsultarVendas);

        add(painel, BorderLayout.NORTH);

        // Tabela de produtos
        tblProdutos = new JTable(new DefaultTableModel(
            new Object[][] {},
            new String[] {"ID", "Nome", "Valor", "Status"}
        ));
        add(new JScrollPane(tblProdutos), BorderLayout.CENTER);

        // Configurações finais da janela
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 450);
        setLocationRelativeTo(null);
    }

    private void onCadastrar(ActionEvent evt) {
        try {
            ProdutosDTO p = new ProdutosDTO();
            p.setNome(txtNome.getText());
            p.setValor(Integer.parseInt(txtValor.getText()));
            p.setStatus("A Venda");

            ProdutosDAO dao = new ProdutosDAO();
            boolean ok = dao.cadastrarProduto(p);
            JOptionPane.showMessageDialog(this,
                ok ? "Cadastro realizado com sucesso!" : "Erro ao realizar cadastro.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage());
        }
    }

    private void onConsultar(ActionEvent evt) {
        try {
            ProdutosDAO dao = new ProdutosDAO();
            List<ProdutosDTO> lista = dao.listarProdutos();
            DefaultTableModel m = (DefaultTableModel) tblProdutos.getModel();
            m.setRowCount(0);
            for (ProdutosDTO produto : lista) {
                m.addRow(new Object[]{
                    produto.getId(),
                    produto.getNome(),
                    produto.getValor(),
                    produto.getStatus()
                });
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao listar produtos: " + e.getMessage());
        }
    }

    private void onVender(ActionEvent evt) {
        int row = tblProdutos.getSelectedRow();
        if (row >= 0) {
            Long id = (Long) tblProdutos.getValueAt(row, 0);
            boolean sucesso = new ProdutosDAO().venderProduto(id);
            JOptionPane.showMessageDialog(this,
                sucesso ? "Produto vendido com sucesso!" : "Falha ao vender produto.");
            // atualiza a lista
            onConsultar(evt);
        } else {
            JOptionPane.showMessageDialog(this,
                "Selecione um produto antes de vender.");
        }
    }

    private void onConsultarVendas(ActionEvent evt) {
        // abre a nova janela de vendas
        new VendasVIEW().setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new cadastroVIEW().setVisible(true));
    }
}
