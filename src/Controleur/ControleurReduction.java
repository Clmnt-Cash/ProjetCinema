package Controleur;

import Modele.Client;
import Modele.Film;
import Modele.Reduction;
import Modele.Seance;
import Vue.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

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
        //Aller sur la page des films
        this.vueReduction.addListenerOngletFilms(new ActionListener() {
            //Ouverture de la page menu
            @Override
            public void actionPerformed(ActionEvent e) {
                vueReduction.closeWindow();
                controleurEmployeAccueil = new ControleurEmployeAccueil(connexion);
                controleurEmployeAccueil.setMembre(membre);
                vueEmployeAccueil = new GUIEmployeAccueil(membre, controleurEmployeAccueil);
                controleurEmployeAccueil.setVue(vueEmployeAccueil);
                controleurEmployeAccueil.openWindow();
            }
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

    public void modifierReduction(int en, int r, int s){
        try {
            String requeteInsertion = "UPDATE reduction SET enfant = " + en + ", regulier = " + r + ", senior = " + s ;
            connexion.executerRequete(requeteInsertion);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

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
