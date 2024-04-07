public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                Vue.CinemaGUI cinemaGUI = new Vue.CinemaGUI();
                cinemaGUI.setVisible(true);
            }
        });
    }
}