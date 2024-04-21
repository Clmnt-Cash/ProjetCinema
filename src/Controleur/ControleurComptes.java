package Controleur;

import Modele.Client;
import Modele.Film;
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
import java.util.Objects;

public class ControleurComptes {
    //Attributs
    private Client membre;
    private GUIcomptes vueComptes;
    private Connexion connexion;
    private ControleurEmployeAccueil controleurEmployeAccueil;
    private GUIEmployeAccueil vueEmployeAccueil;

    //Constructeur
    public ControleurComptes(Connexion connexion) {
        this.connexion = connexion;
    }

    //Méthode pour initialiser la vue
    public void setVue(GUIcomptes vue) {
        this.vueComptes = vue;

        //Aller sur la page des films
        this.vueComptes.addListenerOngletFilms(new ActionListener() {
            //Ouverture de la page menu
            @Override
            public void actionPerformed(ActionEvent e) {
                vueComptes.closeWindow();
                controleurEmployeAccueil = new ControleurEmployeAccueil(connexion);
                controleurEmployeAccueil.setMembre(membre);
                vueEmployeAccueil = new GUIEmployeAccueil(membre, controleurEmployeAccueil);
                controleurEmployeAccueil.setVue(vueEmployeAccueil);
                controleurEmployeAccueil.openWindow();
            }
        });
        //Se déconnecter
        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JPanel panel = new JPanel();
                panel.add(new JLabel("Etes-vous sûr de vouloir vous déconnecter ?"));

                int resultat = JOptionPane.showConfirmDialog(null, panel, "Déconnexion", JOptionPane.OK_CANCEL_OPTION);

                if (resultat == JOptionPane.OK_OPTION) {
                    vueComptes.closeWindow();
                    GUIconnexion vueConnexion = new GUIconnexion();
                    ControleurConnexion controleurConnexion = new ControleurConnexion(vueConnexion);
                }
            }
        };
        this.vueComptes.addMouseListenerBoutonDeconnexion(mouseListener);

        //Aller sur la page des statistiques
        this.vueComptes.addListenerOngletStat(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                vueComptes.closeWindow();
                ControleurStatistiques controleurStatistiques = new ControleurStatistiques(connexion);
                GUIstatistiques vueStat = new GUIstatistiques(membre, controleurStatistiques);
                controleurStatistiques.setVue(vueStat);
                controleurStatistiques.setMembre(membre);
            }
        });
        //Aller sur la page réductions
        this.vueComptes.addListenerOngletReduc(new ActionListener(){
            //Ouverture de la page menu
            @Override
            public void actionPerformed(ActionEvent e) {
                vueComptes.closeWindow();
                ControleurReduction controleurReduction = new ControleurReduction(connexion);
                GUIreduction vueReduction = new GUIreduction(membre, controleurReduction);
                controleurReduction.setVue(vueReduction);
                controleurReduction.setMembre(membre);
            }
        });
        //Modifier un compte
        this.vueComptes.addListenersModifier(e -> {
            JButton bouton = (JButton) e.getSource();

            String clientChoisi = (String) bouton.getClientProperty("infos");
            String[] infos = clientChoisi.split(",");
            //Extraire les informations sur le client
            int id = Integer.parseInt(infos[0].trim());
            String nom = infos[1].trim();
            String prenom = infos[2].trim();
            String type = infos[3].trim();
            String email = infos[4].trim();
            String motDePasse = infos[5].trim();


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

            JLabel labelNom = new JLabel("Nom:");
            JTextField textFieldNom = new JTextField(nom);
            textFieldNom.setPreferredSize(new Dimension(100, 20));
            panel.add(labelNom);
            panel.add(textFieldNom);

            JLabel labelPrenom = new JLabel("Prénom:");
            JTextField textFieldPrenom = new JTextField(prenom);
            textFieldPrenom.setPreferredSize(new Dimension(100, 20));
            panel.add(labelPrenom);
            panel.add(textFieldPrenom);

            JLabel labelType = new JLabel("Type:");
            String[] types = {"enfant", "régulier", "senior"};
            JComboBox<String> comboBoxType = new JComboBox<>(types);
            comboBoxType.setSelectedItem(type);
            comboBoxType.setPreferredSize(new Dimension(100, 20));
            panel.add(labelType);
            panel.add(comboBoxType);

            JLabel labelEmail = new JLabel("Email:");
            JTextField textFieldEmail = new JTextField(email);
            textFieldEmail.setPreferredSize(new Dimension(100, 20));
            panel.add(labelEmail);
            panel.add(textFieldEmail);

            JLabel labelMotDePasse = new JLabel("Mot de passe:");
            JTextField textFieldMotDePasse = new JTextField(motDePasse);
            textFieldMotDePasse.setPreferredSize(new Dimension(100, 20));
            panel.add(labelMotDePasse);
            panel.add(textFieldMotDePasse);

            //Création d'une fenetre OK CANCEL
            int resultat = JOptionPane.showConfirmDialog(null, panel, "Modifier le profil de " + nom + " " + prenom, JOptionPane.OK_CANCEL_OPTION);

            if (resultat == JOptionPane.OK_OPTION) {
                //Récupérer les contenus des textFields
                String newemail = textFieldEmail.getText();
                String newnom = textFieldNom.getText();
                String newprenom = textFieldPrenom.getText();
                String newtype = (String) comboBoxType.getSelectedItem();;
                String newmdp = textFieldMotDePasse.getText();
                System.out.println(newtype);
                JPanel newpanel = new JPanel();
                JLabel labelErreur = new JLabel();
                newpanel.add(labelErreur);

                //Vérifier si les champs ne sont pas vides
                if (textFieldNom.getText().isEmpty() || textFieldPrenom.getText().isEmpty() || textFieldEmail.getText().isEmpty() || textFieldMotDePasse.getText().isEmpty()){
                    labelErreur.setText("Certains champs sont vide, veuillez réessayer.");
                    JOptionPane.showConfirmDialog(null, newpanel, "Erreur", JOptionPane.OK_CANCEL_OPTION);
                } else if (!newemail.contains("@")) { //Vérifier si le mail contient un '@'
                    labelErreur.setText("L'e-mail est invalide");
                    JOptionPane.showConfirmDialog(null, newpanel, "Erreur", JOptionPane.OK_CANCEL_OPTION);
                } else if(emailExisteDeja(id, newemail)){ //Vérifier si le mail est déjà associé à un compte
                    labelErreur.setText("L'e-mail est déjà associé à un autre compte.");
                    JOptionPane.showConfirmDialog(null, newpanel, "Erreur", JOptionPane.OK_CANCEL_OPTION);
                }
                else { //Appel de la méthode modifierClient et ouvrir la fenetre comptes
                    modifierClient(id, newnom, newprenom, newtype, newemail, newmdp);
                    vueComptes.closeWindow();
                    ControleurComptes controleurComptes = new ControleurComptes(connexion);
                    controleurComptes.setMembre(membre);
                    GUIcomptes vueComptes = new GUIcomptes(membre, controleurComptes);
                    controleurComptes.setVue(vueComptes);
                    controleurComptes.openWindow();
                }
            }

        });
        //Supprimer un client
        this.vueComptes.addListenersSupprimer(e -> {
            JButton bouton = (JButton) e.getSource();

            String clientChoisi = (String) bouton.getClientProperty("infos");
            String[] infos = clientChoisi.split(",");
            //Extraire les informations sur le client
            int id = Integer.parseInt(infos[0].trim());
            String nom = infos[1].trim();
            String prenom = infos[2].trim();
            String type = infos[3].trim();
            String email = infos[4].trim();
            String motDePasse = infos[5].trim();


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

            JLabel labelSupprimer = new JLabel("Etes-vous sûr de vouloir supprimer " + nom + " " + prenom + " ? Ses commandes seront également supprimées.");
            panel.add(labelSupprimer);
            panel.add(labelSupprimer);

            int resultat = JOptionPane.showConfirmDialog(null, panel, "Supprimer le profil de " + nom + " " + prenom, JOptionPane.OK_CANCEL_OPTION);

            if (resultat == JOptionPane.OK_OPTION) { //Ouvrir la page comptes
                vueComptes.closeWindow();
                supprimerClient(id);
                ControleurComptes controleurComptes = new ControleurComptes(connexion);
                controleurComptes.setMembre(membre);
                GUIcomptes vueComptes = new GUIcomptes(membre, controleurComptes);
                controleurComptes.setVue(vueComptes);
                controleurComptes.openWindow();

            }

        });
    }
    //Méthode pour initialiser le membre/employé
    public void setMembre(Client membre) {
        this.membre = membre;
    }
    //Méthode pour rendre la fenetre visible
    public void openWindow(){
        this.vueComptes.setVisible(true);
    }
    //Méthode pour modifier un client dans la bdd
    public void modifierClient(int id, String nom, String prenom, String type, String email, String mdp){
        int newType = 0;
        if(Objects.equals(type, "enfant")) newType = 1;
        else if(Objects.equals(type, "régulier")) newType = 2;
        else newType = 1;

        try { //requete d'insertion
            String requeteInsertion = "UPDATE membre SET " +
                    "Nom = '" + nom + "', " +
                    "Prenom = '" + prenom + "', " +
                    "Type = '" + newType + "', " +
                    "Email = '" + email + "', " +
                    "Mot_de_passe = '" + mdp + "' " +
                    "WHERE ID = " + id;
            connexion.executerRequete(requeteInsertion);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de l'insertion d'un client : " + e);
        }
    }
    //Méthode pour vérifier si l'email existe déjà
    public boolean emailExisteDeja(int id, String email){
        try {
            ArrayList<String> resultatsEmail = connexion.remplirChampsRequete("SELECT * FROM membre WHERE Email = '" + email + "' AND ID != " + id);
            return !resultatsEmail.isEmpty();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    //Méthode pour supprimer un client de la bdd
    public void supprimerClient(int id){
        try {
            String requeteInsertion = "DELETE FROM membre WHERE ID = " + id + "; ";
            connexion.executerRequete(requeteInsertion);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //Supprimer également les commandes de ce client
        try {
            String requeteInsertion = "DELETE FROM commande WHERE ID_client = " + id + "; ";
            connexion.executerRequete(requeteInsertion);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Méthode pour récupérer tous les comptes de membre (pas les employés)
    public ArrayList<Client> getComptes(){
        //Liste de comptes
        ArrayList<Client> comptes = new ArrayList<>();
        try {
            ArrayList<String> resultatsComptes = connexion.remplirChampsRequete("SELECT * FROM membre WHERE Type > 0 ORDER BY Nom");

            for (String resultat : resultatsComptes) {
                String[] infosCompte = resultat.split(",");
                //Extraire les informations sur le compte
                int id = Integer.parseInt(infosCompte[0].trim());
                int type = Integer.parseInt(infosCompte[1].trim());
                String nom = infosCompte[2].trim();
                String prenom = infosCompte[3].trim();
                String mail = infosCompte[4].trim();
                String mdp = infosCompte[5].trim();

                //Création d'un nouveau compte et ajour de celui-ci dans la liste
                Client compte = new Client(id, type, nom, prenom, mail, mdp);
                comptes.add(compte);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //On retourne la liste
        return comptes;
    }
}
