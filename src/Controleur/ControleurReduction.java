package Controleur;

import Modele.Client;
import Modele.Reduction;
import Vue.*;

import javax.swing.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControleurReduction {
    private Client membre;
    private GUIreduction vueReduction;
    private Connexion connexion;
    private ControleurEmployeAccueil controleurEmployeAccueil;
    private GUIEmployeAccueil vueEmployeAccueil;
    public ControleurReduction(Connexion connexion) {
        this.connexion = connexion;
    }

    public void setVue(GUIreduction vue) {
        this.vueReduction = vue;
        //Se déconnecter
        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel panel = new JPanel();
                panel.add(new JLabel("Etes-vous sûr de vouloir vous déconnecter ?"));

                int resultat = JOptionPane.showConfirmDialog(null, panel, "Déconnexion", JOptionPane.OK_CANCEL_OPTION);

                if (resultat == JOptionPane.OK_OPTION) {
                    vueReduction.closeWindow();
                    GUIconnexion vueConnexion = new GUIconnexion();
                    ControleurConnexion controleurConnexion = new ControleurConnexion(vueConnexion);
                }
            }
        };
        this.vueReduction.addMouseListenerBoutonDeconnexion(mouseListener);

        //Aller sur la page des films
        this.vueReduction.addListenerOngletFilms(e -> {
            vueReduction.closeWindow();
            controleurEmployeAccueil = new ControleurEmployeAccueil(connexion);
            controleurEmployeAccueil.setMembre(membre);
            vueEmployeAccueil = new GUIEmployeAccueil(membre, controleurEmployeAccueil);
            controleurEmployeAccueil.setVue(vueEmployeAccueil);
            controleurEmployeAccueil.openWindow();
        });
        //Aller sur la page dex comptes
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

        //Aller sur la page des statistiques
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
        //Modifier les réductions
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
    public void setMembre(Client membre) {
        this.membre = membre;
    }

    public void openWindow(){
        this.vueReduction.setVisible(true);
    }

    //Mathode pour modifier les réductions dans la bdd
    public void modifierReduction(int en, int r, int s){
        try {
            String requeteInsertion = "UPDATE reduction SET enfant = " + en + ", regulier = " + r + ", senior = " + s ;
            connexion.executerRequete(requeteInsertion);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Méthode pour récupérer les réductions
    public Reduction getReduction(){
        Reduction red = new Reduction(0, 0, 0);
        try {
            ArrayList<String> resultatReduc = connexion.remplirChampsRequete("SELECT * FROM reduction");

            for (String resultat : resultatReduc) {
                String[] infosReduc = resultat.split(",");
                //Extraire les informations sur le compte
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
