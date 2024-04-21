package Controleur;

import Vue.GUIaccueil;
import Vue.GUIconnexion;
import Vue.GUIcrea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControleurCrea {
    // Attributs pour gérer les interfaces utilisateur et la connexion à la base de données
    private GUIcrea vue;
    private GUIconnexion vueCo;
    private ControleurConnexion coCo;
    private Connexion connexion;

    // Constructeur initialisant le contrôleur avec la vue et la connexion
    public ControleurCrea(GUIcrea vue, Connexion connexion) {
        this.vue = vue;
        this.connexion = connexion;

        // Ajout d'un écouteur pour le bouton de création de compte
        this.vue.addConnexionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Récupération des informations saisies par l'utilisateur
                String nom = vue.getNom();
                String prenom = vue.getPrenom();
                Integer type = 0;  // Par défaut, non spécifié
                int age = vue.getAge();
                String email = vue.getEmail();
                String motDePasse = vue.getMotDePasse();
                String motDePasseConf = vue.getMotDePasseConf();

                // Validation des champs
                if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || motDePasse.isEmpty() || motDePasseConf.isEmpty()) {
                    vue.displayError("Veuillez remplir tous les champs");
                } else if (age == 0) {
                    vue.displayError("Veuillez spécifier votre âge");
                } else if (!email.contains("@")) {
                    vue.displayError("E-mail invalide");
                } else if (!motDePasse.equals(motDePasseConf)) {
                    vue.displayError("Les mots de passe ne sont pas identiques");
                } else {
                    // Assignation du type de membre selon l'âge
                    if (age < 18) {
                        type = 1;  // Enfant
                    } else if (age > 60) {
                        type = 3;  // Senior
                    } else {
                        type = 2;  // Adulte
                    }
                    // Traitement de la création du compte
                    handleCreation(type, nom, prenom, email, motDePasse);
                }
            }
        });
    }

    // Affichage de la fenêtre
    public void openWindow() {
        this.vue.setVisible(true);
    }

    // Gestion de la création d'un compte utilisateur
    private void handleCreation(Integer type, String Nom, String Prenom, String email, String motDePasse) {
        try {
            // Vérification de l'existence de l'email dans la base de données
            ArrayList<String> emails = connexion.remplirChampsRequete("SELECT * FROM membre WHERE Email = '" + email + "'");
            if (emails.isEmpty()) {
                // Insertion du nouveau membre dans la base de données
                String requeteInsertion = "INSERT INTO membre (Type, Nom, Prenom, Email, Mot_De_Passe) VALUES ('" + type + "', '" + Nom + "', '" + Prenom + "', '" + email + "', '" + motDePasse + "')";
                connexion.executerRequete(requeteInsertion);
                vue.displayError("Compte créé avec succès !");
                // Retour à la page de connexion
                transitionToConnexion();
            } else {
                vue.displayError("Cet e-mail est déjà utilisé par un autre compte.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            vue.displayError("Erreur lors de l'inscription : " + e.getMessage());
        }
    }

    // Transition vers la page de connexion après la création d'un compte
    private void transitionToConnexion() {
        vue.closeWindow();
        vueCo = new GUIconnexion();
        coCo = new ControleurConnexion(vueCo);
        coCo.openWindow();
    }
}
