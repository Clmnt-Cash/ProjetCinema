package Controleur;

import Modele.Client;
import Modele.Reduction;
import Vue.*;

import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControleurReduction {
    private Client membre; // Le client associé au contrôleur
    private GUIreduction vueReduction; // Interface utilisateur pour les réductions
    private Connexion connexion; // Connexion à la base de données
    private ControleurEmployeAccueil controleurEmployeAccueil; // Contrôleur pour l'accueil des employés
    private GUIEmployeAccueil vueEmployeAccueil; // Interface graphique pour l'accueil des employés

    // Constructeur pour initialiser la connexion à la base de données
    public ControleurReduction(Connexion connexion) {
        this.connexion = connexion;
    }

    // Configuration de l'interface utilisateur et ajout des écouteurs d'événements
    public void setVue(GUIreduction vue) {
        this.vueReduction = vue;

        // Écouteur pour le bouton de déconnexion
        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel panel = new JPanel();
                panel.add(new JLabel("Etes-vous sûr de vouloir vous déconnecter ?"));
                int resultat = JOptionPane.showConfirmDialog(null, panel, "Déconnexion", JOptionPane.OK_CANCEL_OPTION);
                if (resultat == JOptionPane.OK_OPTION) {
                    vueReduction.closeWindow();
                    new GUIconnexion();
                    new ControleurConnexion(new GUIconnexion());
                }
            }
        };
        this.vueReduction.addMouseListenerBoutonDeconnexion(mouseListener);

        // Écouteurs pour naviguer entre les différentes pages de l'application
        this.vueReduction.addListenerOngletFilms(e -> {
            vueReduction.closeWindow();
            controleurEmployeAccueil = new ControleurEmployeAccueil(connexion);
            controleurEmployeAccueil.setMembre(membre);
            vueEmployeAccueil = new GUIEmployeAccueil(membre, controleurEmployeAccueil);
            controleurEmployeAccueil.setVue(vueEmployeAccueil);
            controleurEmployeAccueil.openWindow();
        });

        this.vueReduction.addListenerOngletComptes(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                vueReduction.closeWindow();
                ControleurComptes controleurComptes = new ControleurComptes(connexion);
                controleurComptes.setMembre(membre);
                GUIcomptes vueComptes = new GUIcomptes(membre, controleurComptes);
                controleurComptes.setVue(vueComptes);
                controleurComptes.openWindow();
            }
        });

        this.vueReduction.addListenerOngletStat(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                vueReduction.closeWindow();
                ControleurStatistiques controleurStatistiques = new ControleurStatistiques(connexion);
                GUIstatistiques vueStat = new GUIstatistiques(membre, controleurStatistiques);
                controleurStatistiques.setVue(vueStat);
                controleurStatistiques.setMembre(membre);
            }
        });

        // Enregistrement des nouvelles réductions
        this.vueReduction.addListenerEnregistrer(e -> {
            Reduction newReduc = vueReduction.getNewReduction();
            modifierReduction(newReduc.getReductionEnfant(), newReduc.getReductionRegulier(), newReduc.getReductionSenior());
            vueReduction.closeWindow();
            ControleurReduction controleurReduction = new ControleurReduction(connexion);
            GUIreduction vueReduction = new GUIreduction(membre, controleurReduction);
            controleurReduction.setVue(vueReduction);
            controleurReduction.setMembre(membre);
        });
    }

    // Assigner le membre client au contrôleur
    public void setMembre(Client membre) {
        this.membre = membre;
    }

    // Méthode pour ouvrir la fenêtre de réduction
    public void openWindow(){
        this.vueReduction.setVisible(true);
    }

    // Modifier les réductions dans la base de données
    public void modifierReduction(int en, int r, int s){
        try {
            String requeteInsertion = "UPDATE reduction SET enfant = " + en + ", regulier = " + r + ", senior = " + s;
            connexion.executerRequete(requeteInsertion);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Récupérer les réductions existantes
    public Reduction getReduction(){
        Reduction red = new Reduction(0, 0, 0);
        try {
            ArrayList<String> resultatReduc = connexion.remplirChampsRequete("SELECT * FROM reduction");
            for (String resultat : resultatReduc) {
                String[] infosReduc = resultat.split(",");
                int e = Integer.parseInt(infosReduc[0].trim());
                int r = Integer.parseInt(infosReduc[1].trim());
                int s = Integer.parseInt(infosReduc[2].trim());
                Reduction reduc = new Reduction(e, r, s);
                return reduc;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return red;
    }
}
