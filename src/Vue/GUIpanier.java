package Vue;

import Controleur.ControleurAccueil;
import Controleur.ControleurFilm;
import Controleur.ControleurPanier;
import Modele.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.*;

public class GUIpanier extends JFrame {
    //Attributs
    private Client client;
    private JButton boutonRetour;
    private ArrayList<Commande> commandes;
    private ArrayList<JButton> boutonsModifier;
    private ControleurPanier controleurPanier;
    private JLabel boutonDeconnexion;
    private JButton boutonPanier;
    private JButton boutonPayer;


    //Constructeur
    public GUIpanier(Client client, ControleurPanier controleurPanier) {
        super("Cinéma");
        this.client = client;
        this.commandes = controleurPanier.getCommandes();
        this.boutonsModifier = new ArrayList<JButton>();
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

        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("Button.background", Color.WHITE);
        UIManager.put("Button.foreground", Color.BLACK);
        UIManager.put("Button.border", BorderFactory.createLineBorder(Color.WHITE));
        UIManager.put("Button.focus", Color.WHITE);

        //Bouton déconnexion
        boutonDeconnexion = new JLabel("Déconnexion");
        boutonDeconnexion.setFont(boutonDeconnexion.getFont().deriveFont(Font.BOLD, 12));
        boutonDeconnexion.setBounds(1400, 50, 100, 20);
        boutonDeconnexion.setForeground(Color.WHITE);
        panel.add(boutonDeconnexion);

        //Ajout du JLabel pour afficher le nom du client
        JLabel labelNom = new JLabel("Connecté en tant que " + client.getPrenom() + " " + client.getNom());
        labelNom.setFont(labelNom.getFont().deriveFont(Font.BOLD, 15));
        Dimension size = labelNom.getPreferredSize();
        labelNom.setBounds(1470 - size.width, 10, size.width, size.height);
        labelNom.setForeground(Color.WHITE);
        panel.add(labelNom);
        panel.setLayout(null);
        //Ajout du bouton Retour
        boutonRetour = new JButton("Retour");
        boutonRetour.setBounds(10, 110, 100, 50);
        boutonRetour.setForeground(Color.WHITE);
        boutonRetour.setBackground(Color.BLACK);
        panel.add(boutonRetour);

        float prixTotAvecReduction = 0;
        float prixTotSansReduction = 0;

        int xLabel = 0;
        for(Commande c : commandes){
            JLabel labelInfos = new JLabel("Séance pour "+c.getSeance().getFilm() + " le " + c.getSeance().getDate() + " à " + c.getSeance().getHeure() + " pour " + c.getNbPlaces() +" personnes");
            JLabel labelPrix;
            if(client.getType() == -1){labelPrix = new JLabel("Prix : " + c.getPrixSansReduc() + "€");}
            else{labelPrix = new JLabel("<html>Prix : " + "<strike>" + c.getPrixSansReduc() + "€</strike> "+ c.getPrixAvecReduc() + "€</html>");}

            labelPrix.setFont(labelNom.getFont().deriveFont(Font.BOLD, 15));
            labelPrix.setBounds(200, 140 + 70 * xLabel, 300, 50);
            labelPrix.setForeground(Color.WHITE);

            labelInfos.setFont(labelNom.getFont().deriveFont(Font.BOLD, 15));
            labelInfos.setBounds(200, 120 + 70 * xLabel, 550, 50);
            labelInfos.setForeground(Color.WHITE);

            JButton boutonModifier = new JButton("Modifier");
            boutonModifier.setFont(labelNom.getFont().deriveFont(Font.BOLD, 13));
            boutonModifier.setBounds(760, 135 + 70 * xLabel, 100, 25);
            boutonModifier.setForeground(new Color(163, 163, 163));
            boutonModifier.putClientProperty("infos", c.getId() + "," + c.getSeance().getFilm() + "," + c.getNbPlaces());


            JButton boutonSupprimer = new JButton("Supprimer");
            boutonSupprimer.setFont(labelNom.getFont().deriveFont(Font.BOLD, 13));
            boutonSupprimer.setBounds(870, 135 + 70 * xLabel, 100, 25);
            boutonSupprimer.setForeground(Color.WHITE);
            boutonSupprimer.setBackground(new Color(174, 27, 27));


            panel.add(labelInfos);
            panel.add(labelPrix);
            panel.add(boutonModifier);
            panel.add(boutonSupprimer);

            this.boutonsModifier.add(boutonModifier);

            xLabel ++;

            prixTotSansReduction += c.getPrixSansReduc();
            prixTotAvecReduction += c.getPrixAvecReduc();
        }

        boutonPayer = new JButton("<html>Payer : " + "<strike>" + prixTotSansReduction + "€</strike> "+ prixTotAvecReduction + "€</html>");
        boutonPayer.setFont(labelNom.getFont().deriveFont(Font.BOLD, 15));
        boutonPayer.setBounds(1200, 640, 200, 30);
        boutonPayer.setForeground(Color.WHITE);
        boutonPayer.setBackground(new Color(100, 100, 100));

        panel.add(boutonPayer);
        setVisible(true);
        panel.setLayout(null);
        add(panel);

    }

    public void addListenerRetour(ActionListener listener){
        boutonRetour.addActionListener(listener);
    }

    public void closeWindow(){setVisible(false);dispose();}
    public void setCommandes(ArrayList<Commande> commandes){this.commandes = commandes;}

    public void addMouseListenerBoutonDeconnexion(MouseListener listener) {
        boutonDeconnexion.addMouseListener(listener);
    }

    public void addListenerModifier(ActionListener listener){
        for(JButton b : this.boutonsModifier){
            b.addActionListener(listener);
        }
    }
}
