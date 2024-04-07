package Modele;

public class Utilisateur {
    private int id;
    private int type;
    private String nom;
    private String prenom;
    private String email;
    private String mdp;

    // Constructeur
    public Utilisateur(int id, int type, String nom, String prenom, String email, String mdp) {
        this.id = id;
        this.type = type;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.mdp = mdp;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMotDePasse() {
        return mdp;
    }

    public void setMotDePasse(String mdp) {
        this.mdp = mdp;
    }
}

