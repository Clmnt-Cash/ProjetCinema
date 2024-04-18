package Controleur;

import Modele.Client;
import Modele.Film;
import Modele.Reduction;
import Modele.Seance;
import Vue.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControleurModifierFilm {
    //Attributs
    private Client client;
    private GUIModifierFilm vueModifierFilm;
    private Connexion connexion;
    private Film filmActuel;
    private GUIaccueil vueAccueil;
    private ControleurAccueil controleurAccueil;

    //Constructeur
    public ControleurModifierFilm(Connexion connexion, Film film, Client client) {
        this.connexion = connexion;
        this.filmActuel = film;
        this.client = client;
    }
    //Methode pour initialiser la vue
    public void setVue(GUIModifierFilm vue){
        this.vueModifierFilm = vue;
        this.vueModifierFilm.addListenerRetour(new ActionListener(){
            //Ouverture de la page menu
            @Override
            public void actionPerformed(ActionEvent e) {
                vueModifierFilm.closeWindow();
                ControleurEmployeAccueil controleurEmployeAccueil = new ControleurEmployeAccueil(connexion);
                GUIEmployeAccueil vueEmployeAccueil= new GUIEmployeAccueil(client, controleurEmployeAccueil);
                controleurEmployeAccueil.setVue(vueEmployeAccueil);
                controleurEmployeAccueil.setMembre(client);
                controleurEmployeAccueil.openWindow();
            }
        });
        //Enregistrer les modifications
        this.vueModifierFilm.addListenerEnregistrer(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                vueModifierFilm.closeWindow();
                ArrayList<Seance> seances = getSeancesForFilm(filmActuel.getId());
                filmActuel.setSeances(seances);
                modifierFilm();
                ControleurModifierFilm controleurModifierFilm = new ControleurModifierFilm(connexion, filmActuel, client);
                GUIModifierFilm vueModifierFilm = new GUIModifierFilm(client, controleurModifierFilm, filmActuel);
                controleurModifierFilm.setVue(vueModifierFilm);
                controleurModifierFilm.setClient(client);
            }
        });

        this.vueModifierFilm.addListenerModifier(new ActionListener(){
            //Modifier le film
            @Override
            public void actionPerformed(ActionEvent e) {
                vueModifierFilm.affichageModifier();
            }
        });
    }

    //Méthode pour initialiser le client
    public void setClient(Client client) {
        this.client = client;
    }

    //Méthode pour ouvrir la fenetre
    public void openWindow(){
        this.vueModifierFilm.setVisible(true);
    }

    public void modifierFilm(){
        String titre = this.vueModifierFilm.getTitre();
        String realisateur = this.vueModifierFilm.getRealisateur();
        String chemin = this.vueModifierFilm.getChemin();
        String synopsis = this.vueModifierFilm.getSynopsis();
        this.filmActuel.setTitre(titre);
        this.filmActuel.setRealisateur(realisateur);
        this.filmActuel.setCheminImage(chemin);
        this.filmActuel.setSynopsis(synopsis);

        try {
            String requeteInsertion = "UPDATE films" +
                    " SET Titre = '" + titre + "'," +
                    " Realisateur = '" + realisateur + "'," +
                    " Synopsis = '" + synopsis + "'," +
                    " Chemin_image = '" + chemin + "'" +
                    " WHERE ID = " + filmActuel.getId();

            connexion.executerRequete(requeteInsertion);


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la connexion à la base de données : " + e);
        }
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
