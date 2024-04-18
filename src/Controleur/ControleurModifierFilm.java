package Controleur;

import Modele.Client;
import Modele.Film;
import Modele.Reduction;
import Modele.Seance;
import Vue.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControleurModifierFilm {
    //Attributs
    private Client client;
    private GUIfilm vueFilm;
    private Connexion connexion;
    private Film filmActuel;
    private GUIaccueil vueAccueil;
    private ControleurAccueil controleurAccueil;

    //Constructeur
    public ControleurModifierFilm(Connexion connexion, Film film, Client client) {
        this.connexion = connexion;
        this.filmActuel = film;
        this.client = client;
    }
    //Methode pour initialiser la vue
    public void setVue(GUIfilm vue){
        this.vueFilm = vue;

        this.vueFilm.addListenerRetour(new ActionListener(){
            //Ouverture de la page menu
            @Override
            public void actionPerformed(ActionEvent e) {
                vueFilm.closeWindow();
                ControleurEmployeAccueil controleurEmployeAccueil = new ControleurEmployeAccueil(connexion);
                GUIEmployeAccueil vueEmployeAccueil= new GUIEmployeAccueil(client, controleurEmployeAccueil);
                controleurEmployeAccueil.setVue(vueEmployeAccueil);
                controleurEmployeAccueil.setMembre(client);
                controleurEmployeAccueil.openWindow();
            }
        });
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
