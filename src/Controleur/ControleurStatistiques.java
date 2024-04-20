package Controleur;

import Modele.Client;
import Modele.Film;
import Modele.FilmParAchat;
import Modele.Seance;
import Vue.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
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
    private GUIcomptes vueComptes;

    public ControleurStatistiques(Connexion connexion) {
        this.connexion = connexion;
    }

    public void setVue(GUIstatistiques vue){
        this.vueStat = vue;
        this.filmsParAchat = this.getFilmsParAchat();
        this.vueStat.addListenerOngletFilms(new ActionListener(){
            //Ouverture de la page menu
            @Override
            public void actionPerformed(ActionEvent e) {
                vueStat.closeWindow();
                ControleurEmployeAccueil controleurAccueil = new ControleurEmployeAccueil(connexion);
                GUIEmployeAccueil vueAccueil = new GUIEmployeAccueil(membre, controleurAccueil);
                controleurAccueil.setVue(vueAccueil);
                controleurAccueil.setMembre(membre);
                controleurAccueil.openWindow();
            }
        });
        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel panel = new JPanel();
                panel.add(new JLabel("Etes-vous sûr de vouloir vous déconnecter ?"));

                int resultat = JOptionPane.showConfirmDialog(null, panel, "Déconnexion", JOptionPane.OK_CANCEL_OPTION);

                if (resultat == JOptionPane.OK_OPTION) {
                    vueStat.closeWindow();
                    GUIconnexion vueConnexion = new GUIconnexion();
                    ControleurConnexion controleurConnexion = new ControleurConnexion(vueConnexion);
                }
            }
        };
        this.vueStat.addMouseListenerBoutonDeconnexion(mouseListener);
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
