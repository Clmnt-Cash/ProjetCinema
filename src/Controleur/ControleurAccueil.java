package Controleur;

import Modele.Client;
import Modele.Film;
import Modele.FilmParAchat;
import Modele.Seance;
import Vue.GUIaccueil;
import Vue.GUIconnexion;
import Vue.GUIfilm;
import Vue.GUIpanier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

// Contrôleur principal pour l'interface d'accueil du système de cinéma
public class ControleurAccueil {
    private Client client;
    private GUIaccueil vueAccueil; // Vue associée à l'accueil
    private Connexion connexion; // Connexion à la base de données
    private ArrayList<Film> films; // Liste des films disponibles
    private Film filmActuel; // Film sélectionné

    private ControleurFilm controleurFilm;
    private GUIfilm vueFilm;

    // Constructeur initialise la connexion
    public ControleurAccueil(Connexion connexion) {
        this.connexion = connexion;
    }

    // Configure la vue et ajoute des écouteurs d'événements
    public void setVue(GUIaccueil vue){
        this.vueAccueil = vue;
        this.films = this.getFilms();

        // Ajoute des écouteurs pour ouvrir les vues des films et du panier
        this.vueAccueil.addListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JButton clickedButton = (JButton) e.getSource();
                filmActuel = vueAccueil.getBoutonFilmMap().get(clickedButton);
                vueAccueil.closeWindow();
                controleurFilm = new ControleurFilm(connexion, filmActuel, client);
                vueFilm = new GUIfilm(client, controleurFilm, filmActuel);
                controleurFilm.setVue(vueFilm);
            }
        });

        // Écouteur pour le panier
        this.vueAccueil.addListenerPanier(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                vueAccueil.closeWindow();
                ControleurPanier controleurPanier = new ControleurPanier(connexion, client);
                GUIpanier vuePanier = new GUIpanier(client, controleurPanier);
                controleurPanier.setVue(vuePanier);
            }
        });

        // Écouteur pour la déconnexion
        MouseListener mouseListener = new MouseAdapter() {
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

    // Défini le client actuel
    public void setClient(Client client) {
        this.client = client;
    }

    // Affiche la fenêtre d'accueil
    public void openWindow(){
        this.vueAccueil.setVisible(true);
    }

    // Récupère le nombre de commandes en attente de paiement
    public int getNbCommande(){
        int nbCommandes = 0;
        try {
            String requete = "SELECT SUM(ID_client=" + client.getId() + ") AS Nombre_de_commandes FROM commande WHERE Paye = 0";
            ArrayList<String> resultatsFilms = connexion.remplirChampsRequete(requete);
            for (String resultat : resultatsFilms) {
                String[] infosFilm = resultat.split(",");
                try {
                    nbCommandes = Integer.parseInt(infosFilm[0].trim());
                } catch (NumberFormatException e) {
                    nbCommandes = 0; // En cas d'erreur de format
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return nbCommandes;
    }

    // Récupère les films disponibles
    public ArrayList<Film> getFilms(){
        ArrayList<Film> films = new ArrayList<>();
        try {
            ArrayList<String> resultatsFilms = connexion.remplirChampsRequete("SELECT * FROM films");
            for (String resultat : resultatsFilms) {
                String[] infosFilm = resultat.split(",");
                String titre = infosFilm[0].trim();
                String realisateur = infosFilm[1].trim();
                int id = Integer.parseInt(infosFilm[2].trim());
                String cheminImage = infosFilm[3].trim();
                StringBuilder synopsisBuilder = new StringBuilder();
                for (int i = 4; i < infosFilm.length; i++) {
                    synopsisBuilder.append(infosFilm[i]);
                }
                String synopsis = synopsisBuilder.toString().trim();
                Film film = new Film(id, titre, realisateur, synopsis, cheminImage);
                ArrayList<Seance> seances = getSeancesForFilm(id);
                film.setSeances(seances);
                films.add(film);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return films;
    }

    // Récupère les séances pour un film donné
    public ArrayList<Seance> getSeancesForFilm(int filmId) {
        ArrayList<Seance> seances = new ArrayList<>();
        try {
            ArrayList<String> resultatsSeances = connexion.remplirChampsRequete(
                    "SELECT seance.*, films.Titre FROM seance INNER JOIN films ON seance.ID_film = films.ID WHERE seance.ID_film = " + filmId + " ORDER BY seance.Date_diffusion");
            for (String resultat : resultatsSeances) {
                String[] infosSeance = resultat.split(",");
                int id = Integer.parseInt(infosSeance[0].trim());
                String dateHeure = infosSeance[1].trim();
                String[] parties = dateHeure.split(" ");
                String date = parties[0].substring(8, 10) + "/" + parties[0].substring(5, 7);
                String heure = parties[1].substring(0, 5);
                float prix = Float.parseFloat(infosSeance[2].trim());
                String titre = infosSeance[3].trim();
                Seance seance = new Seance(date, heure, prix, id, titre);
                seances.add(seance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seances;
    }
}
