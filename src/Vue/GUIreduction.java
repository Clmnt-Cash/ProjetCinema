package Vue;

import Controleur.ControleurComptes;
import Controleur.ControleurReduction;
import Modele.Client;
import Modele.Reduction;
import Modele.Seance;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GUIreduction extends JFrame {
    //Attributs
    private Client employe;
    private ControleurReduction controleurReduction;
    private JButton btnFilms;
    private JButton btnComptes;
    private JButton btnReduc;
    private JButton btnStat;
    private JPanel panel;
    private JLabel boutonDeconnexion;
    private Reduction reduc;
    private JButton boutonEnregistrer;
    private JSpinner spinnerE, spinnerR, spinnerS;

    //Constructeur
    public GUIreduction(Client employe, ControleurReduction controleurReduction) {
        super("Cinéma");
        this.employe = employe;
        this.controleurReduction = controleurReduction;
        this.reduc = controleurReduction.getReduction();
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
        btnFilms.setForeground(Color.WHITE);
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
        btnReduc.setForeground(Color.BLACK);
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

        //Label pour le nom de la personne connectée
        JLabel labelNom = new JLabel("Connecté en tant que " + employe.getPrenom() + " " + employe.getNom() + " (employé)");
        labelNom.setFont(labelNom.getFont().deriveFont(Font.BOLD, 15));
        Dimension size = labelNom.getPreferredSize();
        labelNom.setBounds(1470 - size.width, 10, size.width, size.height);
        labelNom.setForeground(Color.WHITE);
        panel.add(labelNom);

        //Bouton déconnexion
        boutonDeconnexion = new JLabel("Déconnexion");
        boutonDeconnexion.setFont(boutonDeconnexion.getFont().deriveFont(Font.BOLD, 12));
        boutonDeconnexion.setBounds(1400, 50, 100, 20);
        boutonDeconnexion.setForeground(Color.WHITE);
        panel.add(boutonDeconnexion);

        JLabel labelGerer = new JLabel("Gérer les réductions");
        labelGerer.setFont(labelGerer.getFont().deriveFont(Font.BOLD, 20));
        Dimension size4 = labelGerer.getPreferredSize();
        labelGerer.setBounds(750 - size4.width/2, 130, size4.width, size4.height);
        labelGerer.setForeground(Color.WHITE);
        panel.add(labelGerer);

        JLabel labelEnfant = new JLabel("Réduction pour les enfants : ");
        labelEnfant.setFont(labelEnfant.getFont().deriveFont(Font.BOLD, 15));
        Dimension sizeE = labelEnfant.getPreferredSize();
        labelEnfant.setBounds(750 - sizeE.width, 200, sizeE.width, sizeE.height);
        labelEnfant.setForeground(Color.WHITE);
        panel.add(labelEnfant);

        JLabel labelRegulier = new JLabel("Réduction pour les réguliers : ");
        labelRegulier.setFont(labelRegulier.getFont().deriveFont(Font.BOLD, 15));
        Dimension sizeR = labelRegulier.getPreferredSize();
        labelRegulier.setBounds(750 - sizeR.width, 250, sizeR.width, sizeR.height);
        labelRegulier.setForeground(Color.WHITE);
        panel.add(labelRegulier);

        JLabel labelSenior = new JLabel("Réduction pour les seniors : ");
        labelSenior.setFont(labelSenior.getFont().deriveFont(Font.BOLD, 15));
        Dimension sizeS = labelSenior.getPreferredSize();
        labelSenior.setBounds(750 - sizeS.width, 300, sizeS.width, sizeS.height);
        labelSenior.setForeground(Color.WHITE);
        panel.add(labelSenior);

        SpinnerNumberModel spinnerModelE = new SpinnerNumberModel(reduc.getReductionEnfant(), 1, 100, 1);
        spinnerE = new JSpinner(spinnerModelE);
        spinnerE.setBounds(770, 200, 50, 30);
        panel.add(spinnerE);

        SpinnerNumberModel spinnerModelR = new SpinnerNumberModel(reduc.getReductionRegulier(), 1, 100, 1);
        spinnerR = new JSpinner(spinnerModelR);
        spinnerR.setBounds(770, 250, 50, 30);
        panel.add(spinnerR);

        SpinnerNumberModel spinnerModelS = new SpinnerNumberModel(reduc.getReductionSenior(), 1, 100, 1);
        spinnerS = new JSpinner(spinnerModelS);
        spinnerS.setBounds(770, 300, 50, 30);
        panel.add(spinnerS);

        boutonEnregistrer = new JButton("Enregistrer");
        boutonEnregistrer.setBounds(1330, 700, 150, 50);
        boutonEnregistrer.setFont(new Font("Arial", Font.BOLD, 20));
        boutonEnregistrer.setForeground(Color.WHITE);
        boutonEnregistrer.setBackground(Color.GRAY);
        panel.add(boutonEnregistrer);

        panel.setLayout(null);
        add(panel);
        setVisible(true);
    }
    public void addListenerOngletFilms(ActionListener listener){
        btnFilms.addActionListener(listener);
    }
    public void addListenerOngletComptes(ActionListener listener){
        btnComptes.addActionListener(listener);
    }
    public void addMouseListenerBoutonDeconnexion(MouseListener listener) {
        boutonDeconnexion.addMouseListener(listener);
    }
    public void addListenerEnregistrer(ActionListener listener){boutonEnregistrer.addActionListener(listener);}
    public void addListenerOngletStat(ActionListener listener){
        btnStat.addActionListener(listener);
    }
    public void closeWindow(){setVisible(false);dispose();}

    //Getters
    public Reduction getNewReduction(){
        int valeurE = (int) spinnerE.getValue();
        int valeurR = (int) spinnerR.getValue();
        int valeurS = (int) spinnerS.getValue();

        Reduction reduc = new Reduction(valeurE, valeurR, valeurS);
        return reduc;
    }


}