package Controleur;

import Modele.Client;
import Modele.Film;
import Modele.Reduction;
import Modele.Seance;
import Vue.GUIaccueil;
import Vue.GUIconnexion;
import Vue.GUIfilm;
import Vue.GUIpaiement;

import javax.swing.*;
import java.awt.*;
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

        //Retourneer sur l'accueil
        this.vueFilm.addListenerRetour(new ActionListener(){
            //Ouverture de la page menu
            @Override
            public void actionPerformed(ActionEvent e) {
                vueFilm.closeWindow();
                controleurAccueil = new ControleurAccueil(connexion);
                controleurAccueil.setClient(client);
                vueAccueil = new GUIaccueil(client, controleurAccueil);
                controleurAccueil.setVue(vueAccueil);
                controleurAccueil.openWindow();
            }
        });

        //Choisir une séance
        this.vueFilm.addListenerSeance(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JButton bouton = (JButton) e.getSource();

                //Récupération du contenu des JLabels ajoutes au bouton
                JLabel labelHeure = (JLabel) bouton.getComponent(0);
                JLabel labelPrix = (JLabel) bouton.getComponent(1);
                String seanceChoisie = (String) bouton.getClientProperty("seance");
                String[] infos = seanceChoisie.split(",");
                //Extraire les informations sur le film
                int id = Integer.parseInt(infos[0].trim());
                String date = infos[1].trim();

                //Récupération du texte des JLabels
                String heure = labelHeure.getText();
                String prix = labelPrix.getText().trim();

                //Customisation de la fenetre de dialogue
                UIManager.put("OptionPane.background", Color.WHITE);
                UIManager.put("Panel.background", Color.WHITE);
                UIManager.put("OptionPane.messageForeground", Color.WHITE);
                UIManager.put("Button.background", Color.WHITE);
                UIManager.put("Button.foreground", Color.BLACK);
                UIManager.put("Button.border", BorderFactory.createLineBorder(Color.WHITE));
                UIManager.put("Button.focus", Color.WHITE);

                SpinnerNumberModel nombres = new SpinnerNumberModel(1, 1, 10, 1);

                //Création du spinner
                JSpinner spinner = new JSpinner(nombres);
                //Création de la fenêtre de dialogue
                JPanel panel = new JPanel();
                panel.add(new JLabel("Choisissez le nombre de places pour la séance du " + date + " à " + heure + " :"));
                panel.add(spinner);

                int resultat = JOptionPane.showConfirmDialog(null, panel, "Places pour " + filmActuel.getTitre(), JOptionPane.OK_CANCEL_OPTION);

                if (resultat == JOptionPane.OK_OPTION) {
                    int nbPlaces = (int) spinner.getValue();
                    //Si le client est un membre, ajouter sa séance au panier
                    if(client.getType() != -1)ajouterPanier(id, nbPlaces);
                    //Sinon, payer directement
                    else{
                        vueFilm.closeWindow();
                        //Supprimer le symbole "€"
                        String prixSansEuro = prix.substring(0, prix.length() - 1);
                        //Convertir la chaîne en float
                        float prixTot = Float.parseFloat(prixSansEuro) * nbPlaces;
                        GUIpaiement vuePaiement = new GUIpaiement(prixTot, connexion, client);
                    }
                }
            }
        });
    }
    //Méthode pour ajouter une commande dans la table commande
    public void ajouterPanier(int IDseance, int nbPlaces){
        try {
            String requeteInsertion = "INSERT INTO commande (ID_seance, ID_client, Nb_places, Paye) VALUES ('" + IDseance + "','"+ client.getId() + "','" + nbPlaces + "', 0)";
            connexion.executerRequete(requeteInsertion);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Getter pour récupérer les réductions
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
