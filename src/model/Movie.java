package model;

import java.util.Date;

public class Movie {
    private int movieID;
    private String title;
    private String duration;
    private String rating;
    private String director;
    private String actors;
    private String genre;
    private String story;
    private Date releaseDate;
    private int score;

    public Movie() {}

    public Movie(String title, String duration, String rating, String director, String actors, String genre, String story, Date releaseDate, int score) {
        this.title = title;
        this.duration = duration;
        this.rating = rating;
        this.director = director;
        this.actors = actors;
        this.genre = genre;
        this.story = story;
        this.releaseDate = releaseDate;
        this.score = score;
    }
    
    public Movie(int movieID, String title, String duration, String rating, String director, String actors, String genre, String story, Date releaseDate, int score) {
        this.movieID = movieID;
        this.title = title;
        this.duration = duration;
        this.rating = rating;
        this.director = director;
        this.actors = actors;
        this.genre = genre;
        this.story = story;
        this.releaseDate = releaseDate;
        this.score = score;
    }

    public int getMovieID() {
        return movieID;
    }

    public void setMovieID(int movieID) {
        this.movieID = movieID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}