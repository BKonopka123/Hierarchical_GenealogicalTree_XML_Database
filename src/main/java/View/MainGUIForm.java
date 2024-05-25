package View;

import lombok.Getter;

import javax.swing.*;
import java.sql.ResultSet;
import java.util.*;

/**
 * Klasa odpowiadająca za wyświetlanie informacji za pomocą JSwinga.
 */
@Getter
public class MainGUIForm {
    private JFrame frame;
    private JPanel panel_main;
    private JPanel panel_initalization;
    private JPanel panel_activity;
    private JButton button_initailize;
    private JButton button_delete;
    private JTabbedPane tabbedPanel_main;
    private JPanel panel_addtree;
    private JPanel panel_addnode;
    private JPanel panel_deletenode;
    private JPanel panel_raport;
    private JButton button_addtree;
    private JTabbedPane tabbedPanel_raports;
    private JPanel panel_showWholeTree;
    private JPanel panel_showNodeParents;
    private JComboBox comboBox_wholeTree_treeChoose;
    private JLabel label_wholeTreeComboBox;
    private JButton button_wholeTreeShow;
    private JTable table_WholeTreeOutput;
    private JLabel label_showParents_Tree;
    private JComboBox comboBox_showParents_tree;
    private JLabel label_showParents_Node;
    private JComboBox comboBox_showParents_Node;
    private JButton button_showParents;
    private JTable table_showParents_output;
    private JPanel panel_wholeTreeOutput;
    private JLabel label_deleteInfo;
    private JComboBox comboBox_deleteNode_Tree;
    private JLabel label_deleteInfoNode;
    private JComboBox comboBox_deleteNode_Node;
    private JButton button_deleteNode;
    private JLabel label_add_Tree;
    private JComboBox comboBox_add_Tree;
    private JLabel label_add_Node;
    private JLabel label_add_Node_value;
    private JLabel label_add_Name;
    private JTextField textField_add_Name;
    private JLabel label_add_Surname;
    private JTextField textField_add_Surname;
    private JLabel label_add_Gender;
    private JTextField textField_add_Gender;
    private JLabel label_add_Dad;
    private JComboBox comboBox_add_Dad;
    private JLabel label_add_Mom;
    private JComboBox comboBox_add_Mom;
    private JLabel label_add_Birth;
    private JTextField textField_add_Birth;
    private JLabel label_add_Death;
    private JTextField textField_add_Death;
    private JButton button_addNode;
    private JPanel panel_parents_output;

    /**
     * Konstruktor klasy.
     */
    public MainGUIForm() {
        frame = new JFrame("MainGUIForm");
        frame.setContentPane(panel_main);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        //frame.pack();
        frame.setVisible(true);
        createUIComponents();
    }

    /**
     * Metoda inicjalizująca odpowiednie elementy GUI.
     */
    private void createUIComponents() {
        comboBox_wholeTree_treeChoose.addItem(1);
        comboBox_showParents_tree.addItem(1);
        comboBox_deleteNode_Tree.addItem(1);
        comboBox_add_Tree.addItem(1);
    }

    /**
     * Metoda aktualizująca checkBoxy odpowiadające za wybór drzewa genealogicznego w GUI.
     * @param treeIDs - lista dostępnych drzew genealogicznych.
     */
    public void setComboBox_treeChoose_Values(List<Integer> treeIDs) {
        comboBox_wholeTree_treeChoose.removeAllItems();
        comboBox_showParents_tree.removeAllItems();
        comboBox_deleteNode_Tree.removeAllItems();
        comboBox_add_Tree.removeAllItems();
        for(var id : treeIDs) {
            comboBox_add_Tree.addItem(id);
            comboBox_wholeTree_treeChoose.addItem(id);
            comboBox_showParents_tree.addItem(id);
            comboBox_deleteNode_Tree.addItem(id);
        }
    }

    /**
     * Metoda aktualizująca checkBoxy odpowiadające za wybór osoby w GUI.
     * @param nodeIDs - lista dostępnych osób.
     */
    public void setComboBox_NodeChoose_Values(List<Integer> nodeIDs) {
        comboBox_deleteNode_Node.removeAllItems();
        comboBox_add_Dad.removeAllItems();
        comboBox_add_Mom.removeAllItems();
        comboBox_showParents_Node.removeAllItems();
        comboBox_add_Dad.addItem(0);
        comboBox_add_Mom.addItem(0);
        Integer node_to_show = 0;
        if(nodeIDs != null) {
            for (var id : nodeIDs) {
                comboBox_add_Dad.addItem(id);
                comboBox_add_Mom.addItem(id);
                comboBox_deleteNode_Node.addItem(id);
                comboBox_showParents_Node.addItem(id);
                node_to_show = id;
            }
        }
        label_add_Node_value.setText(String.valueOf(node_to_show+1));
    }

    /**
     * Metoda odpowiadająca za wyświetlenie w tabeli w GUI raportu pierwszego.
     * @param result ResultSet otrzymany z odpowiedniego SELECTa.
     */
    public void setTable_WholeTreeOutput_init(ResultSet result){
        getPanel_wholeTreeOutput().removeAll();
        getPanel_wholeTreeOutput().setLayout(new BoxLayout(getPanel_wholeTreeOutput(), BoxLayout.Y_AXIS));
        try {
            int columnCount = result.getMetaData().getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                columnNames[i] = result.getMetaData().getColumnName(i + 1);
            }
            Object[][] data = new Object[100][columnCount];
            int i = 0;
            while (result.next()) {
                for (int j = 0; j < columnCount; j++) {
                    data[i][j] = result.getObject(j + 1);
                }
                i++;
            }
            JTable table = new JTable(data, columnNames);
            table_WholeTreeOutput = table;
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setVisible(true);
            scrollPane.setSize(800, 600);
            scrollPane.revalidate();
            scrollPane.repaint();
            getPanel_wholeTreeOutput().add(scrollPane);
            getPanel_wholeTreeOutput().revalidate();
            getPanel_wholeTreeOutput().repaint();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metoda odpowiadająca za wyświetlenie w tabeli w GUI raportu drugiego.
     * @param result ResultSet otrzymany z odpowiedniego SELECTa.
     */
    public void setTable_ParentsOutput_init(ResultSet result){
        getPanel_parents_output().removeAll();
        getPanel_parents_output().setLayout(new BoxLayout(getPanel_parents_output(), BoxLayout.Y_AXIS));
        try {
            int columnCount = result.getMetaData().getColumnCount();
            String[] columnNames = new String[columnCount];
            for (int i = 0; i < columnCount; i++) {
                columnNames[i] = result.getMetaData().getColumnName(i + 1);
            }
            Object[][] data = new Object[100][columnCount];
            int i = 0;
            while (result.next()) {
                for (int j = 0; j < columnCount; j++) {
                    data[i][j] = result.getObject(j + 1);
                }
                i++;
            }
            JTable table = new JTable(data, columnNames);
            table_showParents_output = table;
            JScrollPane scrollPane = new JScrollPane(table);
            scrollPane.setVisible(true);
            scrollPane.setSize(800, 600);
            scrollPane.revalidate();
            scrollPane.repaint();
            getPanel_parents_output().add(scrollPane);
            getPanel_parents_output().revalidate();
            getPanel_parents_output().repaint();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
