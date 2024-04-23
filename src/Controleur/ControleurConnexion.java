package Controleur;

import Vue.*;
import Modele.Client;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControleurConnexion {
    private Client client;
    private GUIconnexion vueConnexion;
    private GUIcrea vueCrea;
    private Connexion connexion;
    private ControleurAccueil controleurAccueil;
    private ControleurCrea controleurCrea;
    private GUIaccueil vueAccueil;

    public ControleurConnexion(GUIconnexion vue) {
        this.vueConnexion = vue;
        try {
            connexion = new Connexion("cinema", "root", "Garcon1?");  // Initialisation de la connexion à la base de données
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // Écouteur pour le bouton de connexion
        this.vueConnexion.addConnexionListener(e -> verifierConnexion());

        // Écouteur pour la connexion en tant qu'invité
        this.vueConnexion.addListenerInvite(e -> connexionInvite());

        // Écouteur pour la création d'un compte
        this.vueConnexion.addCreationListener(e -> creerCompte());
    }

    private void verifierConnexion() {
        String email = vueConnexion.getEmail();
        String motDePasse = vueConnexion.getMotDePasse();
        if (email.isEmpty() || motDePasse.isEmpty()) {
            vueConnexion.displayError("Veuillez remplir tous les champs");
        } else if (!email.contains("@")) {
            vueConnexion.displayError("E-mail invalide");
        } else {
            if (handleConnexion(email, motDePasse)) {
                vueConnexion.closeWindow();
                if (client.getType() == 0) {
                    ouvrirAccueilEmploye();
                } else {
                    ouvrirAccueilClient();
                }
            }
        }
    }

    private void connexionInvite() {
        Client invite = new Client(0, -1, "", "", "", "");
        vueConnexion.closeWindow();
        controleurAccueil = new ControleurAccueil(connexion);
        vueAccueil = new GUIaccueil(invite, controleurAccueil);
        controleurAccueil.setVue(vueAccueil);
        controleurAccueil.setClient(invite);
        controleurAccueil.openWindow();
    }

    private void creerCompte() {
        vueConnexion.closeWindow();
        vueCrea = new GUIcrea();
        controleurCrea = new ControleurCrea(vueCrea, connexion);
        controleurCrea.openWindow();
    }

    private boolean handleConnexion(String email, String motDePasse) {
        try {
            ArrayList<String> resultatsMdp = connexion.remplirChampsRequete(
                    "SELECT * FROM membre WHERE Email = '" + email + "' AND Mot_de_passe = '" + motDePasse + "'"
            );
            if (!resultatsMdp.isEmpty()) {
                String[] infosMembre = resultatsMdp.get(0).split(",");
                client = new Client(
                        Integer.parseInt(infosMembre[0].trim()),
                        Integer.parseInt(infosMembre[1].trim()),
                        infosMembre[2].trim(),
                        infosMembre[3].trim(),
                        infosMembre[4].trim(),
                        infosMembre[5].trim()
                );
                return true;
            } else {
                vueConnexion.displayError("Mot de passe incorrect ou compte inexistant");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            vueConnexion.displayError("Erreur lors de la connexion à la base de données : " + e);
            return false;
        }
    }

    private void ouvrirAccueilEmploye() {
        ControleurEmployeAccueil controleurEmployeAccueil = new ControleurEmployeAccueil(connexion);
        GUIEmployeAccueil vueEmploye = new GUIEmployeAccueil(client, controleurEmployeAccueil);
        controleurEmployeAccueil.setVue(vueEmploye);
        controleurEmployeAccueil.setMembre(client);
        vueEmploye.setVisible(true);
    }

    private void ouvrirAccueilClient() {
        controleurAccueil = new ControleurAccueil(connexion);
        controleurAccueil.setClient(client);
        vueAccueil = new GUIaccueil(client, controleurAccueil);
        controleurAccueil.setVue(vueAccueil);
        controleurAccueil.openWindow();
    }

    public void openWindow() {
        this.vueConnexion.setVisible(true);
    }
}
