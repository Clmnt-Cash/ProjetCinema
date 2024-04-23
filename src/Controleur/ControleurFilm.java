package Controleur;

import Modele.*;
import Vue.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControleurFilm {
    // Déclaration des variables pour le client, la connexion, la vue et les modèles utilisés.
    private Client client;
    private GUIfilm vueFilm;
    private Connexion connexion;
    private Film filmActuel;
    private GUIaccueil vueAccueil;
    private ControleurAccueil controleurAccueil;
    private Reduction reduction;

    // Constructeur qui initialise le contrôleur avec la connexion, le film et le client.
    public ControleurFilm(Connexion connexion, Film film, Client client) {
        this.connexion = connexion;
        this.filmActuel = film;
        this.client = client;
        // Chargement des réductions disponibles à partir de la base de données.
        try {
            ArrayList<String> resultatReduction = connexion.remplirChampsRequete("SELECT * FROM reduction");
            for (String resultat : resultatReduction) {
                String[] infosReduction = resultat.split(",");
                int e = Integer.parseInt(infosReduction[0].trim());
                int r = Integer.parseInt(infosReduction[1].trim());
                int s = Integer.parseInt(infosReduction[2].trim());
                this.reduction = new Reduction(e, r, s);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Configuration de la vue avec les listeners pour les interactions utilisateur.
    public void setVue(GUIfilm vue){
        this.vueFilm = vue;

        // Listener pour retourner à l'accueil.
        this.vueFilm.addListenerRetour(e -> {
            vueFilm.closeWindow();
            controleurAccueil = new ControleurAccueil(connexion);
            controleurAccueil.setClient(client);
            vueAccueil = new GUIaccueil(client, controleurAccueil);
            controleurAccueil.setVue(vueAccueil);
            controleurAccueil.openWindow();
        });

        // Listener pour choisir une séance.
        this.vueFilm.addListenerSeance(e -> choisirSeance(e));
    }

    // Méthode pour gérer le choix d'une séance et l'ajout au panier ou paiement direct.
    private void choisirSeance(ActionEvent e) {
        JButton bouton = (JButton) e.getSource();
        JLabel labelHeure = (JLabel) bouton.getComponent(0);
        JLabel labelPrix = (JLabel) bouton.getComponent(1);
        String seanceChoisie = (String) bouton.getClientProperty("seance");
        String[] infos = seanceChoisie.split(",");
        int id = Integer.parseInt(infos[0].trim());
        String date = infos[1].trim();
        String heure = labelHeure.getText();
        String prix = labelPrix.getText().trim().substring(0, labelPrix.getText().length() - 1);
        float prixTot = Float.parseFloat(prix) * (int) ((JSpinner) JOptionPane.showInputDialog(null, "Nombre de places:", "Réservation", JOptionPane.PLAIN_MESSAGE, null, null, 1)).getValue();

        if(client.getType() != -1) { // Membre
            ajouterPanier(id, (int) prixTot);
        } else { // Non-membre
            new GUIpaiement(prixTot, connexion, client);
        }
    }

    // Méthode pour ajouter une commande au panier.
    private void ajouterPanier(int IDseance, int nbPlaces){
        try {
            String requeteInsertion = "INSERT INTO commande (ID_seance, ID_client, Nb_places, Paye) VALUES (" + IDseance + ", " + client.getId() + ", " + nbPlaces + ", 0)";
            connexion.executerRequete(requeteInsertion);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Getters et setters.
    public Reduction getReduction(){
        return reduction;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void openWindow(){
        this.vueFilm.setVisible(true);
    }
}
