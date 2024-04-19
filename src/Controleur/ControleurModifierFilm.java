package Controleur;

import Modele.Client;
import Modele.Film;
import Modele.Reduction;
import Modele.Seance;
import Vue.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

public class ControleurModifierFilm {
    //Attributs
    private Client client;
    private GUIModifierFilm vueModifierFilm;
    private Connexion connexion;
    private Film filmActuel;
    private GUIaccueil vueAccueil;
    private ControleurAccueil controleurAccueil;

    //Constructeur
    public ControleurModifierFilm(Connexion connexion, Film film, Client client) {
        this.connexion = connexion;
        this.filmActuel = film;
        this.client = client;
    }
    //Methode pour initialiser la vue
    public void setVue(GUIModifierFilm vue){
        this.vueModifierFilm = vue;
        //////////////////////////////////////////Listener pour le retour à l'accueil//////////////////////////////////////
        this.vueModifierFilm.addListenerRetour(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                vueModifierFilm.closeWindow();
                ControleurEmployeAccueil controleurEmployeAccueil = new ControleurEmployeAccueil(connexion);
                GUIEmployeAccueil vueEmployeAccueil= new GUIEmployeAccueil(client, controleurEmployeAccueil);
                controleurEmployeAccueil.setVue(vueEmployeAccueil);
                controleurEmployeAccueil.setMembre(client);
                controleurEmployeAccueil.openWindow();
            }
        });
        //////////////////////////////////////////Listener pour enregistrer les modifs//////////////////////////////////////
        this.vueModifierFilm.addListenerEnregistrer(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                vueModifierFilm.closeWindow();
                ArrayList<Seance> seances = getSeancesForFilm(filmActuel.getId());
                filmActuel.setSeances(seances);
                modifierFilm();
                ControleurModifierFilm controleurModifierFilm = new ControleurModifierFilm(connexion, filmActuel, client);
                GUIModifierFilm vueModifierFilm = new GUIModifierFilm(client, controleurModifierFilm, filmActuel);
                controleurModifierFilm.setVue(vueModifierFilm);
                controleurModifierFilm.setClient(client);
            }
        });
        //////////////////////////////////////////Listener pour supprimer un film//////////////////////////////////////
        this.vueModifierFilm.addListenerSupprimer(e -> {

            //Customisation de la fenetre de dialogue
            UIManager.put("OptionPane.background", Color.WHITE);
            UIManager.put("Panel.background", Color.WHITE);
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Button.background", Color.WHITE);
            UIManager.put("Button.foreground", Color.BLACK);
            UIManager.put("Button.border", BorderFactory.createLineBorder(Color.WHITE));
            UIManager.put("Button.focus", Color.WHITE);

            //Création de la fenêtre de dialogue
            JPanel panel = new JPanel();
            JLabel label = new JLabel("Etes-vous sûr de vouloir supprimer " + filmActuel.getTitre() + " ?") ;
            panel.add(label);

            int resultat = JOptionPane.showConfirmDialog(null, panel, "Supprimer "+filmActuel.getTitre(), JOptionPane.OK_CANCEL_OPTION);

            if (resultat == JOptionPane.OK_OPTION) {
                supprimerFilm(filmActuel.getId());
                vueModifierFilm.closeWindow();
                ControleurEmployeAccueil controleurEmployeAccueil = new ControleurEmployeAccueil(connexion);
                GUIEmployeAccueil vueEmployeAccueil = new GUIEmployeAccueil(client, controleurEmployeAccueil);
                controleurEmployeAccueil.setVue(vueEmployeAccueil);
                controleurEmployeAccueil.setMembre(client);
            }
        });
        this.vueModifierFilm.addListenersSupprimer(e -> {
            JButton bouton = (JButton) e.getSource();

            String seanceChoisie = (String) bouton.getClientProperty("infos");
            String[] infos = seanceChoisie.split(",");
            //Extraire les informations sur le film
            int idSeance = Integer.parseInt(infos[0].trim());
            String date = infos[1].trim();
            String heureSeance = infos[2].trim();

            //Customisation de la fenetre de dialogue
            UIManager.put("OptionPane.background", Color.WHITE);
            UIManager.put("Panel.background", Color.WHITE);
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Button.background", Color.WHITE);
            UIManager.put("Button.foreground", Color.BLACK);
            UIManager.put("Button.border", BorderFactory.createLineBorder(Color.WHITE));
            UIManager.put("Button.focus", Color.WHITE);

            //Création de la fenêtre de dialogue
            JPanel panel = new JPanel();
            JLabel label = new JLabel("Etes-vous sûr de vouloir supprimer la séance du "+date + " à "+heureSeance+" ?") ;
            panel.add(label);

            int resultat = JOptionPane.showConfirmDialog(null, panel, "Modifier la séance pour "+filmActuel.getTitre(), JOptionPane.OK_CANCEL_OPTION);

            if (resultat == JOptionPane.OK_OPTION) {
                supprimerSeance(idSeance);
                vueModifierFilm.closeWindow();
                ArrayList<Seance> seances = getSeancesForFilm(filmActuel.getId());
                filmActuel.setSeances(seances);
                modifierFilm();
                ControleurModifierFilm controleurModifierFilm = new ControleurModifierFilm(connexion, filmActuel, client);
                GUIModifierFilm vueModifierFilm = new GUIModifierFilm(client, controleurModifierFilm, filmActuel);
                vueModifierFilm.affichageModifier();
                //Remetre la fonctionnalité d enregistrement
                vueModifierFilm.addListenerEnregistrer(new ActionListener(){
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        vueModifierFilm.closeWindow();
                        ArrayList<Seance> seances = getSeancesForFilm(filmActuel.getId());
                        filmActuel.setSeances(seances);
                        modifierFilm();
                    }
                });
                controleurModifierFilm.setVue(vueModifierFilm);
                controleurModifierFilm.setClient(client);
            }
        });
        //////////////////////////////////////////Listener pour ajouter une séance//////////////////////////////////////

        this.vueModifierFilm.addListenerAjouterSeance(e -> {
            //Customisation de la fenetre de dialogue
            UIManager.put("OptionPane.background", Color.WHITE);
            UIManager.put("Panel.background", Color.WHITE);
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Button.background", Color.WHITE);
            UIManager.put("Button.foreground", Color.BLACK);
            UIManager.put("Button.border", BorderFactory.createLineBorder(Color.WHITE));
            UIManager.put("Button.focus", Color.WHITE);

            //Création de la fenêtre de dialogue
            JPanel panel = new JPanel();
            // Création du combobox pour la date
            String[] jours = new String[31];
            for (int i = 0; i < 31; i++) {
                jours[i] = String.format("%02d", i + 1);
            }
            JLabel labelJour = new JLabel("Jour :");
            panel.add(labelJour);

            JComboBox<String> comboBoxJour = new JComboBox<>(jours);
            comboBoxJour.setSelectedItem("01");
            comboBoxJour.setPreferredSize(new Dimension(40, 30));
            panel.add(comboBoxJour);


            JLabel labelMois= new JLabel("Mois :");
            panel.add(labelMois);
            String[] moisCombobox = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
            JComboBox<String> comboBoxMois = new JComboBox<>(moisCombobox);
            comboBoxMois.setSelectedItem("01");
            comboBoxMois.setPreferredSize(new Dimension(40, 30));
            panel.add(comboBoxMois);

            JLabel labelHeure= new JLabel("Heure :");
            panel.add(labelHeure);

            // Création du combobox pour l'heure
            String[] heures = new String[24];
            for (int i = 0; i < 24; i++) {
                heures[i] = String.valueOf(i);
            }
            JComboBox<String> comboBoxHeure = new JComboBox<>(heures);
            comboBoxHeure.setSelectedItem("00");
            comboBoxHeure.setPreferredSize(new Dimension(40, 30));
            panel.add(comboBoxHeure);

            JLabel labelPoints= new JLabel(":");
            panel.add(labelPoints);

            String[] minutes = new String[60];
            for (int i = 0; i < 60; i++) {
                minutes[i] = String.valueOf(i);
            }
            JComboBox<String> comboBoxMinutes = new JComboBox<>(minutes);
            comboBoxMinutes.setSelectedItem("00");
            comboBoxMinutes.setPreferredSize(new Dimension(40, 30));
            panel.add(comboBoxMinutes);

            JLabel labelPrix= new JLabel("  Prix :");
            panel.add(labelPrix);


            SpinnerNumberModel spinnerModel = new SpinnerNumberModel(Double.parseDouble("0.00"), null, null, 0.1);
            JSpinner spinner = new JSpinner(spinnerModel);
            JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner, "#0.00");
            spinner.setEditor(editor);
            panel.add(spinner);


            int resultat = JOptionPane.showConfirmDialog(null, panel, "Créer une séance pour "+filmActuel.getTitre(), JOptionPane.OK_CANCEL_OPTION);

            if (resultat == JOptionPane.OK_OPTION) {
                //Recuperation des valeurs sélectionnees dans les combobox et le spinner
                String newjour = (String) comboBoxJour.getSelectedItem();
                String newmois = (String) comboBoxMois.getSelectedItem();
                String newheure = (String) comboBoxHeure.getSelectedItem();
                String newminute = (String) comboBoxMinutes.getSelectedItem();
                double newprix = (double) spinnerModel.getValue();
                System.out.println(newprix);

                //Formatage de la date et de l'heure
                String dateHeure = "2024-" + newmois + "-" + newjour + " " + newheure + ":" + newminute + ":00";

                ajouterSeance(dateHeure, (float) newprix);

                vueModifierFilm.closeWindow();
                ArrayList<Seance> seances = getSeancesForFilm(filmActuel.getId());
                filmActuel.setSeances(seances);
                modifierFilm();
                ControleurModifierFilm controleurModifierFilm = new ControleurModifierFilm(connexion, filmActuel, client);
                GUIModifierFilm vueModifierFilm = new GUIModifierFilm(client, controleurModifierFilm, filmActuel);
                vueModifierFilm.affichageModifier();
                //Remetre la fonctionnalité d enregistrement
                vueModifierFilm.addListenerEnregistrer(e1 -> {
                    vueModifierFilm.closeWindow();
                    ArrayList<Seance> seances1 = getSeancesForFilm(filmActuel.getId());
                    filmActuel.setSeances(seances1);
                    modifierFilm();
                });
                controleurModifierFilm.setVue(vueModifierFilm);
                controleurModifierFilm.setClient(client);
            }
        });
        //////////////////////////////////////////Listener pour modifier une séance//////////////////////////////////////
        this.vueModifierFilm.addListenersModifier(e -> {
            JButton bouton = (JButton) e.getSource();

            String seanceChoisie = (String) bouton.getClientProperty("infos");
            String[] infos = seanceChoisie.split(",");
            //Extraire les informations sur le film
            int idSeance = Integer.parseInt(infos[0].trim());
            String date = infos[1].trim();
            String[] elementsDate = date.split("/");
            String jour = (elementsDate[0]);
            String mois = (elementsDate[1]);

            String heureSeance = infos[2].trim();
            String[] elementsHeure = heureSeance.split(":"); // Divise la chaîne en fonction du caractère "/"
            String heure = (elementsHeure[0]);
            String minute = (elementsHeure[1]);

            float prix = Float.parseFloat(infos[3].trim());

            //Customisation de la fenetre de dialogue
            UIManager.put("OptionPane.background", Color.WHITE);
            UIManager.put("Panel.background", Color.WHITE);
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("Button.background", Color.WHITE);
            UIManager.put("Button.foreground", Color.BLACK);
            UIManager.put("Button.border", BorderFactory.createLineBorder(Color.WHITE));
            UIManager.put("Button.focus", Color.WHITE);

            //Création de la fenêtre de dialogue
            JPanel panel = new JPanel();
            // Création du combobox pour la date
            String[] jours = new String[31];
            for (int i = 0; i < 31; i++) {
                jours[i] = String.format("%02d", i + 1);
            }
            JLabel labelJour = new JLabel("Jour :");
            panel.add(labelJour);

            JComboBox<String> comboBoxJour = new JComboBox<>(jours);
            comboBoxJour.setSelectedItem(jour);
            comboBoxJour.setPreferredSize(new Dimension(40, 30));
            panel.add(comboBoxJour);


            JLabel labelMois= new JLabel("Mois :");
            panel.add(labelMois);
            String[] moisCombobox = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
            JComboBox<String> comboBoxMois = new JComboBox<>(moisCombobox);
            comboBoxMois.setSelectedItem(mois);
            comboBoxMois.setPreferredSize(new Dimension(40, 30));
            panel.add(comboBoxMois);

            JLabel labelHeure= new JLabel("Heure :");
            panel.add(labelHeure);

            // Création du combobox pour l'heure
            String[] heures = new String[24];
            for (int i = 0; i < 24; i++) {
                heures[i] = String.valueOf(i);
            }
            JComboBox<String> comboBoxHeure = new JComboBox<>(heures);
            comboBoxHeure.setSelectedItem(heure);
            comboBoxHeure.setPreferredSize(new Dimension(40, 30));
            panel.add(comboBoxHeure);

            JLabel labelPoints= new JLabel(":");
            panel.add(labelPoints);

            String[] minutes = new String[60];
            for (int i = 0; i < 60; i++) {
                minutes[i] = String.valueOf(i);
            }
            JComboBox<String> comboBoxMinutes = new JComboBox<>(minutes);
            comboBoxMinutes.setSelectedItem(minute);
            comboBoxMinutes.setPreferredSize(new Dimension(40, 30));
            panel.add(comboBoxMinutes);

            JLabel labelPrix= new JLabel("  Prix :");
            panel.add(labelPrix);


            SpinnerNumberModel spinnerModel = new SpinnerNumberModel(prix, null, null, 0.1);
            JSpinner spinner = new JSpinner(spinnerModel);
            JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner, "#0.00");
            spinner.setEditor(editor);
            panel.add(spinner);


            int resultat = JOptionPane.showConfirmDialog(null, panel, "Modifier la séance pour "+filmActuel.getTitre(), JOptionPane.OK_CANCEL_OPTION);

            if (resultat == JOptionPane.OK_OPTION) {
                //Récupération des valeurs sélectionnées dans les combobox et le spinner
                String newjour = (String) comboBoxJour.getSelectedItem();
                String newmois = (String) comboBoxMois.getSelectedItem();
                String newheure = (String) comboBoxHeure.getSelectedItem();
                String newminute = (String) comboBoxMinutes.getSelectedItem();
                float newprix = (float) spinnerModel.getValue();
                System.out.println(newprix);

                // Formatage de la date et de l'heure
                String dateHeure = "2024-"+newmois+"-"+newjour+" "+newheure+":"+newminute+":00";


                //Appel de la méthode pour modifier la séance dans la base de données
                modifierSeance(idSeance, dateHeure, newprix);
                vueModifierFilm.closeWindow();
                ArrayList<Seance> seances = getSeancesForFilm(filmActuel.getId());
                filmActuel.setSeances(seances);
                modifierFilm();
                ControleurModifierFilm controleurModifierFilm = new ControleurModifierFilm(connexion, filmActuel, client);
                GUIModifierFilm vueModifierFilm = new GUIModifierFilm(client, controleurModifierFilm, filmActuel);
                vueModifierFilm.affichageModifier();
                //Remetre la fonctionnalité d enregistrement
                vueModifierFilm.addListenerEnregistrer(e12 -> {
                    vueModifierFilm.closeWindow();
                    ArrayList<Seance> seances12 = getSeancesForFilm(filmActuel.getId());
                    filmActuel.setSeances(seances12);
                    modifierFilm();
                });
                controleurModifierFilm.setVue(vueModifierFilm);
                controleurModifierFilm.setClient(client);
            }
        });
        //////////////////////////////////////////Listener pour modifier les données d'un film//////////////////////////////////////
        this.vueModifierFilm.addListenerModifier(new ActionListener(){
            //Modifier le film
            @Override
            public void actionPerformed(ActionEvent e) {
                vueModifierFilm.affichageModifier();
            }
        });
    }

    //Méthode pour initialiser le client
    public void setClient(Client client) {
        this.client = client;
    }

    //Méthode pour ouvrir la fenetre
    public void openWindow(){
        this.vueModifierFilm.setVisible(true);
    }

    //Méthode pour supprimer un film de la bdd
    public void supprimerFilm(int id){
        try {
            String requeteInsertion = "DELETE FROM films WHERE ID = " + id + "; ";
            connexion.executerRequete(requeteInsertion);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la connexion à la base de données : " + e);
        }
        try {
            String requeteInsertion = "DELETE FROM seance WHERE ID_film = " + id + "; ";
            connexion.executerRequete(requeteInsertion);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la connexion à la base de données : " + e);
        }
        try {
            String requeteInsertion = "DELETE FROM commande WHERE ID_seance IN (SELECT ID FROM seance WHERE ID_film = " + id + ");";
            connexion.executerRequete(requeteInsertion);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la connexion à la base de données : " + e);
        }
    }

    //Méthode pour ajouter une séance à un film dans la bdd
    public void ajouterSeance(String date, float prix){
        try {
            String requeteInsertion = "INSERT INTO seance (Date_diffusion, Prix, ID_film) VALUES ('" + date + "', " + prix + ", " + filmActuel.getId() + ")";
            connexion.executerRequete(requeteInsertion);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la connexion à la base de données : " + e);
        }
    }
    //méthode pour supprimer une séance
    public void supprimerSeance(int id){
        try {
            String requeteInsertion = "DELETE FROM seance WHERE ID = " + id + "; ";
            connexion.executerRequete(requeteInsertion);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la connexion à la base de données : " + e);
        }
        try {
            String requeteInsertion =  "DELETE FROM commande WHERE ID_seance = " + id + ";";
            connexion.executerRequete(requeteInsertion);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la connexion à la base de données : " + e);
        }

    }
    //Méthode pour modifier une séance
    public void modifierSeance(int id, String dateHeure, float prix){

        try {
            String requeteInsertion = "UPDATE seance" +
                    " SET Date_diffusion = '" + dateHeure + "'," +
                    " Prix = " + prix +
                    " WHERE ID = " + id;

            connexion.executerRequete(requeteInsertion);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la connexion à la base de données : " + e);
        }
    }
    //Méthode pour modifier les infos d'un film
    public void modifierFilm(){
        String titre = this.vueModifierFilm.getTitre();
        String realisateur = this.vueModifierFilm.getRealisateur();
        String chemin = this.vueModifierFilm.getChemin();
        String synopsis = this.vueModifierFilm.getSynopsis();
        this.filmActuel.setTitre(titre);
        this.filmActuel.setRealisateur(realisateur);
        this.filmActuel.setCheminImage(chemin);
        this.filmActuel.setSynopsis(synopsis);

        try {
            String requeteInsertion = "UPDATE films" +
                    " SET Titre = '" + titre + "'," +
                    " Realisateur = '" + realisateur + "'," +
                    " Synopsis = '" + synopsis + "'," +
                    " Chemin_image = '" + chemin + "'" +
                    " WHERE ID = " + filmActuel.getId();

            connexion.executerRequete(requeteInsertion);


        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la connexion à la base de données : " + e);
        }
    }

    //Méthode pour récupérer chaque séance d'un film
    private ArrayList<Seance> getSeancesForFilm(int filmId) {
        ArrayList<Seance> seances = new ArrayList<>();

        try {
            //Récupérer les résultats de la requête SQL pour les séances du film donné
            ArrayList<String> resultatsSeances = connexion.remplirChampsRequete(
                    "SELECT seance.*, films.Titre " +
                            "FROM seance " +
                            "INNER JOIN films ON seance.ID_film = films.ID " +
                            "WHERE seance.ID_film = " + filmId + " " +
                            "ORDER BY seance.Date_diffusion"
            );
            for (String resultat : resultatsSeances) {
                String[] infosSeance = resultat.split(",");

                //Conversion de la date
                int id = Integer.parseInt(infosSeance[0].trim());

                String dateHeure = infosSeance[1].trim();
                String[] parties = dateHeure.split(" ");

                String date = parties[0];
                String mois = date.substring(5, 7);
                String jour = date.substring(8, 10);
                date = jour + "/" + mois;

                String heure = parties[1];
                heure = heure.substring(0, 5);
                float prix = Float.parseFloat(infosSeance[2].trim());
                String titre = infosSeance[3].trim();

                //Créer une séance avec les informations récupérées
                Seance seance = new Seance(date, heure, prix, id, titre);

                //Ajouter la séance à la liste
                seances.add(seance);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return seances;
    }

}
