package Vue;

import Controleur.ControleurAccueil;
import Controleur.ControleurFilm;
import Modele.Client;
import Modele.Film;
import Modele.Seance;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GUIfilm extends JFrame {
    //Attributs
    private Client client;
    private ControleurFilm controleurFilm;
    private JButton boutonRetour;
    private Film filmActuel;
    private ArrayList<JButton> boutonsSeance;

    //Constructeur
    public GUIfilm(Client client, ControleurFilm controleurFilm, Film film) {
        super("Cinéma");
        this.client = client;
        this.controleurFilm = controleurFilm;
        this.filmActuel = film;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(1500, 800);

        //Obtenir les dimensions de l'écran
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Color c1 = new Color(0, 0, 0);
                g.setColor(c1);
                g.fillRect(0, 100, 1500, 700);
                Color c2 = new Color(50, 50, 50, 200);
                g.setColor(c2);
                g.fillRect(0, 0, 1500, 100);
                ImageIcon logo = new ImageIcon("images/logos/logo_black.png");
                Image image = logo.getImage();
                g.drawImage(image, 0, 0, 100, 100, this);
            }
        };

        //Ajout du JLabel pour afficher le nom du client
        JLabel labelNom = new JLabel("Connecté en tant que " + client.getPrenom() + " " + client.getNom());
        labelNom.setBounds(1100, 30, 300, 30);
        labelNom.setForeground(Color.WHITE);
        labelNom.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(labelNom);
        panel.setLayout(null);

        //Affichage du nom du film
        JLabel labelTitre = new JLabel(filmActuel.getTitre());
        labelTitre.setBounds(450, 200, 400, 30);
        labelTitre.setForeground(Color.WHITE);
        Font police = new Font("Arial", Font.BOLD, 20);
        //Appliquer la police au JLabel
        labelTitre.setFont(police);
        panel.add(labelTitre);

        //Affichage de l'image du film
        ImageIcon imageIcon = new ImageIcon("images/affiches/" + filmActuel.getCheminImage());
        Image image = imageIcon.getImage();
        Image newImage = image.getScaledInstance(412, 550, Image.SCALE_SMOOTH);
        ImageIcon newImageIcon = new ImageIcon(newImage);
        JLabel labelImage = new JLabel(newImageIcon);
        labelImage.setBounds(10, 200, 412, 550);
        panel.add(labelImage);

        //Affichage du nom du réalisateur
        JLabel labelRealisateur = new JLabel("Réalisé par " + filmActuel.getRealisateur());
        labelRealisateur.setBounds(450, 230, 400, 30);
        labelRealisateur.setForeground(Color.WHITE);
        panel.add(labelRealisateur);

        //Affichage du synopsis
        JTextArea areaSynopsis = new JTextArea("Synopsis \n\n" + filmActuel.getSynopsis());
        areaSynopsis.setBounds(450, 280, 600, 400);
        areaSynopsis.setForeground(Color.WHITE);
        areaSynopsis.setBackground(Color.BLACK);
        areaSynopsis.setLineWrap(true);
        areaSynopsis.setWrapStyleWord(true);
        panel.add(areaSynopsis);



        //Ajout du bouton Retour
        boutonRetour = new JButton("Retour");
        boutonRetour.setBounds(10, 110, 100, 50);
        boutonRetour.setForeground(Color.WHITE);
        boutonRetour.setBackground(Color.BLACK);
        panel.add(boutonRetour);

        //Ajout des boutons pour chaque séance
        ArrayList<Seance> seances = film.getSeances();
        for(Seance s : seances){
            System.out.println(s.getDate() + " " + s.getPrix());
            JButton bouton = new JButton();
            bouton.setBounds(450, 500, 100, 50);
            bouton.setForeground(Color.WHITE);
            bouton.setBackground(Color.BLACK);
            panel.add(bouton);

        }

        setVisible(true);
        panel.setLayout(null);
        add(panel);

    }

    public void addListenerRetour(ActionListener listener){
        boutonRetour.addActionListener(listener);
    }

    public void closeWindow(){setVisible(false);dispose();}
}
