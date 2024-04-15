package Modele;

import java.util.ArrayList;

public class Film {

    //attributs
    private int id;
    private String titre;
    private String real;
    private String synopsis;
    private String cheminImage;
    private ArrayList<Seance> seances;
    // Constructeur
    public Film(int id, String titre, String real, String synopsis, String cheminImage) {
        this.id = id;
        this.titre = titre;
        this.real = real;
        this.synopsis = synopsis;
        this.cheminImage = cheminImage;
        this.seances = new ArrayList<Seance>();
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getRealisateur() {
        return real;
    }

    public void setRealisateur(String real) {
        this.real = real;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getCheminImage() {
        return cheminImage;
    }

    public void setCheminImage(String cheminImage) {
        this.cheminImage = cheminImage;
    }

    public void setSeances(ArrayList<Seance> seances) {
        this.seances = seances;
    }
    public ArrayList<Seance> getSeances(){
        return seances;
    }
}

