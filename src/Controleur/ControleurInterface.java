package Controleur;

public interface ControleurInterface {
    void handleConnexion(String email, String motDePasse);

    void handleCreation(String Nom, String Prenom, String email, String motDePasse);

}
