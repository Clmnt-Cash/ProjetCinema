package Modele;

import java.util.Date;

public class Seance {
    //Attributs
    private String date;
    private float prix;
    private String heure;
    private int id;
    private String film;


    //Constructeur
    public Seance(String date, String heure, float prix, int id, String film) {
        this.date = date;
        this.heure = heure;
        this.prix = prix;
        this.id = id;
        this.film = film;
    }

    //Getters
    public String getDate(){
        return date;
    }
    public float getPrix(){
        return prix;
    }
    public String getHeure(){
        return heure;
    }
    public int getId(){return id;}
    public String getFilm(){return film;}

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
