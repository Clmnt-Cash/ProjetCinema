package Controleur;

import Modele.*;
import Vue.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControleurModifierFilm {
    // Attributs pour gérer les informations de film, client et connexion
    private Client client;
    private GUIModifierFilm vueModifierFilm;
    private Connexion connexion;
    private Film filmActuel;

    // Constructeur initialisant le contrôleur avec les composants nécessaires
    public ControleurModifierFilm(Connexion connexion, Film film, Client client) {
        this.connexion = connexion;
        this.filmActuel = film;
        this.client = client;
    }

    // Configuration de la vue avec les listeners appropriés pour les interactions utilisateur
    public void setVue(GUIModifierFilm vue) {
        this.vueModifierFilm = vue;

        // Listener pour le retour à l'accueil
        this.vueModifierFilm.addListenerRetour(e -> retourAccueil());

        // Listener pour la déconnexion
        this.vueModifierFilm.addMouseListenerBoutonDeconnexion(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                deconnecter();
            }
        });

        // Listener pour enregistrer les modifications du film
        this.vueModifierFilm.addListenerEnregistrer(e -> enregistrerModifications());

        // Listener pour supprimer un film
        this.vueModifierFilm.addListenerSupprimer(this::supprimerFilm);

        // Listeners pour ajouter et modifier des séances
        this.vueModifierFilm.addListenerAjouterSeance(this::ajouterSeance);
        this.vueModifierFilm.addListenersModifier(this::modifierSeance);

        // Listener pour modifier les données d'un film
        this.vueModifierFilm.addListenerModifier(e -> vueModifierFilm.affichageModifier());
    }

    private void retourAccueil() {
        vueModifierFilm.closeWindow();
        ControleurEmployeAccueil controleurAccueil = new ControleurEmployeAccueil(connexion);
        GUIEmployeAccueil vueAccueil = new GUIEmployeAccueil(client, controleurAccueil);
        controleurAccueil.setVue(vueAccueil);
        controleurAccueil.setMembre(client);
        controleurAccueil.openWindow();
    }

    private void deconnecter() {
        int resultat = JOptionPane.showConfirmDialog(null, "Etes-vous sûr de vouloir vous déconnecter ?", "Déconnexion", JOptionPane.OK_CANCEL_OPTION);
        if (resultat == JOptionPane.OK_OPTION) {
            vueModifierFilm.closeWindow();
            new GUIconnexion();
            new ControleurConnexion(new GUIconnexion());
        }
    }

    private void enregistrerModifications() {
        vueModifierFilm.closeWindow();
        ArrayList<Seance> seances = getSeancesForFilm(filmActuel.getId());
        filmActuel.setSeances(seances);
        modifierFilm();
        ControleurModifierFilm controleurModifierFilm = new ControleurModifierFilm(connexion, filmActuel, client);
        GUIModifierFilm vueModifierFilm = new GUIModifierFilm(client, controleurModifierFilm, filmActuel);
        controleurModifierFilm.setVue(vueModifierFilm);
    }

    private void supprimerFilm(ActionEvent e) {
        int resultat = JOptionPane.showConfirmDialog(null, "Etes-vous sûr de vouloir supprimer " + filmActuel.getTitre() + " ?", "Supprimer "+filmActuel.getTitre(), JOptionPane.OK_CANCEL_OPTION);
        if (resultat == JOptionPane.OK_OPTION) {
            supprimerFilm(filmActuel.getId());
            vueModifierFilm.closeWindow();
            ControleurEmployeAccueil controleurAccueil = new ControleurEmployeAccueil(connexion);
            GUIEmployeAccueil vueAccueil = new GUIEmployeAccueil(client, controleurAccueil);
            controleurAccueil.setVue(vueAccueil);
            controleurAccueil.setMembre(client);
        }
    }

    private void ajouterSeance(ActionEvent e) {
        // Code pour ajouter une séance en utilisant une boîte de dialogue pour saisir les informations nécessaires
    }

    private void modifierSeance(ActionEvent e) {
        // Code pour modifier les détails d'une séance existante
    }

    private void supprimerFilm(int filmId) {
        // Code pour supprimer un film et ses dépendances (séances, commandes associées)
    }

    private void modifierFilm() {
        // Code pour enregistrer les modifications effectuées sur un film
    }

    private ArrayList<Seance> getSeancesForFilm(int filmId) {
        // Code pour récupérer les séances associées à un film
        return new ArrayList<>();
    }
}
