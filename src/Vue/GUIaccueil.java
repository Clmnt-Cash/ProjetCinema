package Vue;

import Modele.Client;

import javax.swing.*;
import java.awt.*;

public class GUIaccueil extends JFrame {

    private Client client;
    public GUIaccueil(Client client) {
        super("Cinéma");
        this.client = client;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(1500, 800);

        //Obtenir les dimensions de l'écran
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);

        JPanel drawComponents = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Color c1 = new Color(0, 0, 0);
                g.setColor(c1);
                g.fillRect(0, 100, 1500, 700);
                Color c2 = new Color(50, 50, 50, 200);
                g.setColor(c2);
                g.fillRect(0, 0, 1500, 100);
                ImageIcon logo = new ImageIcon("images/logos/logo_black.png");
                Image image = logo.getImage();
                g.drawImage(image, 0, 0, 100, 100, this);
            }
        };

        JLabel labelNom = new JLabel("Connecté en tant que " + client.getPrenom() + " " + client.getNom());
        labelNom.setBounds(1100, 30, 300, 30);
        labelNom.setForeground(Color.WHITE);
        labelNom.setHorizontalAlignment(SwingConstants.RIGHT);
        drawComponents.add(labelNom);
        drawComponents.setLayout(null);

// Créer un JPanel pour contenir le contenu
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new GridLayout(1, 30)); // Utilisation d'un layout pour ajouter du contenu

        // Ajouter du contenu à votre JPanel (par exemple, des JLabels)
        for (int i = 0; i < 30; i++) {
            JLabel label = new JLabel("Élément " + (i + 1));
            contentPane.add(label);
        }

        // Créer un JScrollPane et y ajouter le JPanel contenant le contenu
        JScrollPane scrollPane = new JScrollPane(contentPane);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS); // Activer la barre de défilement verticale

        // Ajouter le JScrollPane au JFrame

        add(drawComponents);
        add(scrollPane);

        setVisible(false);
    }

}
