package Vue;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIconnexion extends JFrame {
    private JTextField textEmail;
    private JTextField textMdp;
    public GUIconnexion() {
        super("Connexion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(500, 600);

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

        //Section E-mail
        JLabel labelEmail = new JLabel("E-mail");
        labelEmail.setBounds(225, 200, 50, 30);
        labelEmail.setFont(new Font("Arial", Font.BOLD, 15));
        labelEmail.setForeground(Color.WHITE);
        drawComponents.add(labelEmail);

        textEmail = new JTextField();
        textEmail.setBounds(150, 250, 200, 30);
        drawComponents.add(textEmail);

        //Section Mot de passe
        JLabel labelMdp = new JLabel("Mot de passe");
        labelMdp.setBounds(200, 300, 100, 30);
        labelMdp.setFont(new Font("Arial", Font.BOLD, 15));
        labelMdp.setForeground(Color.WHITE);
        drawComponents.add(labelMdp);

        textMdp = new JTextField();
        textMdp.setBounds(150, 350, 200, 30);
        drawComponents.add(textMdp);

        //Section bouton creer un nouveau compte
        JButton boutonCreer = new JButton("Se créer un compte");
        boutonCreer.setBounds(150, 380, 200, 30);
        boutonCreer.setForeground(Color.GRAY);
        boutonCreer.setOpaque(false);
        boutonCreer.setContentAreaFilled(false);
        boutonCreer.setBorderPainted(false);
        drawComponents.add(boutonCreer);

        //Section bouton se connecter
        JButton boutonConnexion = new JButton("Se connecter");
        boutonConnexion.setBounds(190, 430, 125, 30);
        boutonConnexion.setForeground(Color.WHITE);
        boutonConnexion.setBackground(new Color(100, 100, 100));
        drawComponents.add(boutonConnexion);

        boutonConnexion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = getEmail();
                String motDePasse = getMotDePasse();

                System.out.println("Email: " + email);
                System.out.println("Mot de passe: " + motDePasse);
            }
        });


        drawComponents.setLayout(null);
        add(drawComponents);

        setVisible(true);
    }

    //Getters
    public String getEmail() {
        return textEmail.getText();
    }

    public String getMotDePasse() {
        return textMdp.getText();
    }
}
