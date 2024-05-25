package Controller;

import Model.UtilsDatabase;
import View.MainGUIForm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Klasa łącząca backend z GUI.
 */
public class MainGUIController {
    private static MainGUIForm mainGUIForm;
    private static MainGUIController mainGUIController;
    private static final UtilsDatabase utilsDatabase = new UtilsDatabase();


    /**
     * Kontruktor.
     */
    public MainGUIController() {
        control();
    }

    /**
     * Metoda zwracająca instancję klasy.
     * @return instancja klasy.
     */
    public static MainGUIController getInstance() {
        if(Objects.isNull(mainGUIController)) {
            mainGUIForm = new MainGUIForm();
            mainGUIController = new MainGUIController();
        }
        init();
        return mainGUIController;
    }

    /**
     * Metoda zarządzająca kontrolerem.
     */
    public void control() {
        /**
         * Wywołanie połączenia z bazą danych.
         */
        try {
            utilsDatabase.connectToDatabase();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

        /**
         * Event Listener zarządzający zamknięceim okna.
         */
        mainGUIForm.getFrame().addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                utilsDatabase.disconnectFromDatabase();
            }
        });

        /**
         * Event Listener zarządzający przyciekiem inicjalizującym bazę danych.
         */
        mainGUIForm.getButton_initailize().addActionListener(e -> {
            try {
                utilsDatabase.initialization_sql();
            } catch (Exception exception) {
                JOptionPane.showConfirmDialog(null, "Baza danych nie została zainicjalizowana", "Error", JOptionPane.DEFAULT_OPTION);
                throw new RuntimeException(exception);
            }
            JOptionPane.showConfirmDialog(null, "Baza danych zainicjalizowana", "Initialize Database", JOptionPane.DEFAULT_OPTION);
        });

        /**
         * Event Listener zarządzający przyciskiem usuwającym bazę danych.
         */
        mainGUIForm.getButton_delete().addActionListener(e -> {
            try {
                utilsDatabase.delete_sql();
            } catch(Exception exception) {
                JOptionPane.showConfirmDialog(null, "Baza danych nie została usunięta", "Error", JOptionPane.DEFAULT_OPTION);
                throw new RuntimeException(exception);
            }
            JOptionPane.showConfirmDialog(null, "Baza danych usunięta", "Initialize Database", JOptionPane.DEFAULT_OPTION);
        });

        /**
         * Event Listener zarządzający przyciskiem dodającym drzewo genealogiczne do bazy danych.
         */
        mainGUIForm.getButton_addtree().addActionListener( e-> {
                try {
                    Integer id = utilsDatabase.addTreeUD();
                    mainGUIForm.setComboBox_treeChoose_Values(utilsDatabase.getTreeIDs());
                    JOptionPane.showConfirmDialog(null, "Dodano drzewo o id:" + id, "Dodano drzewo", JOptionPane.DEFAULT_OPTION);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        );

        /**
         * Event Listener zarządzający przyciskiem wyświetlajacym raport numer 1.
         */
        mainGUIForm.getButton_wholeTreeShow().addActionListener( e-> {
                try {
                    Integer treeID = (Integer) mainGUIForm.getComboBox_wholeTree_treeChoose().getSelectedItem();
                    ResultSet result = utilsDatabase.getWholeTree(treeID);
                    mainGUIForm.setTable_WholeTreeOutput_init(result);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        );

        /**
         * Event Listener aktualizający ComboBox który wyświetla dostępne Osoby, w panelu usuwającym osobę.
         */
        mainGUIForm.getComboBox_deleteNode_Tree().addActionListener (new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Integer treeID = (Integer) mainGUIForm.getComboBox_deleteNode_Tree().getSelectedItem();
                try {
                    mainGUIForm.setComboBox_NodeChoose_Values(utilsDatabase.getNodeIDs(treeID));
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        /**
         * Event Listener zarządzający przyciskiem usuwającym osobę z bazy danych.
         */
        mainGUIForm.getButton_deleteNode().addActionListener( e-> {
                try {
                    Integer treeID = (Integer) mainGUIForm.getComboBox_deleteNode_Tree().getSelectedItem();
                    Integer nodeID = (Integer) mainGUIForm.getComboBox_deleteNode_Node().getSelectedItem();
                    boolean result = utilsDatabase.deleteNode(treeID, nodeID);
                    if(result) {
                        JOptionPane.showConfirmDialog(null, "Usunięto Osobę o id: " + nodeID + "w drzewie " + treeID, "Usunięto Osobę", JOptionPane.DEFAULT_OPTION);
                    }
                    else {
                        JOptionPane.showConfirmDialog(null, "Nie udało sie usunąć Osoby! ", "Error", JOptionPane.DEFAULT_OPTION);
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        );

        /**
         * Event Listener aktualizający ComboBox który wyświetla dostępne Osoby, w panelu dodającym osobę.
         */
        mainGUIForm.getComboBox_add_Tree().addActionListener (new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Integer treeID = (Integer) mainGUIForm.getComboBox_add_Tree().getSelectedItem();
                try {
                    mainGUIForm.setComboBox_NodeChoose_Values(utilsDatabase.getNodeIDs(treeID));
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        /**
         * Event Listener zarządzający przyciskiem dodającym osobę z bazy danych.
         */
        mainGUIForm.getButton_addNode().addActionListener( e-> {
                Integer treeId = (Integer) mainGUIForm.getComboBox_add_Tree().getSelectedItem();
                Integer nodeId = Integer.valueOf(mainGUIForm.getLabel_add_Node_value().getText());
                String name = mainGUIForm.getTextField_add_Name().getText();
                String surname = mainGUIForm.getTextField_add_Surname().getText();
                String gender = mainGUIForm.getTextField_add_Gender().getText();
                Integer dadId = mainGUIForm.getComboBox_add_Dad().getSelectedIndex();
                Integer momId = mainGUIForm.getComboBox_add_Mom().getSelectedIndex();
                String birth = mainGUIForm.getTextField_add_Birth().getText();
                String death = mainGUIForm.getTextField_add_Death().getText();
                if(name.isEmpty()){
                    JOptionPane.showConfirmDialog(null, "Imie nie moze być nullem! ", "Error", JOptionPane.DEFAULT_OPTION);
                }
                else if(surname.isEmpty()){
                    JOptionPane.showConfirmDialog(null, "Nazwisko nie moze być nullem! ", "Error", JOptionPane.DEFAULT_OPTION);
                }
                else if(gender.isEmpty()){
                    JOptionPane.showConfirmDialog(null, "Płeć nie moze być nullem! ", "Error", JOptionPane.DEFAULT_OPTION);
                }
                else if(birth.isEmpty()){
                    JOptionPane.showConfirmDialog(null, "Data urodzin nie może być nullem! ", "Error", JOptionPane.DEFAULT_OPTION);
                }
                else if(!birth.matches("[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]")) {
                    JOptionPane.showConfirmDialog(null, "Data urodzin powinna być w formacie 1999-01-01 ", "Error", JOptionPane.DEFAULT_OPTION);
                }
                else if(!death.isEmpty() && !death.matches("[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]")) {
                    JOptionPane.showConfirmDialog(null, "Data śmierci powinna być w formacie 1999-01-01 ", "Error", JOptionPane.DEFAULT_OPTION);
                }
                else {
                    try {
                        boolean result = utilsDatabase.addNode(treeId, nodeId, name, surname, gender, dadId, momId, birth, death);
                        if(result){
                            JOptionPane.showConfirmDialog(null, "W drzewie: " +  treeId + " dodano osobę: " + nodeId, "Dodano osobę", JOptionPane.DEFAULT_OPTION);
                            mainGUIForm.setComboBox_NodeChoose_Values(utilsDatabase.getNodeIDs(treeId));
                        }
                        else {
                            JOptionPane.showConfirmDialog(null, "Nie dodano osoby! ", "Error", JOptionPane.DEFAULT_OPTION);
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        );

        /**
         * Event Listener aktualizający ComboBox który wyświetla dostępne Osoby, w panelu pokazującym raport 2.
         */
        mainGUIForm.getComboBox_showParents_tree().addActionListener (new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Integer treeID = (Integer) mainGUIForm.getComboBox_showParents_tree().getSelectedItem();
                try {
                    mainGUIForm.setComboBox_NodeChoose_Values(utilsDatabase.getNodeIDs(treeID));
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        /**
         * Event Listener zarządzający przyciskiem pokazującym raport 2.
         */
        mainGUIForm.getButton_showParents().addActionListener( e->{
                try {
                    Integer treeID = (Integer) mainGUIForm.getComboBox_showParents_tree().getSelectedItem();
                    Integer nodeID = (Integer) mainGUIForm.getComboBox_showParents_Node().getSelectedItem();
                    ResultSet result = utilsDatabase.getParents(treeID, nodeID);
                    mainGUIForm.setTable_ParentsOutput_init(result);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        );
    }

    /**
     * Metoda inicjalizująca.
     */
    private static void init() {
        mainGUIForm.getFrame().setVisible(true);
    }
}
