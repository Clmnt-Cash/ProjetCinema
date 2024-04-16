package Vue;
import Controleur.ControleurAccueil;
import Controleur.Connexion;
import Controleur.ControleurConnexion;
import Controleur.ControleurPanier;
import Modele.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class GUIpaiement extends JFrame {
    private JTextField textCode;
    private JTextField textDate;
    private JTextField textCVV;
    private JButton boutonPayer;
    private JLabel errorLabel;
    private JPanel drawComponents;
    private Connexion conn;
    private Client client;
    private JLabel boutonRetour;



    public GUIpaiement(float prix, Connexion conn, Client client) {
        super("Paiement");
        this.conn = conn;
        this.client = client;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(500, 600);

        //Obtenir les dimensions de l'écran
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);

        drawComponents = new JPanel() {
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

        boutonRetour = new JLabel("Retour");
        boutonRetour.setFont(boutonRetour.getFont().deriveFont(Font.BOLD, 12));
        boutonRetour.setBounds(20, 110, 100, 20);
        boutonRetour.setForeground(Color.WHITE);
        drawComponents.add(boutonRetour);
        MouseListener mouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dispose();
                if(client.getType() != -1) {
                    ControleurPanier controleurPanier = new ControleurPanier(conn, client);
                    GUIpanier vuePanier = new GUIpanier(client, controleurPanier);
                    controleurPanier.setVue(vuePanier);
                } else {
                    ControleurAccueil controleurAccueil = new ControleurAccueil(conn);
                    GUIaccueil vueAccueil = new GUIaccueil(client, controleurAccueil);
                    controleurAccueil.setVue(vueAccueil);
                    controleurAccueil.setClient(client);
                    controleurAccueil.openWindow();
                }
            }
        };
        addMouseListenerBoutonRetour(mouseListener);

        //Section Code
        JLabel labelCode = new JLabel("Numéro de carte");
        labelCode.setBounds(180, 200, 120, 30);
        labelCode.setFont(new Font("Arial", Font.BOLD, 15));
        labelCode.setForeground(Color.WHITE);
        drawComponents.add(labelCode);

        textCode = new JTextField();
        textCode.setBounds(150, 230, 200, 30);
        drawComponents.add(textCode);

        //Section Age
        JLabel labelCVV = new JLabel("CVV");
        labelCVV.setBounds(150, 300, 50, 30);
        labelCVV.setFont(new Font("Arial", Font.BOLD, 15));
        labelCVV.setForeground(Color.WHITE);
        drawComponents.add(labelCVV);

        textCVV = new JTextField();
        textCVV.setBounds(150, 330, 30, 30);
        drawComponents.add(textCVV);

        JLabel labelDate = new JLabel("Date d'expiration");
        labelDate.setBounds(220, 300, 150, 30);
        labelDate.setFont(new Font("Arial", Font.BOLD, 15));
        labelDate.setForeground(Color.WHITE);
        drawComponents.add(labelDate);

        String[] mois = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        JComboBox<String> comboMois = new JComboBox<>(mois);
        comboMois.setBounds(250, 330, 50, 30);
        drawComponents.add(comboMois);

        String[] annee = {"24", "25", "26", "27", "28", "29", "30", "31", "32", "33"};
        JComboBox<String> comboAnnee = new JComboBox<>(annee);
        comboAnnee.setBounds(300, 330, 50, 30);
        drawComponents.add(comboAnnee);

        //Section bouton pour payer
        boutonPayer = new JButton("Payer " + prix + "€");
        boutonPayer.setBounds(190, 400, 125, 30);
        boutonPayer.setForeground(Color.WHITE);
        boutonPayer.setBackground(new Color(100, 100, 100));
        drawComponents.add(boutonPayer);

        //Ajout d'un ActionListener
        boutonPayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Vérifie si tous les champs sont remplis
                if (textCode.getText().isEmpty() || textCVV.getText().isEmpty()) {
                    displayError("Veuillez remplir tous les champs.");
                    return;
                }


                //Vérifie si le numéro de carte est un nombre de 16 chiffres
                String code = textCode.getText();
                if (!code.matches("\\d{16}")) {
                    displayError("Numéro de carte non-valide.");
                    return;
                }

                //Vérifie si le CVV est un nombre en dessous de 100
                if(textCVV.getText().length()!=3){
                    displayError("CVV non-valide.");
                    return;
                }
                String cvvText = textCVV.getText();

                int cvv = Integer.parseInt(cvvText);
                if (cvv >= 1000) {
                    displayError("CVV non-valide.");
                }
                paiement();
            }
        });



        //Label erreur
        errorLabel = new JLabel();
        errorLabel.setForeground(Color.RED);
        errorLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        errorLabel.setBounds(100, 500, 300, 30);
        errorLabel.setHorizontalAlignment(SwingConstants.CENTER);
        drawComponents.add(errorLabel);

        //Ajoute un timer pour supprimer le label après 3 secondes
        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                errorLabel.setText("");
            }
        });
        timer.setRepeats(false);

        drawComponents.setLayout(null);
        add(drawComponents);

        setVisible(true);
    }


    //Message d'erreur
    public void displayError(String message){
        errorLabel.setText(message); //Afficher le message d'erreur dans le label
        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                errorLabel.setText("");
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    public void paiement() {
        drawComponents.removeAll();
        drawComponents.repaint();

        // Création de la barre de progression
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setBounds(50, 250, 400, 20);

        JLabel labelTraitement = new JLabel("Traitement en cours, veuillez patienter...");
        labelTraitement.setForeground(Color.WHITE);
        labelTraitement.setFont(new Font("Arial", Font.PLAIN, 16));
        labelTraitement.setBounds(40, 170, 400, 30);
        labelTraitement.setHorizontalAlignment(SwingConstants.CENTER);
        drawComponents.add(labelTraitement);

        //Ajout de la barre de progression à drawComponents
        drawComponents.add(progressBar);

        drawComponents.setLayout(null);
        drawComponents.setPreferredSize(new Dimension(1500, 800));

        //Ajout de drawComponents à la fenêtre principale
        add(drawComponents);

        setVisible(true);
        //Progression de la barre
        Thread updateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 100; i++) {
                    progressBar.setValue(i);
                    try {
                        Thread.sleep(50); //Pause de 50 millisecondes entre chaque mise à jour
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }

                //Une fois la barre de progression terminée, on supprime les labels et on rebascule sur le menu
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisible(false);
                        labelTraitement.setText("Traitement effectué ! Redirection vers la page d'accueil");


                        // Fermer la fenêtre après 3 secondes
                        Timer timer = new Timer(3000, e -> {
                            dispose();
                            ControleurAccueil controleurAccueil = new ControleurAccueil(conn);
                            GUIaccueil vueAccueil = new GUIaccueil(client, controleurAccueil);
                            controleurAccueil.setVue(vueAccueil);
                            controleurAccueil.setClient(client);
                            controleurAccueil.openWindow();
                            supprimerCommande(client.getId());
                        });
                        timer.setRepeats(false);
                        timer.start();
                    }
                });
            }
        });
        updateThread.start();
    }

    public void supprimerCommande(int idClient){
        try {
            String requeteInsertion = "DELETE FROM commande WHERE ID_client = " + idClient;
            conn.executerRequete(requeteInsertion);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Erreur lors de la connexion à la base de données : " + e);
        }
    }
    public void addMouseListenerBoutonRetour(MouseListener listener) {
        boutonRetour.addMouseListener(listener);
    }

    public void closeWindow(){
        setVisible(false);
    }
}