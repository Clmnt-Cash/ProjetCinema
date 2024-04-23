package Vue;

import Controleur.ControleurModifierFilm;
import Modele.Client;
import Modele.Film;
import Modele.Seance;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.*;

public class GUIModifierFilm extends JFrame {
    //Attributs
    private final Client client;
    private final JButton boutonRetour;
    private final Film filmActuel;
    private final ArrayList<JButton> boutonsSeance;

    private JButton boutonModifier;
    private final JButton boutonEnregistrer;
    private ArrayList<Seance> seances;
    private final JButton boutonAjouterSeance;

    private JTextField textFieldTitre;
    private JTextField textFieldChemin;
    private JTextField textFieldRealisateur;
    private JTextArea textAreaSynopsis;
    public ArrayList<JButton> boutonsModifier;
    public ArrayList<JButton> boutonsSupprimer;
    private JButton boutonSupprimer;
    private JLabel boutonDeconnexion;

    public JPanel panel;


    //Constructeur
    public GUIModifierFilm(Client client, ControleurModifierFilm controleurModifierFilm, Film film) {
        super("Cinéma");
        this.client = client;
        this.filmActuel = film;
        this.boutonsSeance = new ArrayList<JButton>();
        this.boutonEnregistrer = new JButton("Enregistrer");
        this.boutonAjouterSeance = new JButton("Ajouter une séance");
        this.boutonsModifier = new ArrayList<>();
        this.boutonsSupprimer = new ArrayList<>();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(1250, 700);

        //Obtenir les dimensions de l'écran
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);

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

        //Label pour le nom de la personne connectée
        JLabel labelNom = new JLabel("Connecté en tant que " + client.getPrenom() + " " + client.getNom() + " (employé)");
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

        //Bouton déconnexion
        boutonDeconnexion = new JLabel("Déconnexion");
        boutonDeconnexion.setFont(boutonDeconnexion.getFont().deriveFont(Font.BOLD, 12));
        boutonDeconnexion.setBounds(1400, 50, 100, 20);
        boutonDeconnexion.setForeground(Color.WHITE);
        panel.add(boutonDeconnexion);

        this.affichage();

        setVisible(true);
        panel.setLayout(null);
        add(panel);
    }

    public void closeWindow(){setVisible(false);dispose();}

    //Affichage de base
    public void affichage(){

        //Affichage du nom du film
        JLabel labelTitre = new JLabel(filmActuel.getTitre());
        labelTitre.setBounds(450, 200, 400, 30);
        labelTitre.setForeground(Color.WHITE);
        Font police = new Font("Arial", Font.BOLD, 20);
        labelTitre.setFont(police);
        panel.add(labelTitre);

        //Affichage de l'image du film
        ImageIcon imageIcon = new ImageIcon("images/affiches/" + filmActuel.getCheminImage());
        Image image = imageIcon.getImage();
        Image newImage = image.getScaledInstance(412, 550, Image.SCALE_SMOOTH);
        ImageIcon newImageIcon = new ImageIcon(newImage);
        JLabel labelImage = new JLabel(newImageIcon);
        labelImage.setBounds(10, 200, 412, 550);
        panel.add(labelImage);

        //Affichage du nom du réalisateur
        JLabel labelRealisateur = new JLabel("Réalisé par " + filmActuel.getRealisateur());
        labelRealisateur.setBounds(450, 230, 400, 30);
        labelRealisateur.setForeground(Color.WHITE);
        panel.add(labelRealisateur);

        //Affichage du synopsis
        JLabel labelSynopsis = new JLabel("<html><u>Synopsis</u><br><br>" + filmActuel.getSynopsis() + "</html>");
        labelSynopsis.setBounds(450, 280, 400, 500);
        labelSynopsis.setForeground(Color.WHITE);
        labelSynopsis.setBackground(Color.BLACK);
        labelSynopsis.setVerticalAlignment(SwingConstants.TOP);
        labelSynopsis.setPreferredSize(new Dimension(400, 500));
        labelSynopsis.setVerticalTextPosition(SwingConstants.TOP);
        labelSynopsis.setHorizontalTextPosition(SwingConstants.LEFT);
        labelSynopsis.setHorizontalAlignment(SwingConstants.LEFT);
        labelSynopsis.setOpaque(true);
        panel.add(labelSynopsis);


        //Bouton pour modifier un film
        boutonModifier = new JButton("Modifier");
        boutonModifier.setBounds(1330, 700, 100, 50);
        boutonModifier.setFont(new Font("Arial", Font.BOLD, 20));
        boutonModifier.setForeground(Color.WHITE);
        boutonModifier.setBackground(Color.GRAY);
        panel.add(boutonModifier);

        //Bouton pour supprimer un film
        boutonSupprimer = new JButton("Supprimer");
        boutonSupprimer.setBounds(1220, 700, 100, 50);
        boutonSupprimer.setFont(new Font("Arial", Font.BOLD, 18));
        boutonSupprimer.setForeground(Color.WHITE);
        boutonSupprimer.setBackground(new Color(174, 27, 27));
        panel.add(boutonSupprimer);

        JLabel labelSeances = new JLabel("Séances");
        labelSeances.setBounds(900, 120, 200, 30);
        labelSeances.setForeground(Color.WHITE);
        labelSeances.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(labelSeances);

        //Ajout des boutons pour chaque séance
        seances = filmActuel.getSeances();

        ArrayList<String> datesUniques = new ArrayList<String>();
        for(Seance s : seances){
            if(!datesUniques.contains(s.getDate()))datesUniques.add(s.getDate());
            JButton boutonModifier2 = new JButton("Modifier");
            boutonModifier2.setFont(boutonModifier2.getFont().deriveFont(Font.BOLD, 13));
            boutonModifier2.setForeground(new Color(163, 163, 163));
            boutonModifier2.putClientProperty("infos", s.getId() + "," + s.getDate() + "," + s.getHeure() + "," + s.getPrix());
            panel.add(boutonModifier2);
            this.boutonsModifier.add(boutonModifier2);

            JButton boutonSupprimer = new JButton("Supprimer");
            boutonSupprimer.setFont(boutonSupprimer.getFont().deriveFont(Font.BOLD, 13));
            boutonSupprimer.setForeground(Color.WHITE);
            boutonSupprimer.setBackground(new Color(174, 27, 27));
            boutonSupprimer.putClientProperty("infos", s.getId() + "," + s.getDate() + "," + s.getHeure() + "," + s.getPrix());
            panel.add(boutonSupprimer);
            this.boutonsSupprimer.add(boutonSupprimer);
        }
        int yBouton = 0;
        for(String date : datesUniques){
            String mois = date.substring(3, 5);
            String jour = date.substring(0, 2);
            if(mois.equals("01"))mois = "janvier";
            else if(mois.equals("02"))mois = "février";
            else if(mois.equals("03"))mois = "mars";
            else if(mois.equals("04"))mois = "avril";
            else if(mois.equals("05"))mois = "mai";
            else if(mois.equals("06"))mois = "juin";
            else if(mois.equals("07"))mois = "juillet";
            else if(mois.equals("08"))mois = "août";
            else if(mois.equals("09"))mois = "septembre";
            else if(mois.equals("10"))mois = "octobre";
            else if(mois.equals("11"))mois = "novembre";
            else if(mois.equals("12"))mois = "décembre";

            //Afficher toutes les séances disponibles
            JLabel labelDate = new JLabel(jour + " " + mois);
            labelDate.setBounds(900, 160 + yBouton * 80, 200, 20);
            labelDate.setForeground(Color.WHITE);
            panel.add(labelDate);
            yBouton ++;
            int xBouton = 0;
            for (Seance s : seances) {
                if(Objects.equals(s.getDate(), date)) {
                    JButton bouton = new JButton();
                    bouton.setLayout(new GridBagLayout());

                    //Heure
                    JLabel labelHeure = new JLabel(s.getHeure());
                    labelHeure.setFont(labelDate.getFont().deriveFont(Font.BOLD, 20));
                    GridBagConstraints gbcHeure = new GridBagConstraints();
                    gbcHeure.gridx = 1;
                    gbcHeure.gridy = 0;
                    gbcHeure.anchor = GridBagConstraints.CENTER;
                    bouton.add(labelHeure, gbcHeure);

                    //Prix
                    float prix = s.getPrix();
                    JLabel labelPrix = new JLabel(prix + "€");
                    labelPrix.setFont(labelPrix.getFont().deriveFont(Font.PLAIN, 10));
                    GridBagConstraints gbcPrix = new GridBagConstraints();
                    gbcPrix.gridx = 2;
                    gbcPrix.gridy = 2;
                    gbcPrix.anchor = GridBagConstraints.SOUTHEAST;
                    bouton.add(labelPrix, gbcPrix);

                    //Calcul de la position du bouton
                    bouton.setBounds(900 + xBouton * 110, 110 + yBouton * 80, 100, 40);
                    bouton.setForeground(Color.BLACK);
                    bouton.setBackground(Color.WHITE);

                    bouton.putClientProperty("seance", s.getId() + "," + date);
                    panel.add(bouton);

                    boutonsSeance.add(bouton);
                    xBouton++;
                }
            }
        }
    }
    //Affichage quand on veut modifier les données d'un film
    public void affichageModifier(){
        panel.removeAll();
        panel.revalidate();
        panel.repaint();


        if(this.client.getType() != -1) {
            //Ajout du JLabel pour afficher le nom du client
            JLabel labelNom = new JLabel("Connecté en tant que " + client.getPrenom() + " " + client.getNom());
            labelNom.setFont(labelNom.getFont().deriveFont(Font.BOLD, 15));
            Dimension size2 = labelNom.getPreferredSize();
            labelNom.setBounds(1470 - size2.width, 10, size2.width, size2.height);
            labelNom.setForeground(Color.WHITE);
            panel.add(labelNom);
            panel.setLayout(null);
        } else {
            //Ajout du JLabel pour afficher invité
            JLabel labelNom = new JLabel("Connecté en tant qu'invité ");
            labelNom.setFont(labelNom.getFont().deriveFont(Font.BOLD, 15));
            Dimension size = labelNom.getPreferredSize();
            labelNom.setBounds(1470 - size.width, 10, size.width, size.height);
            labelNom.setForeground(Color.WHITE);
            panel.add(labelNom);
            panel.setLayout(null);
        }

        //Label pour le nom du film
        JLabel labelTitre = new JLabel("Titre:");
        labelTitre.setBounds(150, 200, 100, 30);
        labelTitre.setForeground(Color.WHITE);
        panel.add(labelTitre);

        //Champ de texte pour le nom du film
        textFieldTitre = new JTextField(filmActuel.getTitre());
        textFieldTitre.setBounds(300, 200, 400, 30);
        textFieldTitre.setForeground(Color.WHITE);
        textFieldTitre.setBackground(Color.BLACK);
        panel.add(textFieldTitre);

        //Label pour le chemin de l'image
        JLabel labelChemin = new JLabel("Chemin de l'image:");
        labelChemin.setBounds(150, 230, 130, 30);
        labelChemin.setForeground(Color.WHITE);
        panel.add(labelChemin);

        //Champ de texte pour le chemin de l'image
        textFieldChemin = new JTextField(filmActuel.getCheminImage());
        textFieldChemin.setBounds(300, 230, 400, 30);
        textFieldChemin.setForeground(Color.WHITE);
        textFieldChemin.setBackground(Color.BLACK);
        panel.add(textFieldChemin);

        //Label pour le nom du réalisateur
        JLabel labelRealisateur = new JLabel("Réalisateur:");
        labelRealisateur.setBounds(150, 260, 100, 30);
        labelRealisateur.setForeground(Color.WHITE);
        panel.add(labelRealisateur);

        //Champ de texte pour le nom du réalisateur
        textFieldRealisateur = new JTextField(filmActuel.getRealisateur());
        textFieldRealisateur.setBounds(300, 260, 400, 30);
        textFieldRealisateur.setForeground(Color.WHITE);
        textFieldRealisateur.setBackground(Color.BLACK);
        panel.add(textFieldRealisateur);

        //Label pour le synopsis
        JLabel labelSynopsis = new JLabel("Synopsis:");
        labelSynopsis.setBounds(150, 290, 100, 30);
        labelSynopsis.setForeground(Color.WHITE);
        panel.add(labelSynopsis);

        //Zone de texte pour le synopsis
        textAreaSynopsis = new JTextArea(filmActuel.getSynopsis());
        textAreaSynopsis.setBounds(300, 290, 400, 200);
        textAreaSynopsis.setLineWrap(true);
        textAreaSynopsis.setWrapStyleWord(true);
        textAreaSynopsis.setForeground(Color.WHITE);
        textAreaSynopsis.setBackground(Color.BLACK);
        panel.add(textAreaSynopsis);

        //Label pour les séances
        JLabel labelSeances = new JLabel("Séances");
        labelSeances.setBounds(900, 120, 200, 30);
        labelSeances.setForeground(Color.WHITE);
        labelSeances.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(labelSeances);

        //Label pour le film
        JLabel labelFilm = new JLabel("Film");
        labelFilm.setBounds(300, 120, 200, 30);
        labelFilm.setForeground(Color.WHITE);
        labelFilm.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(labelFilm);

        //Bouton pour Enregistrer les modifs
        boutonEnregistrer.setBounds(1330, 700, 150, 50);
        boutonEnregistrer.setFont(new Font("Arial", Font.BOLD, 20));
        boutonEnregistrer.setForeground(Color.WHITE);
        boutonEnregistrer.setBackground(Color.GRAY);
        panel.add(boutonEnregistrer);

        //Bouton pour ajouter une séance
        boutonAjouterSeance.setBounds(1160, 700, 160, 50);
        boutonAjouterSeance.setFont(new Font("Arial", Font.BOLD, 15));
        boutonAjouterSeance.setForeground(Color.WHITE);
        boutonAjouterSeance.setBackground(Color.GRAY);
        panel.add(boutonAjouterSeance);

        int ySeance = 0;
        int nbSeance = 0;

        for (Seance s : seances) {
            nbSeance++;

            JLabel labelSeance = new JLabel("Séance " + nbSeance + "    Date : " + s.getDate() + "  Heure : " + s.getHeure() + "  Prix : " + s.getPrix());
            labelSeance.setBounds(800, 200 + ySeance, 500, 30);
            labelSeance.setForeground(Color.WHITE);
            panel.add(labelSeance);
            ySeance += 50;
        }

        ySeance = 0;
        for(JButton b : boutonsModifier){
            b.setBounds(1100, 200 + ySeance, 100, 25);
            ySeance += 50;
            panel.add(b);
        }
        ySeance = 0;
        for(JButton b : boutonsSupprimer){
            b.setBounds(1210, 200 + ySeance, 100, 25);
            ySeance += 50;
            panel.add(b);
        }

        panel.setLayout(null);
        add(panel);
    }


    //Add listeners
    public void addListenerEnregistrer(ActionListener listener){boutonEnregistrer.addActionListener(listener);}
    public void addListenerSupprimer(ActionListener listener){boutonSupprimer.addActionListener(listener);}
    public void addListenerAjouterSeance(ActionListener listener){boutonAjouterSeance.addActionListener(listener);}
    public void addListenerRetour(ActionListener listener){
        boutonRetour.addActionListener(listener);
    }
    public void addListenerModifier(ActionListener listener){
        boutonModifier.addActionListener(listener);
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
    //Getters
    public String getTitre(){return textFieldTitre.getText();}
    public String getRealisateur(){return textFieldRealisateur.getText();}
    public String getChemin(){return textFieldChemin.getText();}
    public String getSynopsis(){return textAreaSynopsis.getText();}

}
