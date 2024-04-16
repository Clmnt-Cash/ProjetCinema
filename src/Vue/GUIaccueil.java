package Vue;

import Controleur.ControleurAccueil;
import Modele.Client;
import Modele.Film;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GUIaccueil extends JFrame {
    //Attributs
    private Map<JButton, Film> boutonFilmMap;
    private ArrayList<Film> films;
    private Client client;
    private ControleurAccueil controleurAccueil;
    private ArrayList<JButton> boutons;
    private JPanel scrollablePanel;
    private JLabel boutonDeconnexion;
    private JButton boutonPanier;

    //Constructeur
    public GUIaccueil(Client client, ControleurAccueil controleurAccueil) {
        super("Cinéma");
        this.client = client;
        this.controleurAccueil = controleurAccueil;
        this.films = controleurAccueil.getFilms();
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
        boutonPanier = new JButton();


        if(this.client.getType() != -1) {
            //Ajout du JLabel pour afficher le nom du client
            JLabel labelNom = new JLabel("Connecté en tant que " + client.getPrenom() + " " + client.getNom());
            labelNom.setFont(labelNom.getFont().deriveFont(Font.BOLD, 15));
            Dimension size = labelNom.getPreferredSize();
            labelNom.setBounds(1470 - size.width, 10, size.width, size.height);
            labelNom.setForeground(Color.WHITE);
            panel.add(labelNom);
            panel.setLayout(null);

            //Création d'un bouton avec une image
            ImageIcon iconLogoPanier = new ImageIcon("images/logos/logopanier.png");
            Image imagePanier = iconLogoPanier.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            ImageIcon resizedimagePanier = new ImageIcon(imagePanier);
            boutonPanier.setIcon(resizedimagePanier);
            boutonPanier.setBounds(1330, 40, 40, 40);
            boutonPanier.setBorderPainted(false);
            boutonPanier.setFocusPainted(false);
            boutonPanier.setContentAreaFilled(false);
            panel.add(boutonPanier);
        } else {
            //Ajout du JLabel pour afficher invité
            JLabel labelNom = new JLabel("Connecté en tant qu'invité ");
            labelNom.setFont(labelNom.getFont().deriveFont(Font.BOLD, 15));
            Dimension size = labelNom.getPreferredSize();
            labelNom.setBounds(1470 - size.width, 10, size.width, size.height);
            labelNom.setForeground(Color.WHITE);
            panel.add(labelNom);
            panel.setLayout(null);
        }


        scrollablePanel = new JPanel();
        scrollablePanel.setLayout(new GridLayout(0, films.size()));
        boutonFilmMap = new HashMap<>();

        for (Film f : films) {
            JButton button = new JButton();

            ImageIcon imageIcon = new ImageIcon("images/affiches/" + f.getCheminImage());

            if (imageIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                Image image = imageIcon.getImage().getScaledInstance(500, 700, Image.SCALE_SMOOTH);
                ImageIcon resizedIcon = new ImageIcon(image);
                button.setIcon(resizedIcon);
            } else {
                button.setText("Image indisponible");
            }
            button.setPreferredSize(new Dimension(500, 200));

            JPanel overlayPanel = new JPanel();
            button.setBorderPainted(false);
            overlayPanel.setBackground(new Color(0, 0, 0, 100));

            overlayPanel.setVisible(false);
            button.setLayout(new BorderLayout());
            button.add(overlayPanel, BorderLayout.CENTER);

            JLabel labelTitre = new JLabel();
            labelTitre.setForeground(Color.WHITE);
            labelTitre.setHorizontalAlignment(SwingConstants.LEADING);
            labelTitre.setVerticalAlignment(SwingConstants.CENTER);
            overlayPanel.add(labelTitre);

            Font police = new Font("Arial", Font.BOLD, 25);

            labelTitre.setFont(police);
            boutons.add(button);

            boutonFilmMap.put(button, f);

            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    overlayPanel.setVisible(true);
                    labelTitre.setText(f.getTitre());
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    overlayPanel.setVisible(false);
                    labelTitre.setText("");
                }
            });
        }
        for (JButton button : boutons) {
            scrollablePanel.add(button);
        }

        JScrollPane scrollPane = new JScrollPane(scrollablePanel);
        scrollPane.setBounds(10, 110, 1463, 670);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        panel.add(scrollPane);
        panel.setLayout(null);
        add(panel);
    }

    public void closeWindow(){setVisible(false);dispose();}
    public void openWindow(){setVisible(true);}
    public void addMouseListenerBoutonDeconnexion(MouseListener listener) {
        boutonDeconnexion.addMouseListener(listener);
    }

    public void addListenerPanier(ActionListener listener){if(this.client.getId() != -1)boutonPanier.addActionListener(listener);}

}
