package Vue;

import Controleur.ControleurAccueil;
import Controleur.ControleurFilm;
import Modele.Client;
import Modele.Film;
import Modele.Reduction;
import Modele.Seance;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

public class GUIfilm extends JFrame {
    //Attributs
    private Client client;
    private ControleurFilm controleurFilm;
    private JButton boutonRetour;
    private Film filmActuel;
    private ArrayList<JButton> boutonsSeance;
    private ArrayList<JButton> boutonsDate;
    private ArrayList<Seance> seanceParDate;

    private Reduction reduction;
    private Seance seance;

    //Constructeur
    public GUIfilm(Client client, ControleurFilm controleurFilm, Film film) {
        super("Cinéma");
        this.client = client;
        this.controleurFilm = controleurFilm;
        this.filmActuel = film;
        this.boutonsSeance = new ArrayList<JButton>();
        this.boutonsDate = new ArrayList<JButton>();
        this.seanceParDate = new ArrayList<Seance>();
        this.reduction = controleurFilm.getReduction();
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
                g.fillRect(0, 100, 1500, 700);
                Color c2 = new Color(50, 50, 50, 200);
                g.setColor(c2);
                g.fillRect(0, 0, 1500, 100);
                ImageIcon logo = new ImageIcon("images/logos/logo_black.png");
                Image image = logo.getImage();
                g.drawImage(image, 0, 0, 100, 100, this);
            }
        };

        JLabel labelFilm = new JLabel(filmActuel.getTitre());
        labelFilm.setFont(labelFilm.getFont().deriveFont(Font.BOLD, 30));
        Dimension size1 = labelFilm.getPreferredSize();
        labelFilm.setBounds(750 - size1.width/2, 30, size1.width, size1.height);
        labelFilm.setForeground(Color.WHITE);
        panel.add(labelFilm);

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


        //Ajout du bouton Retour
        boutonRetour = new JButton("Retour");
        boutonRetour.setBounds(10, 110, 100, 50);
        boutonRetour.setForeground(Color.WHITE);
        boutonRetour.setBackground(Color.BLACK);
        panel.add(boutonRetour);

        JLabel labelSeances = new JLabel("Séances");
        labelSeances.setBounds(900, 120, 200, 30);
        labelSeances.setForeground(Color.WHITE);
        labelSeances.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(labelSeances);

        //Ajout des boutons pour chaque séance
        ArrayList<Seance> seances = film.getSeances();

        ArrayList<String> datesUniques = new ArrayList<String>();
        for(Seance s : seances){
            if(!datesUniques.contains(s.getDate()))datesUniques.add(s.getDate());
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
                    //Calcul du prix selon les réductions
                    if (client.getType() == 1) {
                        prix = prix - (prix * reduction.getReductionEnfant()/100);
                    } else if (client.getType() == 2) {
                        prix = prix - (prix* reduction.getReductionRegulier()/100);
                    } else if (client.getType() == 3) {
                        prix = (prix * reduction.getReductionSenior()/100);
                    }

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
        setVisible(true);
        panel.setLayout(null);
        add(panel);

    }

    public void addListenerRetour(ActionListener listener){
        boutonRetour.addActionListener(listener);
    }

    public void closeWindow(){setVisible(false);dispose();}

    public void addListenerSeance(ActionListener listener){
        for(JButton b : boutonsSeance){
            b.addActionListener(listener);
        }
    }
}
