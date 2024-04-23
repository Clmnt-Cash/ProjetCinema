package Controleur;

import Modele.Client;
import Modele.Film;
import Modele.Seance;
import Vue.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControleurComptes {
    private Client membre;
    private GUIcomptes vueComptes;
    private Connexion connexion;
    private ArrayList<Client> comptes;

    public ControleurComptes(Connexion connexion) {
        this.connexion = connexion;
    }

    public void setVue(GUIcomptes vue) {
        this.vueComptes = vue;
        this.comptes = this.getComptes();

        configurerListeners();
    }

    private void configurerListeners() {
        // Listener pour naviguer vers d'autres onglets
        this.vueComptes.addListenerOngletFilms(e -> naviguerVersFilms());
        this.vueComptes.addListenerOngletStat(e -> naviguerVersStatistiques());
        this.vueComptes.addListenerOngletReduc(e -> naviguerVersReductions());

        // Listener pour modifier et supprimer des comptes
        this.vueComptes.addListenersModifier(this::modifierCompte);
        this.vueComptes.addListenersSupprimer(this::supprimerCompte);
    }

    private void deconnecter() {
        vueComptes.closeWindow();
        new GUIconnexion().setVisible(true);
    }

    private void naviguerVersFilms() {
        vueComptes.closeWindow();
        GUIEmployeAccueil vueEmployeAccueil = new GUIEmployeAccueil(membre, new ControleurEmployeAccueil(connexion));
        vueEmployeAccueil.setVisible(true);
    }

    private void naviguerVersStatistiques() {
        vueComptes.closeWindow();
        GUIstatistiques vueStat = new GUIstatistiques(membre, new ControleurStatistiques(connexion));
        vueStat.setVisible(true);
    }

    private void naviguerVersReductions() {
        vueComptes.closeWindow();
        GUIreduction vueReduction = new GUIreduction(membre, new ControleurReduction(connexion));
        vueReduction.setVisible(true);
    }

    private void modifierCompte(ActionEvent e) {
        JButton bouton = (JButton) e.getSource();
        Client clientModifie = (Client) bouton.getClientProperty("client");
        afficherDialogueModification(clientModifie);
    }

    private void supprimerCompte(ActionEvent e) {
        JButton bouton = (JButton) e.getSource();
        Client clientSupprime = (Client) bouton.getClientProperty("client");
        if (confirmerSuppression(clientSupprime)) {
            supprimerClient(clientSupprime.getId());
        }
    }

    private boolean confirmerSuppression(Client client) {
        return JOptionPane.showConfirmDialog(null, "Confirmez-vous la suppression du compte de " + client.getNom() + " ?", "Suppression", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    private void afficherDialogueModification(Client client) {
        // Création et configuration du JPanel pour la modification
        JPanel panel = new JPanel();
        JTextField textFieldNom = new JTextField(client.getNom(), 10);
        JTextField textFieldPrenom = new JTextField(client.getPrenom(), 10);
        // Ajoute les composants au panel
        panel.add(new JLabel("Nom:"));
        panel.add(textFieldNom);
        panel.add(new JLabel("Prénom:"));
        panel.add(textFieldPrenom);
        // Affiche le dialogue
        if (JOptionPane.showConfirmDialog(null, panel, "Modifier le compte", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            client.setNom(textFieldNom.getText());
            client.setPrenom(textFieldPrenom.getText());
            modifierClient(client);  // Appel à la méthode qui effectue la modification dans la base de données
        }
    }

    public void modifierClient(Client client) {
        try {
            String requeteUpdate = "UPDATE membre SET Nom = '" + client.getNom() + "', Prenom = '" + client.getPrenom() + "' WHERE ID = " + client.getId();
            connexion.executerRequete(requeteUpdate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void supprimerClient(int id) {
        try {
            String requeteDelete = "DELETE FROM membre WHERE ID = " + id;
            connexion.executerRequete(requeteDelete);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Client> getComptes() {
        ArrayList<Client> comptes = new ArrayList<>();
        try {
            ArrayList<String> resultats = connexion.remplirChampsRequete("SELECT ID, Nom, Prenom FROM membre");
            for (String resultat : resultats) {
                String[] data = resultat.split(",");
                int id = Integer.parseInt(data[0].trim());
                String nom = data[1].trim();
                String prenom = data[2].trim();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return comptes;
    }
}
