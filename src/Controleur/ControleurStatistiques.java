package Controleur;

import Modele.Client;
import Modele.FilmParAchat;
import Vue.*;

import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControleurStatistiques {
    private Client membre; // Membre associé au contrôleur
    private GUIstatistiques vueStat; // Interface graphique pour les statistiques
    private Connexion connexion; // Connexion à la base de données

    private ControleurComptes controleurComptes; // Contrôleur pour la gestion des comptes
    private GUIcomptes vueComptes; // Interface graphique pour la gestion des comptes

    // Constructeur pour initialiser la connexion à la base de données
    public ControleurStatistiques(Connexion connexion) {
        this.connexion = connexion;
    }

    // Méthode pour configurer l'interface graphique
    public void setVue(GUIstatistiques vue){
        this.vueStat = vue;
        // Ajoute un écouteur pour basculer vers l'onglet des films
        this.vueStat.addListenerOngletFilms(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                vueStat.closeWindow(); // Ferme la fenêtre actuelle
                // Configure et ouvre la fenêtre d'accueil des employés
                ControleurEmployeAccueil controleurAccueil = new ControleurEmployeAccueil(connexion);
                GUIEmployeAccueil vueAccueil = new GUIEmployeAccueil(membre, controleurAccueil);
                controleurAccueil.setVue(vueAccueil);
                controleurAccueil.setMembre(membre);
                controleurAccueil.openWindow();
            }
        });

        // Écouteur pour le bouton de déconnexion
        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Dialogue de confirmation de déconnexion
                JPanel panel = new JPanel();
                panel.add(new JLabel("Etes-vous sûr de vouloir vous déconnecter ?"));
                int resultat = JOptionPane.showConfirmDialog(null, panel, "Déconnexion", JOptionPane.OK_CANCEL_OPTION);
                if (resultat == JOptionPane.OK_OPTION) {
                    vueStat.closeWindow(); // Ferme la fenêtre sur confirmation
                    new GUIconnexion();
                    new ControleurConnexion(new GUIconnexion());
                }
            }
        };
        this.vueStat.addMouseListenerBoutonDeconnexion(mouseListener);

        // Ajoute un écouteur pour basculer vers l'onglet des comptes
        this.vueStat.addListenerOngletComptes(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                vueStat.closeWindow(); // Ferme la fenêtre actuelle
                // Configure et ouvre la fenêtre de gestion des comptes
                controleurComptes = new ControleurComptes(connexion);
                controleurComptes.setMembre(membre);
                vueComptes = new GUIcomptes(membre, controleurComptes);
                controleurComptes.setVue(vueComptes);
                controleurComptes.openWindow();
            }
        });

        // Ajoute un écouteur pour basculer vers l'onglet des réductions
        this.vueStat.addListenerOngletReduc(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                vueStat.closeWindow(); // Ferme la fenêtre actuelle
                ControleurReduction controleurReduction = new ControleurReduction(connexion);
                GUIreduction vueReduction = new GUIreduction(membre, controleurReduction);
                controleurReduction.setVue(vueReduction);
                controleurReduction.setMembre(membre);
            }
        });
    }

    // Méthode pour définir le membre associé au contrôleur
    public void setMembre(Client membre) {
        this.membre = membre;
    }

    // Récupère les films et leur nombre de places réservées par achat
    public ArrayList<FilmParAchat> getFilmsParAchat(){
        ArrayList<FilmParAchat> filmsParAchat = new ArrayList<>();
        try {
            // Requête SQL pour récupérer les films et le nombre de places réservées
            String requete = "SELECT films.Titre, SUM(commande.Nb_places) AS Nombre_de_commandes " +
                    "FROM films " +
                    "LEFT JOIN seance ON films.ID = seance.ID_film " +
                    "LEFT JOIN commande ON seance.ID = commande.ID_seance " +
                    "GROUP BY films.ID, films.Titre " +
                    "ORDER BY Nombre_de_commandes DESC;";
            ArrayList<String> resultatsFilms = connexion.remplirChampsRequete(requete);
            for (String resultat : resultatsFilms) {
                String[] infosFilm = resultat.split(",");
                String titre = infosFilm[0];
                int nbCommandes = infosFilm[1] != null && !infosFilm[1].equals("null") && !infosFilm[1].isEmpty() ? Integer.parseInt(infosFilm[1].trim()) : 0;
                filmsParAchat.add(new FilmParAchat(titre, nbCommandes));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return filmsParAchat;
    }
}
