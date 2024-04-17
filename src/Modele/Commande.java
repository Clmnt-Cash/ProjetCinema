package Modele;

public class Commande {
    //Attributs
    private int nbPlaces;
    private Seance seance;
    private float prixAvecReduc;
    private float prixSansReduc;
    private int ID;
    private boolean paye;
    //Constructeur
    public Commande(int nbPlaces, Seance seance, float prixAvecReduc, float prixSansReduc, int id, boolean paye){
        this.nbPlaces = nbPlaces;
        this.prixAvecReduc = prixAvecReduc;
        this.prixSansReduc = prixSansReduc;
        this.seance = seance;
        this.ID = id;
        this.paye = paye;
    }
    //Getters
    public int getNbPlaces(){return nbPlaces;}
    public Seance getSeance(){return seance;}
    public float getPrixAvecReduc(){return prixAvecReduc;}
    public float getPrixSansReduc(){return prixSansReduc;}
    public int getId(){return ID;}
    public boolean getPaye(){return paye;}

}
