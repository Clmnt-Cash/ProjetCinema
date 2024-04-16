import Controleur.ControleurConnexion;
import Vue.GUIconnexion;
import Vue.GUIpaiement;


public class Main {
    public static void main(String[] args) {
        GUIconnexion vue = new GUIconnexion();
        ControleurConnexion controleur = new ControleurConnexion(vue);
    }
}