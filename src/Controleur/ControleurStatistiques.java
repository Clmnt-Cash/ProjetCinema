package Controleur;

import Modele.Client;
import Modele.Film;
import Modele.FilmParAchat;
import Modele.Seance;
import Vue.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ControleurStatistiques {
    private Client membre;
    private GUIstatistiques vueStat;
    private Connexion connexion;
    private ArrayList<Film> films;
    private ArrayList<FilmParAchat> filmsParAchat;

    private ControleurComptes controleurComptes;
    //private ControleurReduc controleurReduc;
    private GUIcomptes vueComptes;
    //private GUIreduc vueReduc;

    public ControleurStatistiques(Connexion connexion) {
        this.connexion = connexion;
    }

    public void setVue(GUIstatistiques vue){
        this.vueStat = vue;
        this.filmsParAchat = this.getFilmsParAchat();

        //Aller sur la page des comptes
        this.vueStat.addListenerOngletComptes(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                vueStat.closeWindow();
                controleurComptes = new ControleurComptes(connexion);
                controleurComptes.setMembre(membre);
                vueComptes = new GUIcomptes(membre, controleurComptes);
                controleurComptes.setVue(vueComptes);
                controleurComptes.openWindow();
            }
        });
        //Aller sur la page des reductions
        this.vueStat.addListenerOngletReduc(new ActionListener(){
            //Ouverture de la page menu
            @Override
            public void actionPerformed(ActionEvent e) {
                vueStat.closeWindow();
                /*controleurReduc = new ControleurReduc(connexion);
                controleurReduc.setMembre(membre);
                vueReduc = new GUIreduc(membre, controleurReduc);
                controleurReduc.setVue(vueReduc);
                controleurReduc.openWindow();*/
            }
        });
    }
    public void setMembre(Client membre) {
        this.membre = membre;
    }

    public ArrayList<FilmParAchat> getFilmsParAchat(){
        ArrayList<FilmParAchat> filmsParAchat = new ArrayList<>();

        try {
            String requete = "SELECT films.Titre, SUM(commande.Nb_places) AS Nombre_de_commandes " +
                    "FROM films " +
                    "LEFT JOIN seance ON films.ID = seance.ID_film " +
                    "LEFT JOIN commande ON seance.ID = commande.ID_seance " +
                    "GROUP BY films.ID, films.Titre " +
                    "ORDER BY Nombre_de_commandes DESC;";
            ArrayList<String> resultatsFilms = connexion.remplirChampsRequete(requete);

            for (String resultat : resultatsFilms) {
                String[] infosFilm = resultat.split(",");
                String titre = infosFilm[0];
                int nbCommandes;
                if (infosFilm[1] != null && !infosFilm[1].equals("null") && !infosFilm[1].isEmpty()) {
                    try {
                        nbCommandes = Integer.parseInt(infosFilm[1].trim());
                    } catch (NumberFormatException e) {
                        nbCommandes = 0; // Valeur par défaut si la conversion échoue
                    }
                } else {
                    nbCommandes = 0; // Valeur par défaut si le nombre de commandes est null ou vide
                }
                filmsParAchat.add(new FilmParAchat(titre, nbCommandes));
            }




        } catch (SQLException e) {
            e.printStackTrace();
        }

        return filmsParAchat;
    }

}
