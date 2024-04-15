import Controleur.ControleurConnexion;
import Vue.GUIconnexion;


public class Main {
    public static void main(String[] args) {

        GUIconnexion vue = new GUIconnexion();
        ControleurConnexion controleur = new ControleurConnexion(vue);
    }
}