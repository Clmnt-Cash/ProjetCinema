package Vue;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIcrea extends JFrame {
    private JTextField textNom;
    private JTextField textPrenom;
    private JTextField textAge;
    private JTextField textEmail;
    private JTextField textMdp;
    private JTextField textMdpConf;
    private JButton boutonCreer;
    private JLabel errorLabel;
    public GUIcrea() {
        super("Creation de compte");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(500, 1000);

        //Obtenir les dimensions de l'écran
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getWidth()) / 2;
        int y = (screenSize.height - getHeight()) / 2;
        setLocation(x, y);

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

        //Section Nom
        JLabel labelNom = new JLabel("Nom");
        labelNom.setBounds(225, 200, 50, 30);
        labelNom.setFont(new Font("Arial", Font.BOLD, 15));
        labelNom.setForeground(Color.WHITE);
        drawComponents.add(labelNom);

        textNom = new JTextField();
        textNom.setBounds(150, 250, 200, 30);
        drawComponents.add(textNom);

        //Section Prenom
        JLabel labelPrenom = new JLabel("Prenom");
        labelPrenom.setBounds(225, 300, 100, 30);
        labelPrenom.setFont(new Font("Arial", Font.BOLD, 15));
        labelPrenom.setForeground(Color.WHITE);
        drawComponents.add(labelPrenom);

        textPrenom = new JTextField();
        textPrenom.setBounds(150, 350, 200, 30);
        drawComponents.add(textPrenom);

        //Section Age
        JLabel labelAge = new JLabel("Age");
        labelAge.setBounds(225, 400, 50, 30);
        labelAge.setFont(new Font("Arial", Font.BOLD, 15));
        labelAge.setForeground(Color.WHITE);
        drawComponents.add(labelAge);

        textAge = new JTextField();
        textAge.setBounds(150, 450, 200, 30);
        drawComponents.add(textAge);

        //Section E-mail
        JLabel labelEmail = new JLabel("E-mail");
        labelEmail.setBounds(225, 500, 50, 30);
        labelEmail.setFont(new Font("Arial", Font.BOLD, 15));
        labelEmail.setForeground(Color.WHITE);
        drawComponents.add(labelEmail);

        textEmail = new JTextField();
        textEmail.setBounds(150, 550, 200, 30);
        drawComponents.add(textEmail);

        //Section Mot de passe
        JLabel labelMdp = new JLabel("Mot de passe");
        labelMdp.setBounds(200, 600, 100, 30);
        labelMdp.setFont(new Font("Arial", Font.BOLD, 15));
        labelMdp.setForeground(Color.WHITE);
        drawComponents.add(labelMdp);

        textMdp = new JTextField();
        textMdp.setBounds(150, 650, 200, 30);
        drawComponents.add(textMdp);

        //Section Mot de passe pour confirmer
        JLabel labelMdpConf = new JLabel("Confirmer Mot de passe");
        labelMdpConf.setBounds(200, 700, 200, 30);
        labelMdpConf.setFont(new Font("Arial", Font.BOLD, 15));
        labelMdpConf.setForeground(Color.WHITE);
        drawComponents.add(labelMdp);

        textMdpConf = new JTextField();
        textMdpConf.setBounds(150, 750, 200, 30);
        drawComponents.add(textMdpConf);

        //Section bouton créer son compte
        boutonCreer = new JButton("Créer");
        boutonCreer.setBounds(190, 850, 125, 30);
        boutonCreer.setForeground(Color.WHITE);
        boutonCreer.setBackground(new Color(100, 100, 100));
        drawComponents.add(boutonCreer);

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

    //Méthode pour ajouter un ActionListener au bouton de creation
    public void addConnexionListener(ActionListener listener) {
        boutonCreer.addActionListener(listener);
    }
    //Message d'erreur
    public void displayError(String message){
        errorLabel.setText(message); // Afficher le message d'erreur dans le label
        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                errorLabel.setText("");
            }
        });
        timer.setRepeats(false);
        timer.start();
    }


    //Getters
    public String getNom() {
        return textNom.getText();
    }
    public String getPrenom() {
        return textPrenom.getText();
    }
    public String getAge() {
        return textAge.getText();
    }
    public String getEmail() {
        return textEmail.getText();
    }
    public String getMotDePasse() {
        return textMdp.getText();
    }
    public String getMotDePasseConf() {
        return textMdpConf.getText();
    }
}

