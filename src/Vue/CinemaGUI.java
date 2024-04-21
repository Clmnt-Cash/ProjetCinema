package Vue;

import Controleur.FilmController;
import Modele.Film;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

// Interface graphique pour la gestion des films dans un cinéma
public class CinemaGUI extends JFrame {

    private FilmController filmController; // Contrôleur pour les opérations sur les films
    private JList<Film> filmList; // Liste affichant les films
    private DefaultListModel<Film> filmListModel; // Modèle de données pour la liste de films

    public CinemaGUI() {
        setTitle("Gestion des Films"); // Titre de la fenêtre
        setSize(400, 300); // Dimension de la fenêtre
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Comportement à la fermeture

        filmController = new FilmController(); // Initialisation du contrôleur

        JPanel mainPanel = new JPanel(new BorderLayout()); // Panneau principal avec layout BorderLayout

        // Configuration de la liste de films
        filmListModel = new DefaultListModel<>();
        filmList = new JList<>(filmListModel);
        JScrollPane scrollPane = new JScrollPane(filmList); // ScrollPane pour la liste
        mainPanel.add(scrollPane, BorderLayout.CENTER); // Ajout du ScrollPane au centre du panneau principal

        // Panneau pour les boutons
        JPanel buttonPanel = new JPanel(new FlowLayout()); // Layout pour aligner les boutons
        JButton addButton = new JButton("Ajouter"); // Bouton pour ajouter un film
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Action pour ajouter un film
                Film newFilm = new Film(0, "Nouveau Film", "Réalisateur", "Synopsis", "CheminImage");
                filmController.addFilm(newFilm);
                refreshFilmList(); // Rafraîchissement de la liste après ajout
            }
        });
        buttonPanel.add(addButton);

        JButton updateButton = new JButton("Mettre à Jour"); // Bouton pour mettre à jour un film
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Action pour mettre à jour le film sélectionné
                Film selectedFilm = filmList.getSelectedValue();
                if (selectedFilm != null) {
                    selectedFilm.setTitre("Titre mis à jour");
                    selectedFilm.setRealisateur("Réalisateur mis à jour");
                    selectedFilm.setSynopsis("Synopsis mis à jour");
                    selectedFilm.setCheminImage("CheminImage mis à jour");
                    filmController.updateFilm(selectedFilm);
                    refreshFilmList(); // Rafraîchissement de la liste après mise à jour
                }
            }
        });
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("Supprimer"); // Bouton pour supprimer un film
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Action pour supprimer le film sélectionné
                Film selectedFilm = filmList.getSelectedValue();
                if (selectedFilm != null) {
                    filmController.deleteFilm(selectedFilm.getId());
                    refreshFilmList(); // Rafraîchissement de la liste après suppression
                }
            }
        });
        buttonPanel.add(deleteButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH); // Ajout du panneau de boutons en bas du panneau principal

        add(mainPanel); // Ajout du panneau principal à la fenêtre

        // Initialisation de la liste des films
        refreshFilmList(); // Chargement initial des films dans la liste
    }

    // Méthode pour rafraîchir la liste des films
    private void refreshFilmList() {
        filmListModel.clear(); // Effacement du modèle de liste avant chargement
        List<Film> films = filmController.getAllFilms(); // Obtention de tous les films via le contrôleur
        for (Film film : films) {
            filmListModel.addElement(film); // Ajout de chaque film au modèle de liste
        }
    }
}
