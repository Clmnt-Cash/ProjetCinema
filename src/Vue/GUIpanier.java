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
    private ArrayList<JButton> boutonsSupprimer;

    private ControleurPanier controleurPanier;
    private JLabel boutonDeconnexion;
    private JButton boutonPanier;
    private JButton boutonPayer;
    private float prix;
    private Reduction reduc;


    //Constructeur
    public GUIpanier(Client client, ControleurPanier controleurPanier) {
        super("Cinéma");
        this.client = client;
        this.commandes = controleurPanier.getCommandes();
        this.boutonsModifier = new ArrayList<JButton>();
        this.boutonsSupprimer = new ArrayList<JButton>();
        this.reduc = controleurPanier.getReduction();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(1250, 700);

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
                g.fillRect(0, 100, 1100, 700);
                Color c2 = new Color(50, 50, 50, 200);
                g.setColor(c2);
                g.fillRect(0, 0, 1100, 100);
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

        JLabel labelAccueil = new JLabel("Panier");
        labelAccueil.setFont(labelAccueil.getFont().deriveFont(Font.BOLD, 30));
        labelAccueil.setBounds(720, 20, 150, 40);
        labelAccueil.setForeground(Color.WHITE);
        panel.add(labelAccueil);

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

        //Ajout du bouton Retour
        boutonRetour = new JButton("Retour");
        boutonRetour.setBounds(10, 110, 100, 50);
        boutonRetour.setForeground(Color.WHITE);
        boutonRetour.setBackground(Color.BLACK);
        panel.add(boutonRetour);

        JLabel labelCommandes = new JLabel("Commandes dans le panier");
        labelCommandes.setFont(labelNom.getFont().deriveFont(Font.BOLD, 20));
        labelCommandes.setBounds(300, 120, 300, 40);
        labelCommandes.setForeground(Color.WHITE);
        panel.add(labelCommandes);

        JLabel labelCommandesPassees = new JLabel("Commandes passées");
        labelCommandesPassees.setFont(labelNom.getFont().deriveFont(Font.BOLD, 20));
        labelCommandesPassees.setBounds(1050, 120, 250, 40);
        labelCommandesPassees.setForeground(Color.WHITE);
        panel.add(labelCommandesPassees);

        float prixTotAvecReduction = 0;
        float prixTotSansReduction = 0;

        int xLabelNonPaye = 0;
        int xLabelPaye = 0;
        for(Commande c : commandes){
            if(!c.getPaye()) {
                JLabel labelInfos = new JLabel("Séance pour " + c.getSeance().getFilm() + " le " + c.getSeance().getDate() + " à " + c.getSeance().getHeure() + " pour " + c.getNbPlaces() + " personnes");
                JLabel labelPrix;
                if (client.getType() == -1) {
                    labelPrix = new JLabel("Prix : " + c.getPrixSansReduc() + "€");
                } else {
                    labelPrix = new JLabel("<html>Prix : " + "<strike>" + c.getPrixSansReduc() + "€</strike> " + c.getPrixAvecReduc() + "€ (-" + "</html>");
                }

                labelPrix.setFont(labelNom.getFont().deriveFont(Font.BOLD, 15));
                labelPrix.setBounds(50, 180 + 70 * xLabelNonPaye, 300, 50);
                labelPrix.setForeground(Color.WHITE);

                labelInfos.setFont(labelNom.getFont().deriveFont(Font.BOLD, 15));
                labelInfos.setBounds(50, 160 + 70 * xLabelNonPaye, 550, 50);
                labelInfos.setForeground(Color.WHITE);

                JButton boutonModifier = new JButton("Modifier");
                boutonModifier.setFont(labelNom.getFont().deriveFont(Font.BOLD, 13));
                boutonModifier.setBounds(610, 175 + 70 * xLabelNonPaye, 100, 25);
                boutonModifier.setForeground(new Color(163, 163, 163));
                boutonModifier.putClientProperty("infos", c.getId() + "," + c.getSeance().getFilm() + "," + c.getNbPlaces());


                JButton boutonSupprimer = new JButton("Supprimer");
                boutonSupprimer.setFont(labelNom.getFont().deriveFont(Font.BOLD, 13));
                boutonSupprimer.setBounds(720, 175 + 70 * xLabelNonPaye, 100, 25);
                boutonSupprimer.setForeground(Color.WHITE);
                boutonSupprimer.setBackground(new Color(174, 27, 27));
                boutonSupprimer.putClientProperty("infos", c.getId() + "," + c.getSeance().getFilm());


                panel.add(labelInfos);
                panel.add(labelPrix);
                panel.add(boutonModifier);
                panel.add(boutonSupprimer);
                this.boutonsModifier.add(boutonModifier);
                this.boutonsSupprimer.add(boutonSupprimer);

                xLabelNonPaye++;

                prixTotSansReduction += c.getPrixSansReduc();
                prixTotAvecReduction += c.getPrixAvecReduc();
            } else {
                JLabel labelInfos = new JLabel("Séance pour " + c.getSeance().getFilm() + " le " + c.getSeance().getDate() + " à " + c.getSeance().getHeure() + " pour " + c.getNbPlaces() + " personnes");
                labelInfos.setFont(labelNom.getFont().deriveFont(Font.BOLD, 15));
                labelInfos.setBounds(900, 180 + 40 * xLabelPaye, 550, 30);
                labelInfos.setForeground(Color.GRAY);
                panel.add(labelInfos);
                xLabelPaye ++;
            }
        }
        //Si il y a des commandes, afficher le bouton pour payer
        if(!this.commandes.isEmpty() && prixTotSansReduction != 0) {
            this.prix = prixTotAvecReduction;
            if(client.getType() == 1) boutonPayer = new JButton("<html>Payer : " + "<strike>" + prixTotSansReduction + "€</strike> " + prixTotAvecReduction + "€ (-"+ reduc.getReductionEnfant() + "%)</html>");
            else if (client.getType() == 2)boutonPayer = new JButton("<html>Payer : " + "<strike>" + prixTotSansReduction + "€</strike> " + prixTotAvecReduction + "€ (-"+ reduc.getReductionRegulier() + "%)</html>");
            else boutonPayer = new JButton("<html>Payer : " + "<strike>" + prixTotSansReduction + "€</strike> " + prixTotAvecReduction + "€ (-"+ reduc.getReductionSenior() + "%)</html>");

            boutonPayer.setFont(labelNom.getFont().deriveFont(Font.BOLD, 15));
            boutonPayer.setBounds(1200, 640, 200, 30);
            boutonPayer.setForeground(Color.WHITE);
            boutonPayer.setBackground(new Color(100, 100, 100));

            panel.add(boutonPayer);
        } else { // afficher un label si il n y a aucune commande
            JLabel labelRien = new JLabel("Aucune commande actuellement.");
            labelRien.setFont(labelNom.getFont().deriveFont(Font.BOLD, 15));
            labelRien.setBounds(300, 200, 300, 30);
            labelRien.setForeground(Color.GRAY);
            panel.add(labelRien);
        }
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

    public void addListenerSupprimer(ActionListener listener){
        for(JButton b : this.boutonsSupprimer){
            b.addActionListener(listener);
        }
    }

    public void addListenerPayer(ActionListener listener){if(!this.commandes.isEmpty() && this.prix != 0)boutonPayer.addActionListener(listener);}
    public float getPrix(){return prix;}
}
