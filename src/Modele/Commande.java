package Modele;

public class Commande {
    //Attributs
    private int nbPlaces;
    private Seance seance;
    private float prixAvecReduc;
    private float prixSansReduc;
    //Constructeur
    public Commande(int nbPlaces, Seance seance, float prixAvecReduc, float prixSansReduc){
        this.nbPlaces = nbPlaces;
        this.prixAvecReduc = prixAvecReduc;
        this.prixSansReduc = prixSansReduc;
        this.seance = seance;
    }
    //Getters
    public int getNbPlaces(){return nbPlaces;}
    public Seance getSeance(){return seance;}
    public float getPrixAvecReduc(){return prixAvecReduc;}
    public float getPrixSansReduc(){return prixSansReduc;}

}
