package Model;

import com.microsoft.sqlserver.jdbc.SQLServerDriver;
import org.apache.ibatis.jdbc.ScriptRunner;

import API.APILibrary;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
import java.util.*;

/**
 * Klasa odpowiadająca za backend
 */
public class UtilsDatabase {

    private static Connection connection;

    /**
     * Metoda zwracająca połącznie z bazą danych
     * @return połączenie z bazą danych
     */
    public Connection getConnection(){
        return connection;
    }

    /**
     * Metoda odpowiadająca za nawiązanie połączenia z bazą danych.
     */
    public void connectToDatabase() throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String connectionUrl = "jdbc:sqlserver://DESKTOP-NKOR49T;encrypt=true;trustServerCertificate=true;integratedSecurity=true;";
        connection = DriverManager.getConnection(connectionUrl);
        System.out.println("Connection created");
    }

    /**
     * Metoda odpoiwadająca za zakończenie połączenia z bazą danych.
     */
    public void disconnectFromDatabase() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Disconnected from the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda inicjalizująca bazę danych za pomocą sryptów SQL.
     */
    public void initialization_sql() {
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        scriptRunner.setSendFullScript(true);
        scriptRunner.setStopOnError(true);
        try {
            scriptRunner.runScript(new java.io.FileReader("src/main/resources/initialization_sql.sql"));
            scriptRunner.runScript(new java.io.FileReader("src/main/resources/fill_sql.sql"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metoda usuwająca zawartość bazy danych za pomocą poleceń SQL.
     */
    public void delete_sql() {
        ScriptRunner scriptRunner = new ScriptRunner(connection);
        scriptRunner.setSendFullScript(true);
        scriptRunner.setStopOnError(true);
        try {
            scriptRunner.runScript(new java.io.FileReader("src/main/resources/delete_sql.sql"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Metoda wywołująca skrypt API dodający drzewo genealogiczne do bazy danych.
     * @return Id utworzonego drzewa genealogicznego.
     */
    public Integer addTreeUD() throws SQLException {
        try {
            return APILibrary.addTree(connection);
        } catch (SQLException exception) {
            JOptionPane.showConfirmDialog(null, "Błąd: " + exception.getMessage(), "Error", JOptionPane.DEFAULT_OPTION);
            return 0;
        }
    }

    /**
     * Metoda zwracająca ID wszystkich dostępnych w bazie drzew genealogicznych.
     * @return Lista ID drzew genealogicznych.
     */
    public List<Integer> getTreeIDs() throws SQLException {
        try {
            PreparedStatement sql = connection.prepareStatement("SELECT DrzewoGenealogiczneID FROM DrzewoGenealogiczne ORDER BY DrzewoGenealogiczneID ASC");
            ResultSet resultSet = sql.executeQuery();
            List<Integer> result = new ArrayList<Integer>();
            while (resultSet.next()) {
                result.add(resultSet.getInt("DrzewoGenealogiczneID"));
            }
            return result;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Metoda zwracająca ID wszystkich osób znajdujących się w konkretnym drzewie genealogicznym.
     * @param treeID Drzewo genealogiczne z któego wyciągamy ID osób.
     * @return ID osób.
     */
    public List<Integer> getNodeIDs(Integer treeID) throws SQLException {
        try {
            PreparedStatement sql = connection.prepareStatement("SELECT \n" +
                    "    Tbl.Col.value('(Osoba_id)[1]', 'int') AS OsobaID\n" +
                    "FROM \n" +
                    "    DrzewoGenealogiczne\n" +
                    "CROSS APPLY \n" +
                    "    XDocumentDrzewoGenealogiczne.nodes('Osoba') AS Tbl(Col)\n" +
                    "WHERE \n" +
                    "    DrzewoGenealogiczneID = " + treeID + ";");
            ResultSet resultSet = sql.executeQuery();
            List<Integer> result = new ArrayList<Integer>();
            while (resultSet.next()) {
                result.add(resultSet.getInt("OsobaID"));
            }
            return result;
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Metoda wywołujaca skrypt API odpowiedzialny za generownie raportu z wartością node_id 0
     * @param treeID drzewo genealogiczne na którym generowany jest raport.
     * @return ResultSet zawierający output otrzymany z SELECTa.
     */
    public ResultSet getWholeTree(Integer treeID) throws SQLException {
        try {
            return APILibrary.report(connection, treeID, 0);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    /**
     * Metoda wywołująca skrypt API odpowiedzailny za usunięcie osoby z drzewa genealogicznego.
     * @param treeId ID drzewa z którego usuwamy osobę.
     * @param nodeID ID osoby którą usuwamy.
     * @return true/false w zależności czy skrypt się powiódł.
     */
    public boolean deleteNode(Integer treeId, Integer nodeID) throws SQLException {
        return APILibrary.deleteNode(connection, treeId, nodeID);
    }

    /**
     * Metoda wywołująca skrypt API odpowiedzialnyz a dodanie osoby do drzewa genealogicznego.
     * @param tree_id ID drzewa do któego dodajemy osobę.
     * @param person_id ID osoby którą dodajemy.
     * @param name Imię dodawanej osoby.
     * @param surname Nazwisko dodawanej osoby.
     * @param gender Płeć dodawanej osoby.
     * @param dads_id ID ojca dodawanej osoby.
     * @param moms_id ID matki dodawanej osoby.
     * @param birth Data urodzin dodawanej osoby.
     * @param death Data śmierci dodawanej osoby.
     * @return true/false w zależności czy skrypt się powiódł.
     */
    public boolean addNode(Integer tree_id, Integer person_id, String name, String surname, String gender, Integer dads_id, Integer moms_id, String birth, String death) throws SQLException{
        return APILibrary.addNode(connection, tree_id, String.valueOf(person_id), name, surname, gender, String.valueOf(dads_id), String.valueOf(moms_id), birth, death);
    }

    /**
     * Metoda wywołujaca skrypt API odpowiedzialny za generownie raportu z wartością node_id zdefiniowaną przez użytkownika
     * @param treeID drzewo genealogiczne na którym generowany jest raport.
     * @param nodeID Osoba zdefiniowana przez użytkownika.
     * @return ResultSet zawierający output otrzymany z SELECTa.
     */
    public ResultSet getParents(Integer treeID, Integer nodeID) throws SQLException {
        try {
            return APILibrary.report(connection, treeID, nodeID);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}
