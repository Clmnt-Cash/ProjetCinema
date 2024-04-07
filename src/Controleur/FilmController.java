package Controleur;

import Modele.Film;
import java.util.List;
import java.util.ArrayList;

public class FilmController {
    // Méthodes pour interagir avec la base de données et manipuler les objets Film

    // Simulation d'une base de données de films
    private List<Film> films = new ArrayList<>();

    public List<Film> getAllFilms() {
        // Logique pour récupérer tous les films depuis la base de données
        // Retourne une liste d'objets Film
        return films;
    }

    public void addFilm(Film film) {
        // Logique pour ajouter un film à la base de données
        films.add(film);
    }

    public void updateFilm(Film updatedFilm) {
        // Logique pour mettre à jour les détails d'un film dans la base de données
        for (Film film : films) {
            if (film.getId() == updatedFilm.getId()) {
                film.setTitre(updatedFilm.getTitre());
                film.setRealisateur(updatedFilm.getRealisateur());
                film.setSynopsis(updatedFilm.getSynopsis());
                film.setCheminImage(updatedFilm.getCheminImage());
                break;
            }
        }
    }

    public void deleteFilm(int filmId) {
        // Logique pour supprimer un film de la base de données
        films.removeIf(film -> film.getId() == filmId);
    }
}

