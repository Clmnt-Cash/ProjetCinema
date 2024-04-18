package Controleur;

import Modele.Client;
import Modele.Film;
import Modele.Seance;
import Vue.GUIEmployeAccueil;
import Vue.GUIModifierFilm;
import Vue.GUIcomptes;
import Vue.GUIfilm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ControleurEmployeAccueil {
    private Client membre;
    private GUIEmployeAccueil vueMembre;
    private Connexion connexion;
    private ArrayList<Film> films;
    private Film filmActuel;
    private ControleurFilm controleurFilm;
    private ControleurComptes controleurComptes;
    private GUIcomptes vueComptes;

    public ControleurEmployeAccueil(Connexion connexion) {
        this.connexion = connexion;
    }

    public void setVue(GUIEmployeAccueil vue){
        this.vueMembre = vue;
        this.films = this.getFilms();

        this.vueMembre.addListener(e -> {
            JButton clickedButton = (JButton) e.getSource();
            filmActuel = vueMembre.getBoutonFilmMap().get(clickedButton);
            vueMembre.closeWindow();
            ControleurModifierFilm controleurModifierFilm = new ControleurModifierFilm(connexion, filmActuel, membre);
            GUIModifierFilm vueModifierFilm = new GUIModifierFilm(membre, controleurModifierFilm, filmActuel);
        });

        //Aller sur la page des films
        this.vueMembre.addListenerOngletComptes(new ActionListener(){
            //Ouverture de la page menu
            @Override
            public void actionPerformed(ActionEvent e) {
                vueMembre.closeWindow();
                controleurComptes = new ControleurComptes(connexion);
                controleurComptes.setMembre(membre);
                vueComptes = new GUIcomptes(membre, controleurComptes);
                controleurComptes.setVue(vueComptes);
                controleurComptes.openWindow();
            }
        });
    }
    public void setMembre(Client membre) {
        this.membre = membre;
    }

    public void openWindow(){
        this.vueMembre.setVisible(true);
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
