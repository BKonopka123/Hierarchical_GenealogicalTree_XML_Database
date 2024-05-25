package API;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Klasa przetrzymująca bibliotekę API.
 */
public class APILibrary {
    /**
     * Metoda dodająca drzewo genealogiczne do bazy danych.
     * @param connection Połączenie z bazą danych.
     * @return ID dodanego drzewa.
     */
    public static Integer addTree(Connection connection) throws SQLException {
        PreparedStatement sql1 = connection.prepareStatement("INSERT INTO DrzewoGenealogiczne(XDocumentDrzewoGenealogiczne) VALUES ('<?xml version=\"1.0\"?>')");
        sql1.executeUpdate();
        PreparedStatement sql2 = connection.prepareStatement("SELECT DrzewoGenealogiczneID FROM DrzewoGenealogiczne ORDER BY DrzewoGenealogiczneID ASC");
        ResultSet resultSet = sql2.executeQuery();
        int id = 0;
        while(resultSet.next()) {
            id = resultSet.getInt("DrzewoGenealogiczneID");
        }
        return id;
    }

    /**
     * Metoda generująca raport 1 lub 2. W przypadku gdy podany node_id jest równy 0 generowany jest raport wypisujacy
     * wszystkie osoby znajdujące się w konkretnym drzewie genealogicznym.
     * Gdy node_id jest różne od 0, interpretowane jest jako ID Osoby. W tym przypadku raport generuje rodziców osoby o
     * określonym ID.
     * @param connection Połączenie z bazzą danych.
     * @param tree_id Drzewo genealogiczne z którego generowany jest raport.
     * @param node_id ID osoby której rodziców chcemy wyświetlić.
     * @return ResultSet przetrzymujący output SELECTu.
     */
    public static ResultSet report(Connection connection, Integer tree_id, Integer node_id) throws SQLException {
        PreparedStatement sql;
        if(node_id == 0) {
            sql = connection.prepareStatement("SELECT \n" +
                    "\tTbl.Col.value('(Osoba_id)[1]','varchar(20)') AS ID, \n" +
                    "\tTbl.Col.value('(Imie)[1]', 'varchar(20)') AS Imie, \n" +
                    "\tTbl.Col.value('(Nazwisko)[1]', 'varchar(20)') AS Nazwisko,\n" +
                    "\tTbl.Col.value('(Plec)[1]', 'varchar(20)') AS Plec,\n" +
                    "\tTbl.Col.value('(Ojciec_id)[1]', 'varchar(20)') AS Ojciec_id,\n" +
                    "\tTbl.Col.value('(Matka_id)[1]', 'varchar(20)') AS Matka_id,\n" +
                    "\tTbl.Col.value('(Urodziny)[1]', 'varchar(20)') AS Urodziny,\n" +
                    "\tTbl.Col.value('(Smierc)[1]', 'varchar(20)') AS Smierc\n" +
                    "FROM \n" +
                    "    DrzewoGenealogiczne\n" +
                    "CROSS APPLY \n" +
                    "    XDocumentDrzewoGenealogiczne.nodes('Osoba') AS Tbl(Col)\n" +
                    "WHERE \n" +
                    "    DrzewoGenealogiczneID = " + tree_id);
        }
        else {
            sql = connection.prepareStatement("WITH ParentIDs AS (\n" +
                    "    SELECT\n" +
                    "        T.O.value('(Ojciec_id/text())[1]', 'NVARCHAR(50)') AS Ojciec_id,\n" +
                    "        T.O.value('(Matka_id/text())[1]', 'NVARCHAR(50)') AS Matka_id\n" +
                    "    FROM DrzewoGenealogiczne\n" +
                    "    CROSS APPLY XDocumentDrzewoGenealogiczne.nodes('Osoba[Osoba_id=" + node_id + "]') T(O)\n" +
                    "\tWHERE DrzewoGenealogiczneID = " + tree_id + "\n" +
                    ")\n" +
                    "SELECT \n" +
                    "    P.value('(Osoba_id/text())[1]', 'NVARCHAR(50)') AS Osoba_id,\n" +
                    "    P.value('(Imie/text())[1]', 'NVARCHAR(50)') AS Imie,\n" +
                    "    P.value('(Nazwisko/text())[1]', 'NVARCHAR(50)') AS Nazwisko,\n" +
                    "    P.value('(Plec/text())[1]', 'NVARCHAR(20)') AS Plec,\n" +
                    "    P.value('(Ojciec_id/text())[1]', 'NVARCHAR(50)') AS Ojciec_id,\n" +
                    "    P.value('(Matka_id/text())[1]', 'NVARCHAR(50)') AS Matka_id,\n" +
                    "    P.value('(Urodziny/text())[1]', 'NVARCHAR(50)') AS Urodziny,\n" +
                    "    P.value('(Smierc/text())[1]', 'NVARCHAR(50)') AS Smierc\n" +
                    "FROM DrzewoGenealogiczne\n" +
                    "CROSS APPLY XDocumentDrzewoGenealogiczne.nodes('Osoba') T(P)\n" +
                    "JOIN ParentIDs PI ON T.P.value('(Osoba_id/text())[1]', 'NVARCHAR(50)') = PI.Ojciec_id\n" +
                    "UNION ALL\n" +
                    "SELECT \n" +
                    "    P.value('(Osoba_id/text())[1]', 'NVARCHAR(50)') AS Osoba_id,\n" +
                    "    P.value('(Imie/text())[1]', 'NVARCHAR(50)') AS Imie,\n" +
                    "    P.value('(Nazwisko/text())[1]', 'NVARCHAR(50)') AS Nazwisko,\n" +
                    "    P.value('(Plec/text())[1]', 'NVARCHAR(20)') AS Plec,\n" +
                    "    P.value('(Ojciec_id/text())[1]', 'NVARCHAR(50)') AS Ojciec_id,\n" +
                    "    P.value('(Matka_id/text())[1]', 'NVARCHAR(50)') AS Matka_id,\n" +
                    "    P.value('(Urodziny/text())[1]', 'NVARCHAR(50)') AS Urodziny,\n" +
                    "    P.value('(Smierc/text())[1]', 'NVARCHAR(50)') AS Smierc\n" +
                    "FROM DrzewoGenealogiczne\n" +
                    "CROSS APPLY XDocumentDrzewoGenealogiczne.nodes('Osoba') T(P)\n" +
                    "JOIN ParentIDs PI ON T.P.value('(Osoba_id/text())[1]', 'NVARCHAR(50)') = PI.Matka_id;");
        }
        return sql.executeQuery();
    }

    /**
     * Metoda dodająca osobę do drzewa genealogicznego.
     * @param connection Połączenie z bazą danych.
     * @param tree_id Drzewo genealogiczne do którego dodajemy Osobę.
     * @param person_id ID osoby którą dodajemy.
     * @param name Imię dodawanej osoby.
     * @param surname Nazwisko dodawanej osoby.
     * @param gender Płeć dodawanej osoby.
     * @param dads_id ID ojca dodawanej osoby.
     * @param moms_id ID matki dodawanej osoby.
     * @param birth Data urodzenia dodawanej osoby.
     * @param death Data śmierci dodawanej osoby.
     * @return true/false w zależności czy query zadziałało.
     */
    public static boolean addNode(Connection connection, Integer tree_id, String person_id, String name, String surname, String gender, String dads_id, String moms_id, String birth, String death) throws SQLException {
        if(dads_id.equals("0")){
            dads_id = " null ";
        }
        if(moms_id.equals("0")){
            moms_id = " null ";
        }
        if(death.isEmpty()){
            death = " null ";
        }
        String xml_to_insert = "<Osoba>\n" +
                "<Osoba_id>" + person_id + "</Osoba_id>\n" +
                "<Imie>" + name + "</Imie>\n" +
                "<Nazwisko>" + surname + "</Nazwisko>\n" +
                "<Plec>" + gender + "</Plec>\n" +
                "<Ojciec_id>" + dads_id + "</Ojciec_id>\n" +
                "<Matka_id>" + moms_id + "</Matka_id>\n" +
                "<Urodziny>" + birth + "</Urodziny>\n" +
                "<Smierc>" + death + "</Smierc>\n" +
                "</Osoba>";
        PreparedStatement sql = connection.prepareStatement("UPDATE DrzewoGenealogiczne\n" +
                "SET XDocumentDrzewoGenealogiczne.modify('\n" +
                "    insert " + xml_to_insert + " as last into (/)[1]\n" +
                "')\n" +
                "WHERE DrzewoGenealogiczneID = " + tree_id + ";");
        sql.executeUpdate();

        PreparedStatement sql_check = connection.prepareStatement("SELECT Tbl.Col.value('(Osoba_id)[1]', 'varchar(20)') AS Osoba_id\n" +
                "FROM\n" +
                "DrzewoGenealogiczne\n" +
                "CROSS APPLY \n" +
                "XDocumentDrzewoGenealogiczne.nodes('Osoba') AS Tbl(Col) \n" +
                "WHERE \n" +
                " DrzewoGenealogiczneID = " + tree_id + ";");
        List<Integer> check = new ArrayList<>();
        ResultSet result_check = sql_check.executeQuery();
        while(result_check.next()){
            check.add(result_check.getInt("Osoba_id"));
        }

        return check.contains(Integer.valueOf(person_id));
    }

    /**
     * Metoda usuwająca osobę z drzewa genealogicznego.
     * @param connection Połączenie z bazą danych.
     * @param tree_id Drzewo genalogiczne z którego usuwamy osobę.
     * @param node_id ID osoby którą usuwamy.
     * @return true/false w zależności czy query zadziałało.
     */
    public static boolean deleteNode(Connection connection, Integer tree_id, Integer node_id) throws SQLException {
        PreparedStatement sql = connection.prepareStatement("UPDATE DrzewoGenealogiczne\n" +
                "SET XDocumentDrzewoGenealogiczne.modify('delete Osoba[Osoba_id=" + node_id + "]')\n" +
                "WHERE DrzewoGenealogiczneID = " + tree_id + ";");
        sql.executeUpdate();

        PreparedStatement delete_mother = connection.prepareStatement("UPDATE DrzewoGenealogiczne\n" +
                "SET XDocumentDrzewoGenealogiczne.modify('\n" +
                "    replace value of (Osoba[Matka_id="+ node_id+ "]/Matka_id/text())[1]\n" +
                "    with \" null \"\n" +
                "')\n" +
                "WHERE DrzewoGenealogiczneID = " + tree_id + ";");

        PreparedStatement delete_father = connection.prepareStatement("UPDATE DrzewoGenealogiczne\n" +
                "SET XDocumentDrzewoGenealogiczne.modify('\n" +
                "    replace value of (Osoba[Ojciec_id="+ node_id + "]/Ojciec_id/text())[1]\n" +
                "    with \" null \"\n" +
                "')\n" +
                "WHERE DrzewoGenealogiczneID = "+ tree_id +"; ");

        delete_mother.executeUpdate();
        delete_mother.executeUpdate();
        delete_father.executeUpdate();
        delete_father.executeUpdate();

        PreparedStatement sql_check1 = connection.prepareStatement("SELECT Tbl.Col.value('(Osoba_id)[1]', 'varchar(20)') AS Osoba_id\n" +
                "FROM \n" +
                "    DrzewoGenealogiczne\n" +
                "CROSS APPLY \n" +
                "    XDocumentDrzewoGenealogiczne.nodes('Osoba') AS Tbl(Col)\n" +
                "WHERE \n" +
                "    DrzewoGenealogiczneID = " + tree_id + ";");
        List<Integer> check = new ArrayList<>();
        ResultSet result_check = sql_check1.executeQuery();
        while(result_check.next()){
            check.add(result_check.getInt("Osoba_id"));
        }

        return !check.contains(node_id);
    }
}
