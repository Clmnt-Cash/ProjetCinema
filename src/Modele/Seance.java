package Modele;

import java.util.Date;

public class Seance {
    //Attributs
    private String date;
    private int prix;
    private String heure;


    //Constructeur
    public Seance(String date, String heure, int prix) {
        this.date = date;
        this.heure = heure;
        this.prix = prix;
    }

    //Getters
    public String getDate(){
        return date;
    }
    public int getPrix(){
        return prix;
    }
    public String getHeure(){
        return heure;
    }
}
