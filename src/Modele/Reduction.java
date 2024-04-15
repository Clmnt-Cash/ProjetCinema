package Modele;

public class Reduction {
    //Attributs
    private int enfant;
    private int regulier;
    private int senior;
    //Constructeur
    public Reduction(int e, int r, int s){
        this.enfant = e;
        this.regulier = r;
        this.senior = s;
    }

    //Getters
    public int getReductionEnfant(){return enfant;}
    public int getReductionRegulier(){return regulier;}
    public int getReductionSenior(){return senior;}

}
