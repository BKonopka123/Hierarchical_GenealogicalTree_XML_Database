import API.APILibrary;
import Model.UtilsDatabase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Klasa odpowiadająca za testy jednostkowe.
 */
public class TestClass {
    static Connection connection;
    static UtilsDatabase database;

    /**
     * Metoda wywoływana przed każdym testem, odpowiadająca za nawiązanie połączenia z bazą danych, oraz za inicjalizacje bazy.
     */
    @BeforeAll
    static void setUp() throws SQLException, ClassNotFoundException {
        database = new UtilsDatabase();
        database.connectToDatabase();
        connection = database.getConnection();
        database.initialization_sql();
    }

    /**
     * Metoda testująca dodawanie drzewa genealogicznego do tabeli.
     */
    @Test
    void addTreeTest() throws SQLException {
        Integer id = APILibrary.addTree(connection);
        PreparedStatement sql2 = connection.prepareStatement("SELECT DrzewoGenealogiczneID FROM DrzewoGenealogiczne ORDER BY DrzewoGenealogiczneID ASC");
        ResultSet resultSet = sql2.executeQuery();
        Integer id2 = 0;
        while(resultSet.next()) {
            id2 = resultSet.getInt("DrzewoGenealogiczneID");
        }
        assertEquals(id, id2);
    }

    /**
     * Metoda testująca dodawanie osoby do drzewa genealogicznego.
     */
    @Test
    void addNode() throws SQLException {
        Integer treeId = 1;
        Integer nodeId = 9;
        String name = "Bartosz";
        String surname = "Konopka";
        String gender = "Mezczyzna";
        Integer dadId = 0;
        Integer momId = 0;
        String birth = "2002-03-4";
        String death ="";
        boolean isAdded = APILibrary.addNode(connection, treeId, String.valueOf(nodeId), name, surname, gender, String.valueOf(dadId), String.valueOf(momId), birth, death);
        assertTrue(isAdded);
    }

    /**
     * Metoda testujaca usuwanie osoby z drzewa genealogicznego.
     */
    @Test
    void deleteNode() throws SQLException {
        boolean isDeleted = APILibrary.deleteNode(connection, 1, 1);
        assertTrue(isDeleted);
    }
}
