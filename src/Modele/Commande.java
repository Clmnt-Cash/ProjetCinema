package Modele;

public class Commande {
    //Attributs
    private int nbPlaces;
    private Seance seance;
    private float prixAvecReduc;
    private float prixSansReduc;
    private int ID;
    //Constructeur
    public Commande(int nbPlaces, Seance seance, float prixAvecReduc, float prixSansReduc, int id){
        this.nbPlaces = nbPlaces;
        this.prixAvecReduc = prixAvecReduc;
        this.prixSansReduc = prixSansReduc;
        this.seance = seance;
        this.ID = id;
    }
    //Getters
    public int getNbPlaces(){return nbPlaces;}
    public Seance getSeance(){return seance;}
    public float getPrixAvecReduc(){return prixAvecReduc;}
    public float getPrixSansReduc(){return prixSansReduc;}
    public int getId(){return ID;}

}
