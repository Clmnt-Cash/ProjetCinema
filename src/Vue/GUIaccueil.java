package Vue;

import Controleur.ControleurAccueil;
import Modele.Client;
import Modele.Film;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class GUIaccueil extends JFrame {
    private ArrayList<Film> films;
    private Client client;
    private ControleurAccueil controleurAccueil;

    public GUIaccueil(Client client, ControleurAccueil controleurAccueil) {
        super("Cinéma");
        this.client = client;
        this.controleurAccueil = controleurAccueil;
        this.films = controleurAccueil.getFilms();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(1500, 800);

        //Obtenir les dimensions de l'écran
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);

        //Création du JPanel pour drawComponents
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

        // Ajout du JLabel pour afficher le nom du client
        JLabel labelNom = new JLabel("Connecté en tant que " + client.getPrenom() + " " + client.getNom());
        labelNom.setBounds(1100, 30, 300, 30);
        labelNom.setForeground(Color.WHITE);
        labelNom.setHorizontalAlignment(SwingConstants.RIGHT);
        drawComponents.add(labelNom);
        drawComponents.setLayout(null);

        JPanel scrollablePanel = new JPanel();
        scrollablePanel.setLayout(new GridLayout(0, films.size()));

        for (Film f : films) {
            JButton button = new JButton();

            ImageIcon imageIcon = new ImageIcon("images/affiches/" + f.getCheminImage());

            if (imageIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                Image image = imageIcon.getImage().getScaledInstance(500, 700, Image.SCALE_SMOOTH);
                ImageIcon resizedIcon = new ImageIcon(image);
                button.setIcon(resizedIcon);
            } else {
                button.setText("Image indisponible");
            }
            button.setPreferredSize(new Dimension(500, 200));

            JPanel overlayPanel = new JPanel();
            button.setBorderPainted(false);
            overlayPanel.setBackground(new Color(0, 0, 0, 100));

            overlayPanel.setVisible(false);
            button.setLayout(new BorderLayout());
            button.add(overlayPanel, BorderLayout.CENTER);

            JLabel labelTitre = new JLabel();
            labelTitre.setForeground(Color.WHITE);
            labelTitre.setHorizontalAlignment(SwingConstants.LEADING);
            labelTitre.setVerticalAlignment(SwingConstants.CENTER);
            overlayPanel.add(labelTitre);

            Font police = new Font("Arial", Font.BOLD, 25);

            labelTitre.setFont(police);


            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    overlayPanel.setVisible(true);
                    labelTitre.setText(f.getTitre());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    overlayPanel.setVisible(false);
                    labelTitre.setText("");
                }
            });
            scrollablePanel.add(button);
        }



        JScrollPane scrollPane = new JScrollPane(scrollablePanel);
        scrollPane.setBounds(10, 110, 1463, 670);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);


        drawComponents.add(scrollPane);
        add(drawComponents);

        setVisible(true);

    }
}
