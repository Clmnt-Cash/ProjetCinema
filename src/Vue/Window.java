package Vue;

import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Toolkit;

public class Window extends JFrame {

    public Window() {
        // Titre de la fenêtre
        super("Cinéma");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(1500, 800);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);

        setVisible(true);
    }

    public static void main(String[] args) {
        Window fenetre = new Window();
    }
}
