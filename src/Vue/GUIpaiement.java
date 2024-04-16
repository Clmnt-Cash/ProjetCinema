package Vue;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUIpaiement extends JFrame {
    private JTextField textCode;
    private JTextField textDate;
    private JTextField textCVV;
    private JButton boutonPayer;
    private JLabel errorLabel;


    public GUIpaiement(float prix) {
        super("Paiement");
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

        //Section bouton pour
        boutonPayer = new JButton("Payer " + prix + "€");
        boutonPayer.setBounds(190, 400, 125, 30);
        boutonPayer.setForeground(Color.WHITE);
        boutonPayer.setBackground(new Color(100, 100, 100));
        drawComponents.add(boutonPayer);

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
    public void addListenerPayer(ActionListener listener) {
        boutonPayer.addActionListener(listener);
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
    public String getCVV() {
        return textCVV.getText();
    }

}