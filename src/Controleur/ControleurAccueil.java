package Controleur;

import Modele.Client;
import Modele.Film;
import Modele.Seance;
import Vue.GUIaccueil;
import Vue.GUIconnexion;
import Vue.GUIfilm;
import Vue.GUIpanier;

import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControleurAccueil {
    //Attributs
    private Client client;
    private GUIaccueil vueAccueil;
    private final Connexion connexion;
    private Film filmActuel;
    private ControleurFilm controleurFilm;
    private GUIfilm vueFilm;
    //Constructeur
    public ControleurAccueil(Connexion connexion) {
        this.connexion = connexion;
    }
    //Méthode pour initilaiser la vue
    public void setVue(GUIaccueil vue){
        this.vueAccueil = vue;
        //Aller sur la page du film
        this.vueAccueil.addListener(e -> {
            JButton clickedButton = (JButton) e.getSource();
            filmActuel = vueAccueil.getBoutonFilmMap().get(clickedButton);
            vueAccueil.closeWindow();
            controleurFilm = new ControleurFilm(connexion, filmActuel, client);
            vueFilm = new GUIfilm(client, controleurFilm, filmActuel);
            controleurFilm.setVue(vueFilm);
        });
        //Aller sur la page panier
        this.vueAccueil.addListenerPanier(e -> {
            vueAccueil.closeWindow();
            ControleurPanier controleurPanier = new ControleurPanier(connexion, client);
            GUIpanier vuePanier = new GUIpanier(client, controleurPanier);
            controleurPanier.setVue(vuePanier);
        });

        //Permet de se déconnecter
        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel panel = new JPanel();
                panel.add(new JLabel("Etes-vous sûr de vouloir vous déconnecter ?"));
                //Fenetre OK CANCEL
                int resultat = JOptionPane.showConfirmDialog(null, panel, "Déconnexion", JOptionPane.OK_CANCEL_OPTION);
                if (resultat == JOptionPane.OK_OPTION) {
                    //Ouvrir une nouvelle fenetre de connexion
                    vueAccueil.closeWindow();
                    GUIconnexion vueConnexion = new GUIconnexion();
                    ControleurConnexion controleurConnexion = new ControleurConnexion(vueConnexion);
                }
            }
        };
        this.vueAccueil.addMouseListenerBoutonDeconnexion(mouseListener);
    }
    //Méthode pour initialiser le client
    public void setClient(Client client) {
        this.client = client;
    }

    //Méthode pour rendre la fenetre visible
    public void openWindow(){
        this.vueAccueil.setVisible(true);
    }
    //Méthode pour récupérer le nombre de commandes depuis la bdd
    public int getNbCommande(){
        int nbCommandes = 0;
        try {
            //Récupérer le nombre de commandes non payées avec ID_client = id du client connecté
            String requete = "SELECT SUM(ID_client=" + client.getId() + ") AS Nombre_de_commandes FROM commande WHERE Paye = 0";
            ArrayList<String> resultatsFilms = connexion.remplirChampsRequete(requete);
            for (String resultat : resultatsFilms) {
                String[] infosFilm = resultat.split(",");
                //Si il y a des commandes
                if (infosFilm[0] != null && !infosFilm[0].equals("null") && !infosFilm[0].isEmpty()) {
                    try {
                        nbCommandes = Integer.parseInt(infosFilm[0].trim());
                    } catch (NumberFormatException e) {
                        nbCommandes = 0;
                    }
                } else { //Sinon mettre nbCommandes à 0
                    nbCommandes = 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nbCommandes;
    }

    //Méthode pour récupérer les films depuis la bdd
    public ArrayList<Film> getFilms(){
        //Liste de films
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
                //instanciation d un nouveau film qui sera ajouté àn la liste
                Film film = new Film(id, titre, realisateur, synopsis, cheminImage);

                //Création d'une liste de séances qui sera ajouté au film
                ArrayList<Seance> seances = getSeancesForFilm(id);
                film.setSeances(seances);
                films.add(film);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        //On retourne la liste
        return films;
    }

    //Méthode pour récupérer les séances de chaque film
    public ArrayList<Seance> getSeancesForFilm(int filmId) {
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
                int id = Integer.parseInt(infosSeance[0].trim());
                //Conversion de la date et l'heure
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
        //Retourner la liste des séances pour ce film
        return seances;
    }
}
