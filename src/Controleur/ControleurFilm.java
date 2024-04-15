package Controleur;

import Modele.Client;
import Modele.Film;
import Vue.GUIaccueil;
import Vue.GUIcrea;
import Vue.GUIfilm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ControleurFilm {
    private Client client;
    private GUIfilm vueFilm;
    private Connexion connexion;
    private Film filmActuel;
    private GUIaccueil vueAccueil;
    private ControleurAccueil controleurAccueil;

    public ControleurFilm(Connexion connexion, Film film, Client client) {
        this.connexion = connexion;
        this.filmActuel = filmActuel;
        this.client = client;
    }

    public void setVue(GUIfilm vue){
        this.vueFilm = vue;
        this.vueFilm.addListenerRetour(new ActionListener(){
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
    }
    public void setClient(Client client) {
        this.client = client;
    }

    public void openWindow(){
        this.vueFilm.setVisible(true);
    }
}
