package Controleur;

import Modele.Client;
import Modele.Film;
import Vue.GUIaccueil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControleurAccueil {
    private Client client;
    private GUIaccueil vueAccueil;
    private Connexion connexion;
    private ArrayList<Film> films;
    private Film filmActuel;

    public ControleurAccueil(Connexion connexion) {
        this.connexion = connexion;
    }

    public void setVue(GUIaccueil vue){
        this.vueAccueil = vue;
        this.films = this.getFilms();
        this.vueAccueil.addListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JButton clickedButton = (JButton) e.getSource();
                filmActuel = vueAccueil.getBoutonFilmMap().get(clickedButton);
                System.out.println(filmActuel.getTitre());
                vueAccueil.afficherFilm(filmActuel);
            }
        });

        this.vueAccueil.addListenerRetour(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                vueAccueil.afficherMenu();
            }
        });
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
                // Extraire les informations sur le film
                String titre = infosFilm[0].trim();
                String realisateur = infosFilm[1].trim();

                StringBuilder synopsisBuilder = new StringBuilder();
                boolean reachedID = false;
                for (int i = 2; i < infosFilm.length; i++) {
                    if (!reachedID) {
                        synopsisBuilder.append(infosFilm[i]);
                        if (i + 1 < infosFilm.length && Character.isUpperCase(infosFilm[i + 1].charAt(0))) {
                            reachedID = true;
                        }
                    }
                }
                String synopsis = synopsisBuilder.toString().trim();

                int id = Integer.parseInt(infosFilm[infosFilm.length - 2].trim());
                String cheminImage = infosFilm[infosFilm.length - 1].trim();

                Film film = new Film(id, titre, realisateur, synopsis, cheminImage);
                films.add(film);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return films;
    }

}
