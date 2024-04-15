package Modele;

import java.util.Date;

public class Seance {
    //Attributs
    private Date date;
    private int prix;

    //Constructeur
    public Seance(Date date, int prix) {
        this.date = date;
        this.prix = prix;
    }

    //Getters
    public Date getDate(){
        return date;
    }
    public int getPrix(){
        return prix;
    }
}
