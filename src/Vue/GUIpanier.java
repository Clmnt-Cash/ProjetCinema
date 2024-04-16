package Vue;

import Controleur.ControleurAccueil;
import Controleur.ControleurFilm;
import Modele.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class GUIpanier extends JFrame {
    //Attributs
    private Client client;
    private JButton boutonRetour;
    private ArrayList<Commande> commandes;


    //Constructeur
    public GUIpanier(Client client) {
        super("Cinéma");
        this.client = client;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(1500, 800);

        //Obtenir les dimensions de l'écran
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);

        JPanel panel = new JPanel() {
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

        //Ajout du JLabel pour afficher le nom du client
        JLabel labelNom = new JLabel("Connecté en tant que " + client.getPrenom() + " " + client.getNom());
        labelNom.setFont(labelNom.getFont().deriveFont(Font.BOLD, 15));
        Dimension size = labelNom.getPreferredSize();
        labelNom.setBounds(1470 - size.width, 20, size.width, size.height);
        labelNom.setForeground(Color.WHITE);
        panel.add(labelNom);
        panel.setLayout(null);

        //Ajout du bouton Retour
        boutonRetour = new JButton("Retour");
        boutonRetour.setBounds(10, 110, 100, 50);
        boutonRetour.setForeground(Color.WHITE);
        boutonRetour.setBackground(Color.BLACK);
        panel.add(boutonRetour);


        JPanel scrollPanel = new JPanel();
        scrollPanel.setLayout(new GridLayout(0, 3)); // 3 colonnes, le nombre de lignes sera ajusté automatiquement
        JScrollPane scrollPane = new JScrollPane(scrollPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        // Ajout des composants dans les colonnes
        for (int i = 0; i < 10; i++) {
            JLabel label1 = new JLabel("Label 1 - Row " + i);
            JLabel label2 = new JLabel("Label 2 - Row " + i);
            JButton button1 = new JButton("Button 1 - Row " + i);
            JButton button2 = new JButton("Button 2 - Row " + i);

            JPanel column1 = new JPanel(new GridLayout(2, 1)); // 2 lignes pour les labels
            column1.add(label1);
            column1.add(label2);

            JPanel column2 = new JPanel(new GridLayout(1, 1)); // 1 ligne pour le premier bouton
            column2.add(button1);

            JPanel column3 = new JPanel(new GridLayout(1, 1)); // 1 ligne pour le deuxième bouton
            column3.add(button2);

            scrollPanel.add(column1);
            scrollPanel.add(column2);
            scrollPanel.add(column3);
        }
        panel.add(scrollPanel);


        setVisible(true);
        panel.setLayout(null);
        add(panel);

    }

    public void addListenerRetour(ActionListener listener){
        boutonRetour.addActionListener(listener);
    }

    public void closeWindow(){setVisible(false);dispose();}
    public void setCommandes(ArrayList<Commande> commandes){this.commandes = commandes;}
}
