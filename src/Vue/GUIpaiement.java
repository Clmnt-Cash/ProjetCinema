package Vue;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIpaiement extends JFrame {
    private JTextField textCode;
    private JTextField textDate;
    private JTextField textCVV;
    private JButton boutonAccepter;
    private JLabel errorLabel;


    public GUIpaiement() {
        super("Paiement");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(500, 800);

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

        //Section Code
        JLabel labelCode = new JLabel("Nom");
        labelCode.setBounds(225, 200, 50, 30);
        labelCode.setFont(new Font("Arial", Font.BOLD, 15));
        labelCode.setForeground(Color.WHITE);
        drawComponents.add(labelCode);

        textCode = new JTextField();
        textCode.setBounds(150, 250, 200, 30);
        drawComponents.add(textCode);

        //Section Date
        JLabel labelDate = new JLabel("Prenom");
        labelDate.setBounds(225, 300, 100, 30);
        labelDate.setFont(new Font("Arial", Font.BOLD, 15));
        labelDate.setForeground(Color.WHITE);
        drawComponents.add(labelDate);

        textDate = new JTextField();
        textDate.setBounds(150, 350, 200, 30);
        drawComponents.add(textDate);

        //Section Age
        JLabel labelCVV = new JLabel("Age");
        labelCVV.setBounds(225, 400, 50, 30);
        labelCVV.setFont(new Font("Arial", Font.BOLD, 15));
        labelCVV.setForeground(Color.WHITE);
        drawComponents.add(labelCVV);

        textCVV = new JTextField();
        textCVV.setBounds(150, 450, 200, 30);
        drawComponents.add(textCVV);

        //Section bouton créer son compte
        boutonAccepter = new JButton("Créer");
        boutonAccepter.setBounds(190, 550, 125, 30);
        boutonAccepter.setForeground(Color.WHITE);
        boutonAccepter.setBackground(new Color(100, 100, 100));
        drawComponents.add(boutonAccepter);

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
        boutonAccepter.addActionListener(listener);
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

    public void closeWindow(){
        setVisible(false);
    }

    //Getters
    public String getCode() {
        return textCode.getText();
    }
    public String getDate() {
        return textDate.getText();
    }
    public String getCVV() {
        return textCVV.getText();
    }
}