package Modele;

import java.util.ArrayList;

public class FilmParAchat {
    //attributs
    private String titre;
    private int nbCommandes;
    // Constructeur
    public FilmParAchat(String titre, int nb) {
        this.titre = titre;
        this.nbCommandes = nb;
    }

    //Getters
    public String getTitre(){return titre;}
    public int getNbCommandes(){return nbCommandes;}
}