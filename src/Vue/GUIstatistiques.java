package Vue;

import Controleur.ControleurEmployeAccueil;
import Controleur.ControleurStatistiques;
import Modele.Client;
import Modele.Film;
import Modele.FilmParAchat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class GUIstatistiques extends JFrame {
    //Attributs
    private ArrayList<Film> films;
    private ArrayList<FilmParAchat> filmsParAchats;

    private Client membre;
    private ControleurStatistiques controleurStatistiques;
    private JButton btnFilms;
    private JButton btnComptes;
    private JButton btnReduc;
    private JButton btnStat;
    private JPanel panel;

    //Constructeur
    public GUIstatistiques(Client membre, ControleurStatistiques controleurStatistiques ) {
        super("Cinéma");
        this.membre = membre;
        this.controleurStatistiques = controleurStatistiques;
        this.filmsParAchats = controleurStatistiques.getFilmsParAchat();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(1500, 800);

        //Obtenir les dimensions de l'écran
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);

        this.afficherMenu();

        setVisible(true);
    }

    public void afficherMenu(){
        panel = new JPanel() {
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

                //Affichage du graphique
                g.setColor(Color.WHITE);
                int i = 0;
                for(FilmParAchat f : filmsParAchats){
                    g.fillRect(230, 205 + i * 30, 50 * f.getNbCommandes(), 20);
                    JLabel labelGraph = new JLabel(f.getTitre());
                    labelGraph.setBounds(20, 200 + i * 30, 200, 30);
                    labelGraph.setForeground(Color.WHITE);
                    labelGraph.setHorizontalAlignment(SwingConstants.RIGHT);
                    panel.add(labelGraph);

                    JLabel labelNum = new JLabel(String.valueOf(f.getNbCommandes()));
                    labelNum.setBounds(200 + 50 * f.getNbCommandes(), 200 + i * 30, 20, 30);
                    labelNum.setForeground(Color.BLACK);
                    labelNum.setHorizontalAlignment(SwingConstants.RIGHT);
                    if(f.getNbCommandes() == 0) {
                        labelNum.setForeground(Color.WHITE);
                        labelNum.setBounds(260, 200 + i * 30, 20, 30);
                    } else {
                        labelNum.setForeground(Color.BLACK);
                    }
                    panel.add(labelNum);
                    i++;
                }
            }
        };
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("Button.background", Color.WHITE);
        UIManager.put("Button.foreground", Color.BLACK);
        UIManager.put("Button.border", BorderFactory.createLineBorder(Color.WHITE));
        UIManager.put("Button.focus", Color.WHITE);

        //Onglet bouton films
        btnFilms = new JButton("Films");
        btnFilms.setBounds(100, 60, 100, 30);
        btnFilms.setForeground(Color.BLACK);
        btnFilms.setBackground(new Color(100, 100, 100));
        btnFilms.setOpaque(false);
        btnFilms.setContentAreaFilled(false);
        btnFilms.setBorderPainted(false);
        panel.add(btnFilms);

        //Onglet bouton comptes
        btnComptes = new JButton("Comptes");
        btnComptes.setBounds(200, 60, 100, 30);
        btnComptes.setForeground(Color.WHITE);
        btnComptes.setOpaque(false);
        btnComptes.setContentAreaFilled(false);
        btnComptes.setBorderPainted(false);
        panel.add(btnComptes);

        //Onglet bouton reductions
        btnReduc = new JButton("Reductions");
        btnReduc.setBounds(300, 60, 100, 30);
        btnReduc.setForeground(Color.WHITE);
        btnReduc.setOpaque(false);
        btnReduc.setContentAreaFilled(false);
        btnReduc.setBorderPainted(false);
        panel.add(btnReduc);

        //Onglet bouton statistiques
        btnStat = new JButton("Statistiques");
        btnStat.setBounds(400, 60, 100, 30);
        btnStat.setForeground(Color.WHITE);
        btnStat.setOpaque(false);
        btnStat.setContentAreaFilled(false);
        btnStat.setBorderPainted(false);
        panel.add(btnStat);

        //Ajout du JLabel pour afficher le nom du client
        JLabel labelNom = new JLabel("Connecté en tant que " + membre.getPrenom() + " " + membre.getNom());
        labelNom.setBounds(1100, 30, 300, 30);
        labelNom.setForeground(Color.WHITE);
        labelNom.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(labelNom);
        panel.setLayout(null);





        panel.setLayout(null);
        add(panel);
        setVisible(true);
    }

    public void addListenerOngletComptes(ActionListener listener){
        btnComptes.addActionListener(listener);
    }
    public void addListenerOngletReduc(ActionListener listener){
        btnReduc.addActionListener(listener);
    }
    public void addListenerOngletStat(ActionListener listener){
        btnStat.addActionListener(listener);
    }
    public void closeWindow(){setVisible(false);dispose();}
}