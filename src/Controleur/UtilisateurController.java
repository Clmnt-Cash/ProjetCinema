package Controleur;

import Modele.Utilisateur;
import java.util.List;
import java.util.ArrayList;

public class UtilisateurController {
    // Méthodes pour interagir avec la base de données et manipuler les objets Utilisateur

    // Simulation d'une base de données d'utilisateurs
    private List<Utilisateur> utilisateurs = new ArrayList<>();

    public List<Utilisateur> getAllUtilisateurs() {
        // Logique pour récupérer tous les utilisateurs depuis la base de données
        // Retourne une liste d'objets Utilisateur
        return utilisateurs;
    }

    public void addUtilisateur(Utilisateur utilisateur) {
        // Logique pour ajouter un utilisateur à la base de données
        utilisateurs.add(utilisateur);
    }

    public void updateUtilisateur(Utilisateur updatedUtilisateur) {
        // Logique pour mettre à jour les détails d'un utilisateur dans la base de données
        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.getId() == updatedUtilisateur.getId()) {
                utilisateur.setType(updatedUtilisateur.getType());
                utilisateur.setNom(updatedUtilisateur.getNom());
                utilisateur.setPrenom(updatedUtilisateur.getPrenom());
                utilisateur.setEmail(updatedUtilisateur.getEmail());
                utilisateur.setMotDePasse(updatedUtilisateur.getMotDePasse());
                break;
            }
        }
    }

    public void deleteUtilisateur(int utilisateurId) {
        // Logique pour supprimer un utilisateur de la base de données
        utilisateurs.removeIf(utilisateur -> utilisateur.getId() == utilisateurId);
    }
}

