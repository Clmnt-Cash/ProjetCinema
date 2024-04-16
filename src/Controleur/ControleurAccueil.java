package Controleur;

import Modele.Client;
import Modele.Film;
import Modele.Seance;
import Vue.GUIaccueil;
import Vue.GUIconnexion;
import Vue.GUIfilm;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ControleurAccueil {
    private Client client;
    private GUIaccueil vueAccueil;
    private Connexion connexion;
    private ArrayList<Film> films;
    private Film filmActuel;

    private ControleurFilm controleurFilm;
    private GUIfilm vueFilm;

    public ControleurAccueil(Connexion connexion) {
        this.connexion = connexion;
    }

    public void setVue(GUIaccueil vue){
        this.vueAccueil = vue;
        this.films = this.getFilms();
        //Aller sur la page du film
        this.vueAccueil.addListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton clickedButton = (JButton) e.getSource();
                filmActuel = vueAccueil.getBoutonFilmMap().get(clickedButton);
                vueAccueil.closeWindow();
                controleurFilm = new ControleurFilm(connexion, filmActuel, client);
                vueFilm = new GUIfilm(client, controleurFilm, filmActuel);
                controleurFilm.setVue(vueFilm);
            }
        });
        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel panel = new JPanel();
                panel.add(new JLabel("Etes-vous sûr de vouloir vous déconnecter ?"));

                int resultat = JOptionPane.showConfirmDialog(null, panel, "Déconnexion", JOptionPane.OK_CANCEL_OPTION);

                if (resultat == JOptionPane.OK_OPTION) {
                    vueAccueil.closeWindow();
                    GUIconnexion vueConnexion = new GUIconnexion();
                    ControleurConnexion controleurConnexion = new ControleurConnexion(vueConnexion);
                }
            }
        };
        this.vueAccueil.addMouseListenerBoutonDeconnexion(mouseListener);
    }
    public void setClient(Client client) {
        this.client = client;
    }

    public void openWindow(){
        this.vueAccueil.setVisible(true);
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
            ArrayList<String> resultatsSeances = connexion.remplirChampsRequete("SELECT * FROM seance WHERE ID_film = " + filmId + " ORDER BY Date_diffusion");
            for (String resultat : resultatsSeances) {
                String[] infosSeance = resultat.split(",");

                //Conversion de la date
                int id = Integer.parseInt(infosSeance[0].trim());

                String dateHeure = infosSeance[1].trim();
                String[] parties = dateHeure.split(" ");

                String date = parties[0];
                String mois = date.substring(5, 7);
                String jour = date.substring(8, 10);
                date = jour + "-" + mois;

                String heure = parties[1];
                heure = heure.substring(0, 5);
                int prix = Integer.parseInt(infosSeance[2].trim());

                //Créer une séance avec les informations récupérées
                Seance seance = new Seance(date, heure, prix, id);

                //Ajouter la séance à la liste
                seances.add(seance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return seances;
    }

}
