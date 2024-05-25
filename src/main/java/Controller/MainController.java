package Controller;

import java.io.IOException;
import java.util.Objects;

/**
 * Klasa odpowiadająca za połączenie GUI z backendem.
 */
public class MainController {

    private static MainController mainController;

    /**
     * Kontruktor klasy kontrolującej.
     */
    public MainController() throws IOException{
        control();
    }

    /**
     * Metoda zwracająca instancję klasy jeżeli takowa istnieje, jeżeli nie - tworzy ją.
     * @return instancja klasy.
     */
    public static MainController getInstance() throws IOException{
        if(Objects.isNull(mainController)) {
            return new MainController();
        }
        return mainController;
    }

    /**
     * Metoda inicjalizująca.
     */
    public void control() throws IOException {
        init();
    }

    /**
     * Metoda inicalizująca - wywołanie kontrolera GUI.
     */
    private static void init() throws IOException {
        MainGUIController.getInstance();
    }
}
