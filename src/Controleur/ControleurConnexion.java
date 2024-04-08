package Controleur;

import Vue.GUIconnexion;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControleurConnexion implements ControleurInterface{
    //Attributs
    private GUIconnexion vue;
    private Connexion connexion;

    //Constructeur
    public ControleurConnexion(GUIconnexion vue) {
        this.vue = vue;
        try {
            connexion = new Connexion("cinema", "root", "");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        //Si l utilisateur clique sur créer, obtneir l email et le mdp
        this.vue.addConnexionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = vue.getEmail();
                String motDePasse = vue.getMotDePasse();
                if(email.isEmpty() || motDePasse.isEmpty()) {
                    vue.displayError("Veuillez remplir tous les champs");
                }
                else if(!email.contains("@")){
                    vue.displayError("E-mail invalide");
                }else{
                    handleConnexion(email, motDePasse);
                }

            }
        });
    }

    @Override
    public void handleConnexion(String email, String motDePasse) {
        try {
            //Requete pour vérifier si lemail existe dans la bdd
            ArrayList<String> emails = connexion.remplirChampsRequete("SELECT * FROM membre WHERE Email = '" + email + "'");
            if (!emails.isEmpty()) {
                //Vérifier si le mot de passe correspond à l'email
                ArrayList<String> resultatsMdp = connexion.remplirChampsRequete("SELECT * FROM membre WHERE Email = '" + email + "' AND Mot_de_passe = '" + motDePasse + "'");
                if (!resultatsMdp.isEmpty()) {
                    System.out.println("Connexion réussie !");
                } else {
                    //Mot de passe incorrect
                    vue.displayError("Mot de passe incorrect");
                }
            } else {
                //Email non trouvé dans la bdd
                vue.displayError("Cet e-mail n'est associé à aucun compte");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la connexion à la base de données : " + e);
        }
    }
}
