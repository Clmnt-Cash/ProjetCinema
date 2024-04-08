package Controleur;

import Modele.Client;
import Modele.Film;
import Vue.GUIaccueil;

import java.sql.SQLException;
import java.util.ArrayList;

public class ControleurAccueil {
    private Client client;
    private GUIaccueil vueAccueil;
    private Connexion connexion;
    public ControleurAccueil(GUIaccueil vueAccueil, Connexion connexion){
        this.connexion=connexion;
        this.vueAccueil=vueAccueil;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void openWindow(){
        this.vueAccueil.setVisible(true);
    }
    public void setVueAccueil(GUIaccueil vueAccueil){
        this.vueAccueil = vueAccueil;
    }
    public ArrayList<Film> getFilms(){
        ArrayList<Film> films = new ArrayList<>();

        try {
            // Exécuter la requête SQL pour récupérer les informations sur les films
            ArrayList<String> resultatsFilms = connexion.remplirChampsRequete("SELECT * FROM films");

            // Parcourir les résultats de la requête et créer des objets Film
            for (String resultat : resultatsFilms) {
                String[] infosFilm = resultat.split(","); // Supposons que les attributs de Film sont dans cet ordre dans la base de données
                // Extraire les informations sur le film
                String titre = infosFilm[0].trim();
                String realisateur = infosFilm[1].trim();

                // Rejoindre les parties du synopsis jusqu'à celle contenant l'ID
                StringBuilder synopsisBuilder = new StringBuilder();
                boolean reachedID = false;
                for (int i = 2; i < infosFilm.length; i++) {
                    if (!reachedID) {
                        synopsisBuilder.append(infosFilm[i]);
                        // Vérifier si la prochaine partie commence par une majuscule, ce qui indique probablement le début de l'ID
                        if (i + 1 < infosFilm.length && Character.isUpperCase(infosFilm[i + 1].charAt(0))) {
                            reachedID = true;
                        }
                    }
                }
                String synopsis = synopsisBuilder.toString().trim();

                int id = Integer.parseInt(infosFilm[infosFilm.length - 2].trim()); // Récupère l'avant-dernière valeur
                String cheminImage = infosFilm[infosFilm.length - 1].trim(); // Récupère la dernière valeur


                // Créer un objet Film et l'ajouter à la liste des films
                Film film = new Film(id, titre, realisateur, synopsis, cheminImage);
                films.add(film);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return films;
    }

}
