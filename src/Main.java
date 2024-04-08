import Controleur.ControleurConnexion;
import Controleur.ControleurCrea;
import Vue.GUIprincipal;
import Vue.GUIconnexion;
import Vue.GUIcrea;


public class Main {
    public static void main(String[] args) {
        /*javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Vue.CinemaGUI cinemaGUI = new Vue.CinemaGUI();
                cinemaGUI.setVisible(true);
            }
        });*/
        //GUIconnexion vue = new GUIconnexion();
        GUIcrea vue = new GUIcrea();
        ControleurCrea controleur = new ControleurCrea(vue);

    }
}