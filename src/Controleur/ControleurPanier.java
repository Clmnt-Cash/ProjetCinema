package Controleur;

import Modele.*;
import Vue.GUIaccueil;
import Vue.GUIconnexion;
import Vue.GUIfilm;
import Vue.GUIpanier;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControleurPanier {
    //Attributs
    private Client client;
    private Connexion connexion;
    private GUIpanier vuePanier;
    private Reduction reduction;
    private ArrayList<Commande> commandes;
    private float prixTotal;

    //Constructeur
    public ControleurPanier(Connexion connexion, Client client, GUIpanier vuePanier) {
        this.connexion = connexion;
        this.client = client;
        this.vuePanier = vuePanier;
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
        this.commandes = getCommandes(client.getId(), reduction);
        this.vuePanier.setCommandes(this.commandes);

        for (Commande commande : commandes) {
            System.out.print("Nombre de places : " + commande.getNbPlaces());
            System.out.print("Prix avec réduction : " + commande.getPrixAvecReduc());
            System.out.print("Prix sans réduction : " + commande.getPrixSansReduc());
            System.out.print("Date de diffusion : " + commande.getSeance().getDate());
            System.out.println("Heure de diffusion : " + commande.getSeance().getHeure());
            System.out.println("-------------------------------------------");
        }
    }
    //Methode pour initialiser la vue
    public void setVue(GUIpanier vue){
        this.vuePanier = vue;
    }

    public ArrayList<Commande> getCommandes(int clientID, Reduction reduction){
        ArrayList<Commande> commandes = new ArrayList<>();

        try {
            //Récupérer les résultats de la requête SQL pour les séances du film donné
            ArrayList<String> resultatsCommande = connexion.remplirChampsRequete(
                    "SELECT s.*, c.NB_places FROM seance s JOIN commande c ON s.ID = c.ID_seance WHERE c.ID_client = " + clientID + " ORDER BY s.Date_diffusion;");
            for (String resultat : resultatsCommande) {
                String[] infosSeance = resultat.split(",");
                int id = Integer.parseInt(infosSeance[0].trim());
                String dateHeure = infosSeance[1].trim();
                String[] parties = dateHeure.split(" ");
                String date = parties[0];
                String mois = date.substring(5, 7);
                String jour = date.substring(8, 10);
                date = jour + "/" + mois;
                String heure = parties[1];
                heure = heure.substring(0, 5);

                float prix = Float.parseFloat(infosSeance[2].trim());
                int nbPlaces = Integer.parseInt(infosSeance[4].trim());

                float prixAvecReduction = prix;
                if(this.client.getType() == 1){prixAvecReduction = (float)prix*reduction.getReductionEnfant()/100;}
                else if(this.client.getType() == 2){prixAvecReduction = (float)prix*reduction.getReductionEnfant()/100;}
                else if(this.client.getType() == 3){prixAvecReduction = (float)prix*reduction.getReductionSenior()/100;}

                //Créer une séance avec les informations récupérées
                Seance seance = new Seance(date, heure, prix, id);

                //Créer une commande avec les informations récupérées
                Commande commande = new Commande(nbPlaces, seance, prixAvecReduction, prix);
                //Ajouter la séance à la liste
                commandes.add(commande);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return commandes;
    }

    public Reduction getReduction(){
        return reduction;
    }

    //Méthode pour initialiser le client
    public void setClient(Client client) {
        this.client = client;
    }
}
