package Controleur;

import Modele.Client;
import Vue.GUIaccueil;

public class ControleurAccueil {
    private Client client;
    private GUIaccueil vueAccueil;
    public ControleurAccueil(GUIaccueil vueAccueil){
        this.vueAccueil=vueAccueil;
    }

    public void setClient(Client client) {
        this.client = client;
        System.out.println(client.getPrenom());
    }

    public void openWindow(){
        this.vueAccueil.setVisible(true);
    }

}
