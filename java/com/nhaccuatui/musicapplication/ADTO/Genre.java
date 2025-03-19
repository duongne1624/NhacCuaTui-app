package com.nhaccuatui.musicapplication.ADTO;

public class Genre {
    private int genreId;
    private String genreName;
    private String region;

    // Constructor
    public Genre(int genreId, String genreName, String region) {
        this.genreId = genreId;
        this.genreName = genreName;
        this.region = region;
    }

    // Default Constructor
    public Genre() {}

    // Getters and Setters
    public int getGenreId() { return genreId; }
    public void setGenreId(int genreId) { this.genreId = genreId; }

    public String getGenreName() { return genreName; }
    public void setGenreName(String genreName) { this.genreName = genreName; }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    @Override
    public String toString() {
        return "Genre{" +
                "genreId=" + genreId +
                ", genreName='" + genreName + '\'' +
                ", region='" + region + '\'' +
                '}';
    }
}
