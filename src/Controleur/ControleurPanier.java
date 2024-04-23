package Controleur;

import Modele.*;
import Vue.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControleurPanier {
    // Attributs
    private Client client;
    private Connexion connexion;
    private GUIpanier vuePanier;
    private Reduction reduction;
    private ArrayList<Commande> commandes;

    // Constructeur
    public ControleurPanier(Connexion connexion, Client client) {
        this.connexion = connexion;
        this.client = client;
        try {
            // Récupération des réductions depuis la base de données
            ArrayList<String> resultatReduction = connexion.remplirChampsRequete("SELECT * FROM reduction");
            for (String resultat : resultatReduction) {
                String[] infosReduction = resultat.split(",");
                this.reduction = new Reduction(Integer.parseInt(infosReduction[0].trim()), Integer.parseInt(infosReduction[1].trim()), Integer.parseInt(infosReduction[2].trim()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        this.commandes = getCommandes(client.getId(), reduction);
    }

    // Méthode pour initialiser la vue
    public void setVue(GUIpanier vue){
        this.vuePanier = vue;

        // Ajout de listeners pour les actions de l'utilisateur
        this.vuePanier.addListenerRetour(e -> retourAccueil());
        this.vuePanier.addListenerPayer(e -> effectuerPaiement());
        this.vuePanier.addListenerModifier(e -> modifierCommande(e));
        this.vuePanier.addListenerSupprimer(e -> supprimerCommande(e));
        this.vuePanier.addMouseListenerBoutonDeconnexion(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                deconnecter();
            }
        });
    }

    private void retourAccueil() {
        vuePanier.closeWindow();
        ControleurAccueil controleurAccueil = new ControleurAccueil(connexion);
        controleurAccueil.setClient(client);
        GUIaccueil vueAccueil = new GUIaccueil(client, controleurAccueil);
        controleurAccueil.setVue(vueAccueil);
        controleurAccueil.openWindow();
    }

    private void effectuerPaiement() {
        vuePanier.closeWindow();
        float prix = vuePanier.getPrix();
        new GUIpaiement(prix, connexion, client);
    }

    private void modifierCommande(ActionEvent e) {
        JButton bouton = (JButton) e.getSource();
        String[] infos = bouton.getClientProperty("infos").toString().split(",");
        int idCommande = Integer.parseInt(infos[0].trim());
        String film = infos[1].trim();
        int places = Integer.parseInt(infos[2].trim());
        // Création et gestion de la fenêtre de dialogue pour la modification
        JOptionPane.showConfirmDialog(null, creerPanelModification(film, places), "Places pour " + film, JOptionPane.OK_CANCEL_OPTION);
    }

    private void supprimerCommande(ActionEvent e) {
        JButton bouton = (JButton) e.getSource();
        String[] infos = bouton.getClientProperty("infos").toString().split(",");
        int idCommande = Integer.parseInt(infos[0].trim());
        String film = infos[1].trim();
        // Création et gestion de la fenêtre de dialogue pour la suppression
        JOptionPane.showConfirmDialog(null, creerPanelSuppression(film), "Annuler une commande", JOptionPane.OK_CANCEL_OPTION);
    }

    private void deconnecter() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Etes-vous sûr de vouloir vous déconnecter ?"));
        int resultat = JOptionPane.showConfirmDialog(null, panel, "Déconnexion", JOptionPane.OK_CANCEL_OPTION);
        if (resultat == JOptionPane.OK_OPTION) {
            vuePanier.closeWindow();
            new GUIconnexion();
            new ControleurConnexion(new GUIconnexion());
        }
    }

    // Méthode pour récupérer les commandes
    private ArrayList<Commande> getCommandes(int clientID, Reduction reduction){
        ArrayList<Commande> commandes = new ArrayList<>();
        // Logique pour extraire les données de commandes de la base de données
        return commandes;
    }

    // Panels de dialogue personnalisés pour modifier et supprimer des commandes
    private JPanel creerPanelModification(String film, int places) {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Modifier le nombre de places pour " + film + " :"));
        panel.add(new JSpinner(new SpinnerNumberModel(places, 1, 10, 1)));
        return panel;
    }

    private JPanel creerPanelSuppression(String film) {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Voulez-vous vraiment annuler la commande de la séance pour " + film + " ?"));
        return panel;
    }

    // Méthodes pour modifier et supprimer des commandes dans la base de données
    private void executeUpdate(String requete) {
        try {
            connexion.executerRequete(requete);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'exécution de la requête : " + e.getMessage());
        }
    }
}
