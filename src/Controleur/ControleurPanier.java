package Controleur;

import Modele.*;
import Vue.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControleurPanier {
    //Attributs
    private Client client;
    private Connexion connexion;
    private GUIpanier vuePanier;
    private Reduction reduction;
    private ArrayList<Commande> commandes;

    //Constructeur
    public ControleurPanier(Connexion connexion, Client client) {
        this.connexion = connexion;
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
        this.commandes = getCommandes(client.getId(), reduction);
    }
    //Methode pour initialiser la vue
    public void setVue(GUIpanier vue){
        this.vuePanier = vue;
        //Revenir à l'accueil
        this.vuePanier.addListenerRetour(new ActionListener(){
            //Ouverture de la page menu
            @Override
            public void actionPerformed(ActionEvent e) {
                vuePanier.closeWindow();
                ControleurAccueil controleurAccueil = new ControleurAccueil(connexion);
                controleurAccueil.setClient(client);
                GUIaccueil vueAccueil = new GUIaccueil(client, controleurAccueil);
                controleurAccueil.setVue(vueAccueil);
                controleurAccueil.openWindow();
            }
        });
        //Listener sur le bouton payer
        this.vuePanier.addListenerPayer(new ActionListener(){
            //Ouverture de la page menu
            @Override
            public void actionPerformed(ActionEvent e) {
                vuePanier.closeWindow();
                float prix = vuePanier.getPrix();
                GUIpaiement vuePaiement = new GUIpaiement(prix, connexion, client);
            }
        });
        //Listener sur les bouton modifier
        this.vuePanier.addListenerModifier(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JButton bouton = (JButton) e.getSource();

                String seanceChoisie = (String) bouton.getClientProperty("infos");
                String[] infos = seanceChoisie.split(",");
                //Extraire les informations sur le film
                int idCommande = Integer.parseInt(infos[0].trim());
                String film = infos[1].trim();
                int places = Integer.parseInt(infos[2].trim());

                //Customisation de la fenetre de dialogue
                UIManager.put("OptionPane.background", Color.WHITE);
                UIManager.put("Panel.background", Color.WHITE);
                UIManager.put("OptionPane.messageForeground", Color.WHITE);
                UIManager.put("Button.background", Color.WHITE);
                UIManager.put("Button.foreground", Color.BLACK);
                UIManager.put("Button.border", BorderFactory.createLineBorder(Color.WHITE));
                UIManager.put("Button.focus", Color.WHITE);

                SpinnerNumberModel nombres = new SpinnerNumberModel(places, 1, 10, 1);

                //Création du spinner
                JSpinner spinner = new JSpinner(nombres);

                //Création de la fenêtre de dialogue
                JPanel panel = new JPanel();
                panel.add(new JLabel("Modifier le nombre de places pour " + film + " :"));
                panel.add(spinner);

                int resultat = JOptionPane.showConfirmDialog(null, panel, "Places pour " + film, JOptionPane.OK_CANCEL_OPTION);

                if (resultat == JOptionPane.OK_OPTION) {
                    //modifier la commande puis rouvrir la page
                    int nbPlaces = (int) spinner.getValue();
                    modifierCommande(idCommande, nbPlaces);
                    vuePanier.closeWindow();
                    ControleurPanier controleurPanier = new ControleurPanier(connexion, client);
                    GUIpanier vuePanier = new GUIpanier(client, controleurPanier);
                    controleurPanier.setVue(vuePanier);
                }
            }
        });
        //Listener sur les boutons supprimer
        this.vuePanier.addListenerSupprimer(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                JButton bouton = (JButton) e.getSource();

                String seanceChoisie = (String) bouton.getClientProperty("infos");
                String[] infos = seanceChoisie.split(",");
                //Extraire les informations sur le film
                int idCommande = Integer.parseInt(infos[0].trim());
                String film = infos[1].trim();

                //Customisation de la fenetre de dialogue
                UIManager.put("OptionPane.background", Color.WHITE);
                UIManager.put("Panel.background", Color.WHITE);
                UIManager.put("OptionPane.messageForeground", Color.WHITE);
                UIManager.put("Button.background", Color.WHITE);
                UIManager.put("Button.foreground", Color.BLACK);
                UIManager.put("Button.border", BorderFactory.createLineBorder(Color.WHITE));
                UIManager.put("Button.focus", Color.WHITE);


                //Création de la fenêtre de dialogue
                JPanel panel = new JPanel();
                panel.add(new JLabel("Voulez-vous vraiment annuler la commande de la séance pour " + film + " ?"));
                int resultat = JOptionPane.showConfirmDialog(null, panel, "Annuler une commande", JOptionPane.OK_CANCEL_OPTION);

                if (resultat == JOptionPane.OK_OPTION) {
                    //Supprimer la commande puis rouvrir la page
                    supprimerCommande(idCommande);
                    vuePanier.closeWindow();
                    ControleurPanier controleurPanier = new ControleurPanier(connexion, client);
                    GUIpanier vuePanier = new GUIpanier(client, controleurPanier);
                    controleurPanier.setVue(vuePanier);
                }
            }
        });
        //Se déconnecter
        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel panel = new JPanel();
                panel.add(new JLabel("Etes-vous sûr de vouloir vous déconnecter ?"));

                int resultat = JOptionPane.showConfirmDialog(null, panel, "Déconnexion", JOptionPane.OK_CANCEL_OPTION);

                if (resultat == JOptionPane.OK_OPTION) {
                    vuePanier.closeWindow();
                    GUIconnexion vueConnexion = new GUIconnexion();
                    ControleurConnexion controleurConnexion = new ControleurConnexion(vueConnexion);
                }
            }
        };
        this.vuePanier.addMouseListenerBoutonDeconnexion(mouseListener);
    }

    //Methode pour modifier le nombre de places pour une seance
    public void modifierCommande(int idCommande, int places){
        try {
            String requeteInsertion = "UPDATE commande" +
                    " SET Nb_places = " + places +
                    " WHERE ID = " + idCommande;
            connexion.executerRequete(requeteInsertion);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la mise à jour d'une commande : " + e);
        }
    }

    //Méthode pour supprimer une commande de la bdd
    public void supprimerCommande(int idCommande){
        try {
            String requeteInsertion = "DELETE FROM commande WHERE ID = " + idCommande;
            connexion.executerRequete(requeteInsertion);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la suppression d'une commande : " + e);
        }
    }

    //Méthode pour récupérer els commandes
    public ArrayList<Commande> getCommandes(int clientID, Reduction reduction){
        ArrayList<Commande> commandes = new ArrayList<>();

        try {
            //Récupérer les résultats de la requête SQL pour les séances du film donné
            ArrayList<String> resultatsCommande = connexion.remplirChampsRequete(
                    "SELECT s.*, c.NB_places, f.Titre, c.ID, C.Paye " +
                            "FROM seance s " +
                            "JOIN commande c ON s.ID = c.ID_seance " +
                            "JOIN films f ON s.ID_film = f.ID " +
                            "WHERE c.ID_client = " + clientID + " " +
                            "ORDER BY s.Date_diffusion"
            );
            for (String resultat : resultatsCommande) {
                //Récupération des données
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
                String titre = infosSeance[5].trim();
                int idCommande = Integer.parseInt(infosSeance[6].trim());
                boolean paye = Integer.parseInt(infosSeance[7].trim()) == 1;

                prix *= nbPlaces;

                float prixAvecReduction = prix;
                if(this.client.getType() == 1){ prixAvecReduction = prix - (prix* reduction.getReductionEnfant()/100);}
                else if(this.client.getType() == 2){ prixAvecReduction = prix - (prix* reduction.getReductionRegulier()/100);}
                else if(this.client.getType() == 3){ prixAvecReduction = prix - (prix* reduction.getReductionSenior()/100);}

                //Créer une séance avec les informations récupérées
                Seance seance = new Seance(date, heure, prix, id, titre);

                //Créer une commande avec les informations récupérées
                Commande commande = new Commande(nbPlaces, seance, prixAvecReduction, prix, idCommande, paye);
                //Ajouter la commande à la liste
                commandes.add(commande);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return commandes;
    }
    //Méthode pour initialiser le client
    public void setClient(Client client) {
        this.client = client;
    }
    public ArrayList<Commande> getCommandes(){
        return commandes;
    }

}
