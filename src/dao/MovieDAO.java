package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Movie;
import util.DBConnection;

public class MovieDAO {

    public List<Movie> getAllMovies() throws SQLException {
        List<Movie> movies = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        String query = "SELECT * FROM movies";
        PreparedStatement stmt = conn.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            Movie movie = new Movie();
            movie.setMovieID(rs.getInt("MovieID"));
            movie.setTitle(rs.getString("Title"));
            movie.setDuration(rs.getString("Duration"));
            movie.setRating(rs.getString("Rating"));
            movie.setDirector(rs.getString("Director"));
            movie.setActors(rs.getString("Actors"));
            movie.setGenre(rs.getString("Genre"));
            movie.setStory(rs.getString("Story"));
            movie.setReleaseDate(rs.getDate("ReleaseDate"));
            movie.setScore(rs.getInt("Score"));
            movies.add(movie);
        }

        return movies;
    }

    public Movie getMovieByID(int movieID) throws SQLException {
        Movie movie = null;
        Connection conn = DBConnection.getConnection();
        String query = "SELECT * FROM movies WHERE MovieID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, movieID);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            movie = new Movie();
            movie.setMovieID(rs.getInt("MovieID"));
            movie.setTitle(rs.getString("Title"));
            movie.setDuration(rs.getString("Duration"));
            movie.setRating(rs.getString("Rating"));
            movie.setDirector(rs.getString("Director"));
            movie.setActors(rs.getString("Actors"));
            movie.setGenre(rs.getString("Genre"));
            movie.setStory(rs.getString("Story"));
            movie.setReleaseDate(rs.getDate("ReleaseDate"));
            movie.setScore(rs.getInt("Score"));
        }

        return movie;
    }

    public void addMovie(Movie movie) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String query = "INSERT INTO movies (MovieID, Title, Duration, Rating, Director, Actors, Genre, Story, ReleaseDate, Score) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, movie.getMovieID());
        stmt.setString(2, movie.getTitle());
        stmt.setString(3, movie.getDuration());
        stmt.setString(4, movie.getRating());
        stmt.setString(5, movie.getDirector());
        stmt.setString(6, movie.getActors());
        stmt.setString(7, movie.getGenre());
        stmt.setString(8, movie.getStory());
        stmt.setDate(9, new java.sql.Date(movie.getReleaseDate().getTime()));
        stmt.setInt(10, movie.getScore());
        stmt.executeUpdate();
    }

    public void updateMovie(Movie movie) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String query = "UPDATE movies SET Title = ?, Duration = ?, Rating = ?, Director = ?, Actors = ?, Genre = ?, Story = ?, ReleaseDate = ?, Score = ? WHERE MovieID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, movie.getTitle());
        stmt.setString(2, movie.getDuration());
        stmt.setString(3, movie.getRating());
        stmt.setString(4, movie.getDirector());
        stmt.setString(5, movie.getActors());
        stmt.setString(6, movie.getGenre());
        stmt.setString(7, movie.getStory());
        stmt.setDate(8, new java.sql.Date(movie.getReleaseDate().getTime()));
        stmt.setInt(9, movie.getScore());
        stmt.setInt(10, movie.getMovieID());
        stmt.executeUpdate();
    }

    public void deleteMovie(int movieID) throws SQLException {
        Connection conn = DBConnection.getConnection();
        String query = "DELETE FROM movies WHERE MovieID = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, movieID);
        stmt.executeUpdate();
    }
    
    public Movie getMovieByTitle(String title) throws SQLException {
        Movie movie = null;
        Connection conn = DBConnection.getConnection();
        String query = "SELECT * FROM movies WHERE Title = ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, title);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            movie = new Movie();
            movie.setMovieID(rs.getInt("MovieID"));
            movie.setTitle(rs.getString("Title"));
            movie.setDuration(rs.getString("Duration"));
            movie.setRating(rs.getString("Rating"));
            movie.setDirector(rs.getString("Director"));
            movie.setActors(rs.getString("Actors"));
            movie.setGenre(rs.getString("Genre"));
            movie.setStory(rs.getString("Story"));
            movie.setReleaseDate(rs.getDate("ReleaseDate"));
            movie.setScore(rs.getInt("Score"));
        }

        return movie;
    }
}