package Controleur;

import Modele.Client;
import Modele.Film;
import Modele.Reduction;
import Modele.Seance;
import Vue.GUIaccueil;
import Vue.GUIfilm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControleurFilm {
    //Attributs
    private Client client;
    private GUIfilm vueFilm;
    private Connexion connexion;
    private Film filmActuel;
    private GUIaccueil vueAccueil;
    private ControleurAccueil controleurAccueil;
    private Reduction reduction;

    //Constructeur
    public ControleurFilm(Connexion connexion, Film film, Client client) {
        this.connexion = connexion;
        this.filmActuel = film;
        this.client = client;
        try {
            //Recuperer les réductions
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
    //Methode pour initialiser la vue
    public void setVue(GUIfilm vue){
        this.vueFilm = vue;

        this.vueFilm.addListenerRetour(new ActionListener(){
            //Ouverture de la page menu
            @Override
            public void actionPerformed(ActionEvent e) {
                vueFilm.closeWindow();
                controleurAccueil = new ControleurAccueil(connexion);
                vueAccueil = new GUIaccueil(client, controleurAccueil);
                controleurAccueil.setVue(vueAccueil);
                controleurAccueil.setClient(client);
                controleurAccueil.openWindow();

            }
        });
        this.vueFilm.addListenerSeance(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JButton buttonClicked = (JButton) e.getSource();

                //Récupération du contenu des JLabels ajoutes au bouton
                JLabel labelDate = (JLabel) buttonClicked.getComponent(0);
                JLabel labelHeure = (JLabel) buttonClicked.getComponent(1);
                JLabel labelPrix = (JLabel) buttonClicked.getComponent(2);

                //Récupération du texte des JLabels
                String date = labelDate.getText();
                String heure = labelHeure.getText();
                String prix = labelPrix.getText().trim();

                //Création de la boite de dialogue pour confirmer l'ajout au panier
                int option = JOptionPane.showConfirmDialog(null, "Ajouter la séance de " + filmActuel.getTitre() + " le " + date + " au panier ?", "Confirmation", JOptionPane.YES_NO_OPTION);

                if (option == JOptionPane.YES_OPTION) {

                } else {
                    // Ajoutez ici le code pour ne pas ajouter la séance au panier
                }
            }


        });
    }

    public void ajouterPanier(){

    }

    public Reduction getReduction(){
        return reduction;
    }

    //Méthode pour initialiser le client
    public void setClient(Client client) {
        this.client = client;
    }

    //Méthode pour ouvrir la fenetre
    public void openWindow(){
        this.vueFilm.setVisible(true);
    }
}
