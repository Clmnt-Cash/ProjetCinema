package Controleur;

import Modele.*;
import Vue.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControleurEmployeAccueil {
    // Déclaration des attributs nécessaires pour le contrôleur
    private Client membre;
    private GUIEmployeAccueil vueEmployeAccueil;
    private Connexion connexion;
    private Film filmActuel;

    // Constructeur avec connexion
    public ControleurEmployeAccueil(Connexion connexion) {
        this.connexion = connexion;
    }

    // Initialisation de la vue et configuration des listeners pour les interactions
    public void setVue(GUIEmployeAccueil vue) {
        this.vueEmployeAccueil = vue;

        // Listener pour la déconnexion
        this.vueEmployeAccueil.addMouseListenerBoutonDeconnexion(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (confirmerDeconnexion()) {
                    vueEmployeAccueil.closeWindow();
                    new GUIconnexion();
                    new ControleurConnexion(new GUIconnexion());
                }
            }
        });

        // Listener pour l'ouverture de la modification de film
        this.vueEmployeAccueil.addListener(e -> ouvrirModificationFilm(e));

        // Navigation vers la page des comptes
        this.vueEmployeAccueil.addListenerOngletComptes(e -> ouvrirGestionComptes());

        // Navigation vers la page des statistiques
        this.vueEmployeAccueil.addListenerOngletStat(e -> ouvrirStatistiques());

        // Ajouter un nouveau film
        this.vueEmployeAccueil.addListenerAjouter(e -> ajouterNouveauFilm());
    }

    // Méthode pour confirmer la déconnexion
    private boolean confirmerDeconnexion() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Etes-vous sûr de vouloir vous déconnecter ?"));
        return JOptionPane.showConfirmDialog(null, panel, "Déconnexion", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
    }

    // Méthode pour ouvrir l'interface de modification de film
    private void ouvrirModificationFilm(ActionEvent e) {
        JButton clickedButton = (JButton) e.getSource();
        filmActuel = vueEmployeAccueil.getBoutonFilmMap().get(clickedButton);
        vueEmployeAccueil.closeWindow();
        GUIModifierFilm vueModifierFilm = new GUIModifierFilm(membre, new ControleurModifierFilm(connexion, filmActuel, membre), filmActuel);
        vueModifierFilm.setVisible(true);
    }

    // Méthode pour ouvrir l'interface de gestion des comptes
    private void ouvrirGestionComptes() {
        vueEmployeAccueil.closeWindow();
        ControleurComptes controleurComptes = new ControleurComptes(connexion);
        controleurComptes.setMembre(membre);
        GUIcomptes vueComptes = new GUIcomptes(membre, controleurComptes);
        vueComptes.setVisible(true);
    }

    // Méthode pour ouvrir l'interface des statistiques
    private void ouvrirStatistiques() {
        vueEmployeAccueil.closeWindow();
        GUIstatistiques vueStat = new GUIstatistiques(membre, new ControleurStatistiques(connexion));
        vueStat.setVisible(true);
    }

    // Méthode pour ajouter un nouveau film via un dialogue
    private void ajouterNouveauFilm() {
        if (JOptionPane.showConfirmDialog(null, creerPanelAjoutFilm(), "Ajouter un film", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            // Logique pour récupérer les valeurs des champs et ajouter le film dans la base de données
            ajouterFilm("titre", "réalisateur", "synopsis", "chemin");
        }
    }

    // Crée et retourne le JPanel pour l'ajout d'un film
    private JPanel creerPanelAjoutFilm() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Titre :"));
        panel.add(new JTextField());
        // Ajouter plus de composants comme réalisateur, synopsis, etc.
        return panel;
    }

    // Méthode pour ajouter les informations d'un film dans la base de données
    public void ajouterFilm(String titre, String realisateur, String synopsis, String chemin) {
        try {
            String requeteInsertion = "INSERT INTO films (Titre, Realisateur, Synopsis, Chemin_image) VALUES ('" + titre + "', '" + realisateur + "', '" + synopsis + "', '" + chemin + "')";
            connexion.executerRequete(requeteInsertion);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Setter pour le membre
    public void setMembre(Client membre) {
        this.membre = membre;
    }

    // Méthode pour rendre la fenêtre visible
    public void openWindow() {
        this.vueEmployeAccueil.setVisible(true);
    }
}
