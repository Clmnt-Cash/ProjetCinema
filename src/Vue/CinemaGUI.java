package Vue;

import Controleur.FilmController;
import Modele.Film;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class CinemaGUI extends JFrame{

    //attributs
    private FilmController filmController;
    private JList<Film> filmList;
    private DefaultListModel<Film> filmListModel;

    public CinemaGUI() {
        setTitle("Gestion des Films");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        filmController = new FilmController();

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Affichage de la liste des films
        filmListModel = new DefaultListModel<>();
        filmList = new JList<>(filmListModel);
        JScrollPane scrollPane = new JScrollPane(filmList);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Boutons pour ajouter, mettre à jour et supprimer des films
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Ajouter");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ajouter un nouveau film
                Film newFilm = new Film(0, "Nouveau Film", "Réalisateur", "Synopsis", "CheminImage");
                filmController.addFilm(newFilm);
                refreshFilmList();
            }
        });
        buttonPanel.add(addButton);

        JButton updateButton = new JButton("Mettre à Jour");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mettre à jour le film sélectionné
                Film selectedFilm = filmList.getSelectedValue();
                if (selectedFilm != null) {
                    selectedFilm.setTitre("Titre mis à jour");
                    selectedFilm.setRealisateur("Réalisateur mis à jour");
                    selectedFilm.setSynopsis("Synopsis mis à jour");
                    selectedFilm.setCheminImage("CheminImage mis à jour");
                    filmController.updateFilm(selectedFilm);
                    refreshFilmList();
                }
            }
        });
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("Supprimer");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Supprimer le film sélectionné
                Film selectedFilm = filmList.getSelectedValue();
                if (selectedFilm != null) {
                    filmController.deleteFilm(selectedFilm.getId());
                    refreshFilmList();
                }
            }
        });
        buttonPanel.add(deleteButton);

        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Initialiser la liste des films
        refreshFilmList();
    }

    private void refreshFilmList() {
        // Récupérer tous les films et mettre à jour la liste
        filmListModel.clear();
        List<Film> films = filmController.getAllFilms();
        for (Film film : films) {
            filmListModel.addElement(film);
        }
    }
}
