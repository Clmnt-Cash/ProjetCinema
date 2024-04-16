package Modele;

import java.util.Date;

public class Seance {
    //Attributs
    private String date;
    private int prix;
    private String heure;
    private int id;


    //Constructeur
    public Seance(String date, String heure, int prix, int id) {
        this.date = date;
        this.heure = heure;
        this.prix = prix;
        this.id = id;
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
    public int getId(){return id;}

    //Setters
    public void setDate(String date){
        this.date = date;
    }

    public void setPrix(int prix){
        this.prix = prix;
    }

    public void setHeure(String heure){
        this.heure = heure;
    }
}
