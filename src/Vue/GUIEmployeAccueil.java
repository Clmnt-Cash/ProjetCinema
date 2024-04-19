package Vue;

import Controleur.ControleurEmployeAccueil;
import Modele.Client;
import Modele.Film;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GUIEmployeAccueil extends JFrame {
    //Attributs
    private Map<JButton, Film> boutonFilmMap;
    private ArrayList<Film> films;
    private Client membre;
    private ControleurEmployeAccueil controleurEmployeAccueil;
    private ArrayList<JButton> boutons;
    private JButton btnFilms;
    private JButton btnComptes;
    private JButton btnReduc;
    private JButton btnStat;
    private JPanel scrollablePanel;
    private JPanel panel;
    private JButton boutonAjouterFilm;
    private JLabel boutonDeconnexion;

    //Constructeur
    public GUIEmployeAccueil(Client membre, ControleurEmployeAccueil controleurEmployeAccueil) {
        super("Cinéma");
        this.membre = membre;
        this.controleurEmployeAccueil = controleurEmployeAccueil;
        this.films = controleurEmployeAccueil.getFilms();
        this.boutons = new ArrayList<>();
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

    //Méthode pour ajouter un listener sur les boutons
    public void addListener(ActionListener listener) {
        for(JButton b : boutons) {
            b.addActionListener(listener);
        }
    }

    public Map<JButton, Film> getBoutonFilmMap() {
        return boutonFilmMap;
    }

    public void changerAffichage(Component nouveauContenu) {
        getContentPane().removeAll();
        getContentPane().add(nouveauContenu);
        revalidate();
        repaint();
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


        //Bouton déconnexion
        boutonDeconnexion = new JLabel("Déconnexion");
        boutonDeconnexion.setFont(boutonDeconnexion.getFont().deriveFont(Font.BOLD, 12));
        boutonDeconnexion.setBounds(1400, 50, 100, 20);
        boutonDeconnexion.setForeground(Color.WHITE);
        panel.add(boutonDeconnexion);

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

        //Label pour le nom de la personne connectée
        JLabel labelNom = new JLabel("Connecté en tant que " + membre.getPrenom() + " " + membre.getNom() + " (employé)");
        labelNom.setFont(labelNom.getFont().deriveFont(Font.BOLD, 15));
        Dimension size = labelNom.getPreferredSize();
        labelNom.setBounds(1470 - size.width, 10, size.width, size.height);
        labelNom.setForeground(Color.WHITE);
        panel.add(labelNom);
        panel.setLayout(null);

        JLabel labelModifier = new JLabel("Modifier les films");
        labelModifier.setBounds(540, 120, 300, 30);
        labelModifier.setFont(labelNom.getFont().deriveFont(Font.BOLD, 25));
        labelModifier.setForeground(Color.WHITE);
        labelModifier.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(labelModifier);

        scrollablePanel = new JPanel();
        scrollablePanel.setLayout(new GridLayout(0, films.size()));
        boutonFilmMap = new HashMap<>();


        for (Film f : films) {
            JButton button = new JButton();

            ImageIcon imageIcon = new ImageIcon("images/affiches/" + f.getCheminImage());

            if (imageIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                Image image = imageIcon.getImage().getScaledInstance(350, 490, Image.SCALE_SMOOTH);
                ImageIcon resizedIcon = new ImageIcon(image);
                button.setIcon(resizedIcon);
            } else {
                button.setText("Image indisponible");
            }
            button.setPreferredSize(new Dimension(350, 100));

            JPanel overlayPanel = new JPanel();
            button.setBorderPainted(false);
            overlayPanel.setBackground(new Color(0, 0, 0, 100));

            overlayPanel.setVisible(false);
            button.setLayout(new BorderLayout());
            button.add(overlayPanel, BorderLayout.CENTER);
            boutons.add(button);

            boutonFilmMap.put(button, f);

            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    overlayPanel.setVisible(true);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    overlayPanel.setVisible(false);
                }
            });
        }
        for (JButton button : boutons) {
            scrollablePanel.add(button);
        }

        JScrollPane scrollPane = new JScrollPane(scrollablePanel);
        scrollPane.setBounds(10, 160, 1463, 490);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPane);


        ImageIcon iconLogoPanier = new ImageIcon("images/logos/logoajouterfilm.png");
        Image imagePanier = iconLogoPanier.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        ImageIcon resizedimagePanier = new ImageIcon(imagePanier);
        boutonAjouterFilm = new JButton();
        boutonAjouterFilm.setIcon(resizedimagePanier);
        boutonAjouterFilm.setBounds(1420, 700, 50, 50);
        boutonAjouterFilm.setBorderPainted(false);
        boutonAjouterFilm.setFocusPainted(false);
        boutonAjouterFilm.setContentAreaFilled(false);
        panel.add(boutonAjouterFilm);

        panel.setLayout(null);
        add(panel);
    }

    public void addListenerAjouter(ActionListener listener){
        boutonAjouterFilm.addActionListener(listener);
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
    public void addMouseListenerBoutonDeconnexion(MouseListener listener) {
        boutonDeconnexion.addMouseListener(listener);
    }
    public void closeWindow(){setVisible(false);dispose();}
    public void openWindow(){setVisible(true);}
}