package com.nhaccuatui.musicapplication.ADTO;

public class SongArtist {
    private int songArtistId;
    private int songId;
    private int artistId;

    // Constructor
    public SongArtist(int songArtistId, int songId, int artistId) {
        this.songArtistId = songArtistId;
        this.songId = songId;
        this.artistId = artistId;
    }

    // Default Constructor
    public SongArtist() {}

    // Getters and Setters
    public int getSongArtistId() { return songArtistId; }
    public void setSongArtistId(int songArtistId) { this.songArtistId = songArtistId; }

    public int getSongId() { return songId; }
    public void setSongId(int songId) { this.songId = songId; }

    public int getArtistId() { return artistId; }
    public void setArtistId(int artistId) { this.artistId = artistId; }

    @Override
    public String toString() {
        return "SongArtist{" +
                "songArtistId=" + songArtistId +
                ", songId=" + songId +
                ", artistId=" + artistId +
                '}';
    }
}

