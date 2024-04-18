package Controleur;

import Modele.Client;
import Modele.Film;
import Modele.Seance;
import Vue.GUIEmployeAccueil;
import Vue.GUIcomptes;
import Vue.GUIfilm;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ControleurComptes {
    private Client membre;
    private GUIcomptes vueComptes;
    private Connexion connexion;
    private ArrayList<Client> comptes;
    private Film filmActuel;
    private ControleurFilm controleurFilm;
    private ControleurEmployeAccueil controleurEmployeAccueil;
    private GUIfilm vueFilm;
    private GUIEmployeAccueil vueEmployeAccueil;

    public ControleurComptes(Connexion connexion) {
        this.connexion = connexion;
    }

    public void setVue(GUIcomptes vue){
        this.vueComptes = vue;
        this.comptes = this.getComptes();

        //Aller sur la page des films
        this.vueComptes.addListenerOngletFilms(new ActionListener(){
            //Ouverture de la page menu
            @Override
            public void actionPerformed(ActionEvent e) {
                vueComptes.closeWindow();
                controleurEmployeAccueil = new ControleurEmployeAccueil(connexion);
                controleurEmployeAccueil.setMembre(membre);
                vueEmployeAccueil = new GUIEmployeAccueil(membre, controleurEmployeAccueil);
                controleurEmployeAccueil.setVue(vueEmployeAccueil);
                controleurEmployeAccueil.openWindow();
            }
        });
    }
    public void setMembre(Client membre) {
        this.membre = membre;
    }

    public void openWindow(){
        this.vueComptes.setVisible(true);
    }

    public ArrayList<Client> getComptes(){
        ArrayList<Client> comptes = new ArrayList<>();

        try {
            ArrayList<String> resultatsComptes = connexion.remplirChampsRequete("SELECT * FROM membre");

            for (String resultat : resultatsComptes) {
                String[] infosCompte = resultat.split(",");
                //Extraire les informations sur le compte
                int id = Integer.parseInt(infosCompte[0].trim());
                int type = Integer.parseInt(infosCompte[1].trim());
                String nom = infosCompte[2].trim();
                String prenom = infosCompte[3].trim();
                String mail = infosCompte[4].trim();
                String mdp = infosCompte[5].trim();

                Client compte = new Client(id, type, nom, prenom, mail, mdp);
                comptes.add(compte);
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return comptes;
    }
}
