package Controleur;

import Vue.GUIconnexion;
import Vue.GUIaccueil;
import Modele.Client;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
public class ControleurConnexion {
    private Client client;
    private GUIconnexion vueConnexion;
    private Connexion connexion;
    private ControleurAccueil controleurAccueil;
    private GUIaccueil vueAccueil;


    public ControleurConnexion(GUIconnexion vue) {
        this.vueConnexion = vue;
        try {
            connexion = new Connexion("cinema", "root", "");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        this.vueConnexion.addConnexionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = vue.getEmail();
                String motDePasse = vue.getMotDePasse();
                if (email.isEmpty() || motDePasse.isEmpty()) {
                    vue.displayError("Veuillez remplir tous les champs");
                } else if (!email.contains("@")) {
                    vue.displayError("E-mail invalide");
                } else {
                    if(handleConnexion(email, motDePasse)){
                        vue.closeWindow();
                        // Envoyer le client à ControleurAccueil
                        vueAccueil = new GUIaccueil(client);
                        controleurAccueil = new ControleurAccueil(vueAccueil);
                        controleurAccueil.setClient(client);
                        controleurAccueil.openWindow();
                    }
                }
            }
        });
    }

    /**
     *
     * @param email
     * @param motDePasse
     */
    public boolean handleConnexion(String email, String motDePasse) {
        try {
            //Requete pour vérifier si lemail existe dans la bdd
            ArrayList<String> emails = connexion.remplirChampsRequete("SELECT * FROM membre WHERE Email = '" + email + "'");
            if (!emails.isEmpty()) {
                //Vérifier si le mot de passe correspond à l'email
                ArrayList<String> resultatsMdp = connexion.remplirChampsRequete("SELECT * FROM membre WHERE Email = '" + email + "' AND Mot_de_passe = '" + motDePasse + "'");
                System.out.println(resultatsMdp);

                if (!resultatsMdp.isEmpty()) {

                    //Création du client avec toutes les infos
                    String[] infosMembre = resultatsMdp.get(0).split(",");

                    int idMembre = Integer.parseInt(infosMembre[0].trim());
                    int typeMembre = Integer.parseInt(infosMembre[1].trim());
                    String nomMembre = infosMembre[2].trim();
                    String prenomMembre = infosMembre[3].trim();
                    String emailMembre = infosMembre[4].trim();
                    String mdpMembre = infosMembre[5].trim();
                    client = new Client(idMembre, typeMembre, nomMembre, prenomMembre, emailMembre, mdpMembre);
                    return true;
                } else {
                    //Mot de passe incorrect
                    vueConnexion.displayError("Mot de passe incorrect");
                    return false;
                }
            } else {
                //Email non trouvé dans la bdd
                vueConnexion.displayError("Cet e-mail n'est associé à aucun compte");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la connexion à la base de données : " + e);
            return false;
        }
    }
}
