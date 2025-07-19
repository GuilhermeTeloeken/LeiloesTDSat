// src/leiloestdsat/cadastroVIEW.java
package leiloestdsat;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class cadastroVIEW extends JFrame {
    private JTextField txtNome;
    private JTextField txtValor;
    private JButton btnCadastrar;
    private JButton btnProdutos;
    private JTable tblProdutos;

    public cadastroVIEW() {
        super("Sistema de Leilões");
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        // Painel de cadastro
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

        add(painel, BorderLayout.NORTH);

        // Tabela de produtos
        tblProdutos = new JTable(new DefaultTableModel(
            new Object[][] {},
            new String[] {"ID", "Nome", "Valor", "Status"}
        ));
        add(new JScrollPane(tblProdutos), BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
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
            List<ProdutosDTO> lista = dao.listarTodos();
            DefaultTableModel m = (DefaultTableModel) tblProdutos.getModel();
            m.setRowCount(0);
            for (ProdutosDTO p : lista) {
                m.addRow(new Object[]{p.getId(), p.getNome(), p.getValor(), p.getStatus()});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erro ao listar produtos: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new cadastroVIEW().setVisible(true));
    }
}
