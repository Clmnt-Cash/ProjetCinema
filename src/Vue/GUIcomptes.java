package Vue;

import Controleur.ControleurComptes;
import Modele.Client;
import Modele.Seance;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GUIcomptes extends JFrame {
    //Attributs
    private Client employe;
    private ControleurComptes controleurComptes;
    private JButton btnFilms;
    private JButton btnComptes;
    private JButton btnReduc;
    private JButton btnStat;
    private JPanel panel;
    private JLabel boutonDeconnexion;
    private ArrayList<Client> clients;
    private ArrayList<JButton> boutonsModifier;
    private ArrayList<JButton> boutonsSupprimer;

    //Constructeur
    public GUIcomptes(Client employe, ControleurComptes controleurComptes) {
        super("Cinéma");
        this.employe = employe;
        this.controleurComptes = controleurComptes;
        this.clients = controleurComptes.getComptes();
        this.boutonsModifier = new ArrayList<>();
        this.boutonsSupprimer = new ArrayList<>();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(1500, 800);

        //Obtenir les dimensions de l'écran
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);

        this.afficherMenu();

        setVisible(true);
    }

    public void afficherMenu(){
        panel = new JPanel() {
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

        //Onglet bouton films
        btnFilms = new JButton("Films");
        btnFilms.setBounds(100, 60, 100, 30);
        btnFilms.setForeground(Color.WHITE);
        btnFilms.setBackground(new Color(100, 100, 100));
        btnFilms.setOpaque(false);
        btnFilms.setContentAreaFilled(false);
        btnFilms.setBorderPainted(false);
        panel.add(btnFilms);

        //Onglet bouton comptes
        btnComptes = new JButton("Comptes");
        btnComptes.setBounds(200, 60, 100, 30);
        btnComptes.setForeground(Color.BLACK);
        btnComptes.setOpaque(false);
        btnComptes.setContentAreaFilled(false);
        btnComptes.setBorderPainted(false);
        panel.add(btnComptes);

        //Onglet bouton reductions
        btnReduc = new JButton("Reductions");
        btnReduc.setBounds(300, 60, 100, 30);
        btnReduc.setForeground(Color.WHITE);
        btnReduc.setOpaque(false);
        btnReduc.setContentAreaFilled(false);
        btnReduc.setBorderPainted(false);
        panel.add(btnReduc);

        //Onglet bouton statistiques
        btnStat = new JButton("Statistiques");
        btnStat.setBounds(400, 60, 100, 30);
        btnStat.setForeground(Color.WHITE);
        btnStat.setOpaque(false);
        btnStat.setContentAreaFilled(false);
        btnStat.setBorderPainted(false);
        panel.add(btnStat);

        //Label pour le nom de la personne connectée
        JLabel labelNom = new JLabel("Connecté en tant que " + employe.getPrenom() + " " + employe.getNom() + " (employé)");
        labelNom.setFont(labelNom.getFont().deriveFont(Font.BOLD, 15));
        Dimension size = labelNom.getPreferredSize();
        labelNom.setBounds(1470 - size.width, 10, size.width, size.height);
        labelNom.setForeground(Color.WHITE);
        panel.add(labelNom);

        //Bouton déconnexion
        boutonDeconnexion = new JLabel("Déconnexion");
        boutonDeconnexion.setFont(boutonDeconnexion.getFont().deriveFont(Font.BOLD, 12));
        boutonDeconnexion.setBounds(1400, 50, 100, 20);
        boutonDeconnexion.setForeground(Color.WHITE);
        panel.add(boutonDeconnexion);
        int i = 0;
        for (Client c : clients) {
            String type = "";
            if(c.getType() == 1) type = "enfant";
            else if(c.getType() == 2) type = "régulier";
            else type = "sénior";


            JLabel labelClient = new JLabel("   Type : " + type + "    E-mail : " + c.getEmail() + "    Mot de passe : " + c.getMotDePasse());
            labelClient.setBounds(200, 200 + i, 500, 30);
            Dimension size2 = labelClient.getPreferredSize();
            labelClient.setBounds(750 - size2.width, 200 + i, size2.width, size2.height);
            labelClient.setForeground(Color.WHITE);
            panel.add(labelClient);

            // Création du label pour le nom et le prénom en gras
            JLabel labelNomPrenom = new JLabel(c.getNom() + " " + c.getPrenom());
            labelNomPrenom.setFont(new Font("Arial", Font.BOLD, 15));
            Dimension size3 = labelNomPrenom.getPreferredSize();
            labelNomPrenom.setBounds(730 - size2.width - size3.width, 195 + i, size3.width, 30);
            labelNomPrenom.setForeground(Color.WHITE);
            panel.add(labelNomPrenom);


            JButton boutonModifier = new JButton("Modifier");
            boutonModifier.setFont(boutonModifier.getFont().deriveFont(Font.BOLD, 13));
            boutonModifier.setForeground(new Color(163, 163, 163));
            boutonModifier.putClientProperty("infos", c.getId() + "," + c.getNom() + "," + c.getPrenom() + "," + type + "," + c.getEmail() + ',' + c.getMotDePasse());
            boutonModifier.setBounds(760, 200 + i, 100, 25);
            panel.add(boutonModifier);
            this.boutonsModifier.add(boutonModifier);

            JButton boutonSupprimer = new JButton("Supprimer");
            boutonSupprimer.setFont(boutonSupprimer.getFont().deriveFont(Font.BOLD, 13));
            boutonSupprimer.setForeground(Color.WHITE);
            boutonSupprimer.setBackground(new Color(174, 27, 27));
            boutonSupprimer.putClientProperty("infos", c.getId() + "," + c.getNom() + "," + c.getPrenom() + "," + type + "," + c.getEmail() + ',' + c.getMotDePasse());
            boutonSupprimer.setBounds(870, 200 + i, 100, 25);
            panel.add(boutonSupprimer);
            this.boutonsSupprimer.add(boutonSupprimer);

            i+=50;
        }


        panel.setLayout(null);
        add(panel);
        setVisible(true);
    }
    public void addListenerOngletFilms(ActionListener listener){
        btnFilms.addActionListener(listener);
    }
    public void addListenerOngletReduc(ActionListener listener){
        btnReduc.addActionListener(listener);
    }
    public void addMouseListenerBoutonDeconnexion(MouseListener listener) {
        boutonDeconnexion.addMouseListener(listener);
    }
    public void addListenersModifier(ActionListener l){
        for(JButton b : boutonsModifier){
            b.addActionListener(l);
        }
    }
    public void addListenersSupprimer(ActionListener listener){
        for(JButton b : this.boutonsSupprimer){
            b.addActionListener(listener);
        }
    }
    public void addListenerOngletStat(ActionListener listener){
        btnStat.addActionListener(listener);
    }
    public void closeWindow(){setVisible(false);dispose();}
}