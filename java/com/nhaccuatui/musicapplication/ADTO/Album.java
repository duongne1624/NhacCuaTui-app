package com.nhaccuatui.musicapplication.ADTO;

public class Album {
    private int albumId;
    private String albumName;
    private String artistName;
    private String albumCover;
    private String releaseDate;
    private String createdAt;
    private String updatedAt;

    // Constructor
    public Album(int albumId, String albumName, String artistName, String albumCover, String releaseDate, String createdAt, String updatedAt) {
        this.albumId = albumId;
        this.albumName = albumName;
        this.artistName = artistName;
        this.albumCover = albumCover;
        this.releaseDate = releaseDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Default Constructor
    public Album() {}

    // Getters and Setters
    public int getAlbumId() { return albumId; }
    public void setAlbumId(int albumId) { this.albumId = albumId; }

    public String getAlbumName() { return albumName; }
    public void setAlbumName(String albumName) { this.albumName = albumName; }

    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) { this.artistName = artistName; }

    public String getAlbumCover() { return albumCover; }
    public void setAlbumCover(String albumCover) { this.albumCover = albumCover; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Album{" +
                "albumId=" + albumId +
                ", albumName='" + albumName + '\'' +
                ", artistName='" + artistName + '\'' +
                '}';
    }
}
