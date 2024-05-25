import Controller.MainController;

import javax.swing.*;
import java.io.IOException;

/**
 * Metoda main wywołująca program
 */
public class Main {
    public static void main(String[] args) throws IOException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        MainController.getInstance();
    }
}
