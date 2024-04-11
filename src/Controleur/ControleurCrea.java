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

public class ControleurCrea{
    //Attributs
    private GUIcrea vue;
    private GUIconnexion vueCo;
    private ControleurConnexion coCo;
    private Connexion connexion;

    //Constructeur
    public ControleurCrea(GUIcrea vue) {
        this.vue = vue;
        try {
            connexion = new Connexion("cinema", "root", "C.start08202003");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        //Si l utilisateur clique sur créer, obtneir l email et le mdp
        this.vue.addConnexionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nom = vue.getNom();
                String prenom = vue.getPrenom();
                Integer type = 0;
                String age = vue.getAge();
                String email = vue.getEmail();
                String motDePasse = vue.getMotDePasse();
                String motDePasseConf = vue.getMotDePasseConf();
                if(Integer.parseInt(age) < 18){
                    type = 1;
                }else if(Integer.parseInt(age) > 60){
                    type = 3;
                }else{
                    type = 2;
                }
                if(nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || motDePasse.isEmpty() || motDePasseConf.isEmpty()) {
                    vue.displayError("Veuillez remplir tous les champs");
                }else if(!email.contains("@")){
                    vue.displayError("E-mail invalide");
                }else if(!motDePasse.equals(motDePasseConf)) {
                    vue.displayError("les mots de passes ne sont pas identiques");
                }else{
                    handleCreation(type, nom, prenom, email, motDePasse);
                }

            }
        });
    }

    public void openWindow(){
        this.vue.setVisible(true);
    }

    public void handleCreation(Integer type, String Nom, String Prenom, String email, String motDePasse) {
        try {
            //Requete pour vérifier si lemail existe dans la bdd
            ArrayList<String> emails = connexion.remplirChampsRequete("SELECT * FROM membre WHERE Email = '" + email + "'");
            if (emails.isEmpty()) {
                //Insérer dans la bdd
                String requeteInsertion = "INSERT INTO membre (Type, Nom, Prenom, Email, Mot_De_Passe) " +
                        "VALUES ('" + type + "', '" + Nom + "', '" + Prenom + "', '" + email + "', '" + motDePasse + "')";
                connexion.executerRequete(requeteInsertion); // Exécute la requête d'insertion
                // Confirmation de la création du compte
                vue.displayError("Compte créé avec succès !");
                //retour page connexion :
                vue.closeWindow();
                //retour page connexion
                vueCo = new GUIconnexion();
                coCo = new ControleurConnexion(vueCo);
                coCo.openWindow();
            } else {
                //Email non trouvé dans la bdd
                vue.displayError("Cet e-mail est déjà associé à aucun compte");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la connexion à la base de données : " + e);
        }
    }
}
