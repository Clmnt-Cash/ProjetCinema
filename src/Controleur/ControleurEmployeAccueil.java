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
    //Attributs
    private Client membre;
    private GUIEmployeAccueil vueEmployeAccueil;
    private Connexion connexion;
    private Film filmActuel;
    //Constructeur
    public ControleurEmployeAccueil(Connexion connexion) {
        this.connexion = connexion;
    }

    public void setVue(GUIEmployeAccueil vue){
        this.vueEmployeAccueil = vue;
        //Se déconnecter
        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel panel = new JPanel();
                panel.add(new JLabel("Etes-vous sûr de vouloir vous déconnecter ?"));

                int resultat = JOptionPane.showConfirmDialog(null, panel, "Déconnexion", JOptionPane.OK_CANCEL_OPTION);

                if (resultat == JOptionPane.OK_OPTION) {
                    //Renvoyer sur la ppage connexion
                    vueEmployeAccueil.closeWindow();
                    GUIconnexion vueConnexion = new GUIconnexion();
                    ControleurConnexion controleurConnexion = new ControleurConnexion(vueConnexion);
                }
            }
        };
        this.vueEmployeAccueil.addMouseListenerBoutonDeconnexion(mouseListener);

        //Si l'employé clique sur un film, ouvrir la page pour modifier le film
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

        //Aller sur la page des statistiques
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

        //Ajouter un nouveau film
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
                panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

                //Ajout du champ de texte pour le titre
                JLabel labelTitre = new JLabel("Titre :");
                JTextField textFieldTitre = new JTextField();
                textFieldTitre.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
                panel.add(labelTitre);
                panel.add(textFieldTitre);

                //Ajout du champ de texte pour le réalisateur
                JLabel labelRealisateur = new JLabel("Réalisateur :");
                JTextField textFieldRealisateur = new JTextField();
                textFieldRealisateur.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
                panel.add(labelRealisateur);
                panel.add(textFieldRealisateur);

                //Ajout du champ de texte pour le chemin vers l image
                JLabel labelChemin = new JLabel("chemin vers l'affiche :");
                JTextField textFieldChemin = new JTextField();
                textFieldChemin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
                panel.add(labelChemin);
                panel.add(textFieldChemin);

                //Ajout du champ de texte pour le synopsis
                JLabel labelSynopsis = new JLabel("Synopsis :");
                JTextArea textAreaSynopsis = new JTextArea();
                textAreaSynopsis.setLineWrap(true);
                textAreaSynopsis.setWrapStyleWord(true);
                JScrollPane scrollPane = new JScrollPane(textAreaSynopsis);
                scrollPane.setPreferredSize(new Dimension(Integer.MAX_VALUE, 200));
                panel.add(labelSynopsis);
                panel.add(scrollPane);


                int resultat = JOptionPane.showConfirmDialog(null, panel, "Ajouter un film", JOptionPane.OK_CANCEL_OPTION);

                if (resultat == JOptionPane.OK_OPTION) {
                    //Vérifier si les champs sont tous remplis
                    if(!textAreaSynopsis.getText().isEmpty() && !textFieldChemin.getText().isEmpty() && !textFieldTitre.getText().isEmpty() && !textFieldRealisateur.getText().isEmpty()) {
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
                    } else { //Si un champ est vide
                        JPanel newpanel = new JPanel();
                        JLabel labelErreur = new JLabel();
                        newpanel.add(labelErreur);
                        labelErreur.setText("Certains champs sont vide, veuillez réessayer.");
                        JOptionPane.showConfirmDialog(null, newpanel, "Erreur", JOptionPane.OK_CANCEL_OPTION);
                    }

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

    //Méthode pour set le membre
    public void setMembre(Client membre) {
        this.membre = membre;
    }

    public void openWindow(){
        this.vueEmployeAccueil.setVisible(true);
    }

    //Méthode pour ajouter un membre dans la bdd
    public void ajouterFilm(String titre, String realisateur, String synopsis, String chemin){
        //Remplacer les ' dans le synopsis par des '' pour éviter les problèmes
        synopsis = synopsis.replace("'", "''");
        try {
            String requeteInsertion = "INSERT INTO films (Titre, Realisateur, Synopsis, Chemin_image) VALUES ('" + titre + "', '" + realisateur + "', '" + synopsis + "','" + chemin + "')";
            connexion.executerRequete(requeteInsertion);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'insertion du film : " + e);
        }
    }

    //Méthode pour récupérer les films
    public ArrayList<Film> getFilms(){
        ArrayList<Film> films = new ArrayList<>();

        try {
            ArrayList<String> resultatsFilms = connexion.remplirChampsRequete("SELECT * FROM films");

            for (String resultat : resultatsFilms) {
                String[] infosFilm = resultat.split(",");
                //Extraire les informations sur le film
                String titre = infosFilm[0].trim();
                String realisateur = infosFilm[1].trim();

                int id = Integer.parseInt(infosFilm[2].trim());
                String cheminImage = infosFilm[3].trim();
                StringBuilder synopsisBuilder = new StringBuilder();
                boolean reachedID = false;
                //Pour le synopsis, prendre tout jusqu'à la fin
                for (int i = 4; i < infosFilm.length; i++) {
                    if (!reachedID) {
                        synopsisBuilder.append(infosFilm[i]);
                    }
                }
                String synopsis = synopsisBuilder.toString().trim();
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

    //Méthode pour récupérer les séances d un film
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
