package Controleur;

import Modele.Client;
import Modele.Film;
import Modele.Seance;
import Vue.*;

import javax.naming.ldap.Control;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ControleurEmployeAccueil {
    private Client membre;
    private GUIEmployeAccueil vueEmployeAccueil;
    private Connexion connexion;
    private ArrayList<Film> films;
    private Film filmActuel;
    private ControleurFilm controleurFilm;
    private ControleurComptes controleurComptes;
    //private ControleurReduc controleurReduc;
    private GUIcomptes vueComptes;
    //private GUIreduc vueReduc;

    public ControleurEmployeAccueil(Connexion connexion) {
        this.connexion = connexion;
    }

    public void setVue(GUIEmployeAccueil vue){
        this.vueEmployeAccueil = vue;
        this.films = this.getFilms();

        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel panel = new JPanel();
                panel.add(new JLabel("Etes-vous sûr de vouloir vous déconnecter ?"));

                int resultat = JOptionPane.showConfirmDialog(null, panel, "Déconnexion", JOptionPane.OK_CANCEL_OPTION);

                if (resultat == JOptionPane.OK_OPTION) {
                    vueEmployeAccueil.closeWindow();
                    GUIconnexion vueConnexion = new GUIconnexion();
                    ControleurConnexion controleurConnexion = new ControleurConnexion(vueConnexion);
                }
            }
        };
        this.vueEmployeAccueil.addMouseListenerBoutonDeconnexion(mouseListener);
        this.vueEmployeAccueil.addListener(e -> {
            JButton clickedButton = (JButton) e.getSource();
            filmActuel = vueEmployeAccueil.getBoutonFilmMap().get(clickedButton);
            vueEmployeAccueil.closeWindow();
            ControleurModifierFilm controleurModifierFilm = new ControleurModifierFilm(connexion, filmActuel, membre);
            GUIModifierFilm vueModifierFilm = new GUIModifierFilm(membre, controleurModifierFilm, filmActuel);
            controleurModifierFilm.setVue(vueModifierFilm);
        });

        //Aller sur la page des comptes
        this.vueEmployeAccueil.addListenerOngletComptes(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                vueEmployeAccueil.closeWindow();
                ControleurComptes controleurComptes = new ControleurComptes(connexion);
                controleurComptes.setMembre(membre);
                GUIcomptes vueComptes = new GUIcomptes(membre, controleurComptes);
                controleurComptes.setVue(vueComptes);
                controleurComptes.openWindow();
            }
        });
        this.vueEmployeAccueil.addListenerOngletStat(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                vueEmployeAccueil.closeWindow();
                ControleurStatistiques controleurStatistiques = new ControleurStatistiques(connexion);
                GUIstatistiques vueStat = new GUIstatistiques(membre, controleurStatistiques);
                controleurStatistiques.setVue(vueStat);
                controleurStatistiques.setMembre(membre);
            }
        });
        this.vueEmployeAccueil.addListenerAjouter(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                //Customisation de la fenetre de dialogue
                UIManager.put("OptionPane.background", Color.WHITE);
                UIManager.put("Panel.background", Color.WHITE);
                UIManager.put("OptionPane.messageForeground", Color.WHITE);
                UIManager.put("Button.background", Color.WHITE);
                UIManager.put("Button.foreground", Color.BLACK);
                UIManager.put("Button.border", BorderFactory.createLineBorder(Color.WHITE));
                UIManager.put("Button.focus", Color.WHITE);

                JPanel panel = new JPanel();
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS)); // Utilisation de BoxLayout pour aligner les composants verticalement

                //Ajout du champ de texte pour le titre
                JLabel labelTitre = new JLabel("Titre :");
                JTextField textFieldTitre = new JTextField();
                textFieldTitre.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30)); // Augmentation de la hauteur à 30 pixels
                panel.add(labelTitre);
                panel.add(textFieldTitre);

                //Ajout du champ de texte pour le réalisateur
                JLabel labelRealisateur = new JLabel("Réalisateur :");
                JTextField textFieldRealisateur = new JTextField();
                textFieldRealisateur.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30)); // Augmentation de la hauteur à 30 pixels
                panel.add(labelRealisateur);
                panel.add(textFieldRealisateur);

                //Ajout du champ de texte pour le chemin vers l'image
                JLabel labelChemin = new JLabel("chemin vers l'affiche :");
                JTextField textFieldChemin = new JTextField();
                textFieldChemin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30)); // Augmentation de la hauteur à 30 pixels
                panel.add(labelChemin);
                panel.add(textFieldChemin);

                //Ajout du champ de texte pour le synopsis
                JLabel labelSynopsis = new JLabel("Synopsis :");
                JTextArea textAreaSynopsis = new JTextArea();
                textAreaSynopsis.setLineWrap(true);
                textAreaSynopsis.setWrapStyleWord(true);
                JScrollPane scrollPane = new JScrollPane(textAreaSynopsis);
                scrollPane.setPreferredSize(new Dimension(Integer.MAX_VALUE, 200)); // Le synopsis peut occuper toute la largeur, mais seulement 200 pixels de hauteur
                panel.add(labelSynopsis);
                panel.add(scrollPane);


                int resultat = JOptionPane.showConfirmDialog(null, panel, "Ajouter un film", JOptionPane.OK_CANCEL_OPTION);

                if (resultat == JOptionPane.OK_OPTION) {
                    vueEmployeAccueil.closeWindow();
                    String titre = textFieldTitre.getText();
                    String realisateur = textFieldRealisateur.getText();
                    String synopsis = textAreaSynopsis.getText();
                    String chemin = textFieldChemin.getText();
                    ajouterFilm(titre, realisateur, synopsis, chemin);
                    ControleurEmployeAccueil controleurEmployeAccueil = new ControleurEmployeAccueil(connexion);
                    GUIEmployeAccueil vueEmployeAccueil = new GUIEmployeAccueil(membre, controleurEmployeAccueil);
                    controleurEmployeAccueil.setVue(vueEmployeAccueil);
                    controleurEmployeAccueil.setMembre(membre);

                }
            }
        });
        //Aller sur la page des reductions
        this.vueEmployeAccueil.addListenerOngletReduc(new ActionListener(){
            //Ouverture de la page menu
            @Override
            public void actionPerformed(ActionEvent e) {
                vueEmployeAccueil.closeWindow();
                ControleurReduction controleurReduction = new ControleurReduction(connexion);
                GUIreduction vueReduction = new GUIreduction(membre, controleurReduction);
                controleurReduction.setVue(vueReduction);
                controleurReduction.setMembre(membre);
            }
        });
    }
    public void setMembre(Client membre) {
        this.membre = membre;
    }

    public void openWindow(){
        this.vueEmployeAccueil.setVisible(true);
    }

    public void ajouterFilm(String titre, String realisateur, String synopsis, String chemin){
        try {
            String requeteInsertion = "INSERT INTO films (Titre, Realisateur, Synopsis, Chemin_image) VALUES ('" + titre + "', '" + realisateur + "', '" + synopsis + "','" + chemin + "')";
            connexion.executerRequete(requeteInsertion);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la connexion à la base de données : " + e);
        }
    }

    public ArrayList<Film> getFilms(){
        ArrayList<Film> films = new ArrayList<>();

        try {
            ArrayList<String> resultatsFilms = connexion.remplirChampsRequete("SELECT * FROM films");

            for (String resultat : resultatsFilms) {
                String[] infosFilm = resultat.split(",");
                //Extraire les informations sur le film
                String titre = infosFilm[0].trim();
                String realisateur = infosFilm[1].trim();

                StringBuilder synopsisBuilder = new StringBuilder();
                boolean reachedID = false;
                //Pour le synopsis, prendre tout jusqu'à ce qu'il y ait ',numero'
                for (int i = 2; i < infosFilm.length; i++) {
                    if (!reachedID) {
                        if (i + 1 < infosFilm.length && Character.isUpperCase(infosFilm[i + 1].charAt(0))) {
                            reachedID = true;
                        }
                        else{
                            synopsisBuilder.append(infosFilm[i]);
                        }
                    }
                }
                String synopsis = synopsisBuilder.toString().trim();

                int id = Integer.parseInt(infosFilm[infosFilm.length - 2].trim());
                String cheminImage = infosFilm[infosFilm.length - 1].trim();
                Film film = new Film(id, titre, realisateur, synopsis, cheminImage);
                ArrayList<Seance> seances = getSeancesForFilm(id); // Méthode à implémenter
                film.setSeances(seances);
                films.add(film);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return films;
    }
    private ArrayList<Seance> getSeancesForFilm(int filmId) {
        ArrayList<Seance> seances = new ArrayList<>();

        try {
            //Récupérer les résultats de la requête SQL pour les séances du film donné
            ArrayList<String> resultatsSeances = connexion.remplirChampsRequete(
                    "SELECT seance.*, films.Titre " +
                            "FROM seance " +
                            "INNER JOIN films ON seance.ID_film = films.ID " +
                            "WHERE seance.ID_film = " + filmId + " " +
                            "ORDER BY seance.Date_diffusion"
            );
            for (String resultat : resultatsSeances) {
                String[] infosSeance = resultat.split(",");

                //Conversion de la date
                int id = Integer.parseInt(infosSeance[0].trim());

                String dateHeure = infosSeance[1].trim();
                String[] parties = dateHeure.split(" ");

                String date = parties[0];
                String mois = date.substring(5, 7);
                String jour = date.substring(8, 10);
                date = jour + "/" + mois;

                String heure = parties[1];
                heure = heure.substring(0, 5);
                float prix = Float.parseFloat(infosSeance[2].trim());
                String titre = infosSeance[3].trim();

                //Créer une séance avec les informations récupérées
                Seance seance = new Seance(date, heure, prix, id, titre);

                //Ajouter la séance à la liste
                seances.add(seance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seances;
    }

}
