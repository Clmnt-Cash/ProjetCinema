package Vue;

import Modele.Client;
import Modele.Film;
import Controleur.ControleurMembre;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GUImembre extends JFrame {
    //Attributs
    private Map<JButton, Film> boutonFilmMap;
    private ArrayList<Film> films;
    private Client membre;
    private ControleurMembre controleurMembre;
    private ArrayList<JButton> boutons;
    private JButton btnFilms;
    private JButton btnComptes;
    private JPanel scrollablePanel;

    //Constructeur
    public GUImembre(Client membre, ControleurMembre controleurMembre) {
        super("Cinéma");
        this.membre = membre;
        this.controleurMembre = controleurMembre;
        this.films = controleurMembre.getFilms();
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


    JPanel drawComponents = new JPanel() {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(new Color(0, 0, 0));
            g.fillRect(0, 100, 1500, 700);
            g.setColor(new Color(50, 50, 50, 200));
            g.fillRect(0, 0, 1500, 100);
            ImageIcon logo = new ImageIcon("images/logos/logo_black.png");
            Image image = logo.getImage();
            g.drawImage(image, 200, 0, 100, 100, this);
        }
    };

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

        //Onglet bouton films
        btnFilms = new JButton("Films");
        btnFilms.setBounds(500, 30, 100, 30);
        btnFilms.setForeground(Color.WHITE);
        btnFilms.setBackground(new Color(100, 100, 100));
        drawComponents.add(btnFilms);

        //Onglet bouton comptes
        btnComptes = new JButton("Comptes");
        btnComptes.setBounds(650, 30, 100, 30);
        btnComptes.setForeground(Color.WHITE);
        btnComptes.setOpaque(false);
        btnComptes.setContentAreaFilled(false);
        btnComptes.setBorderPainted(false);
        drawComponents.add(btnComptes);

        //Ajout du JLabel pour afficher le nom du client
        JLabel labelNom = new JLabel("Connecté en tant que " + membre.getPrenom() + " " + membre.getNom());
        labelNom.setBounds(1100, 30, 300, 30);
        labelNom.setForeground(Color.WHITE);
        labelNom.setHorizontalAlignment(SwingConstants.RIGHT);
        panel.add(labelNom);
        panel.setLayout(null);

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
}