package com.nhaccuatui.musicapplication.DTO;

public class Song {
    private String id;
    private String name;
    private String releaseDate;
    private String audioFile; // Path to the audio file
    private String imageFile;
    private long views;
    private int likes;
    private String album;
    private String genre;
    private String artist;
    private long duration; // Duration in milliseconds

    // Constructor
    public Song(String id, String name, String releaseDate, String audioFile, String imageFile,
                long views, int likes, String album, String genre, String artist) {
        this.id = id;
        this.name = name;
        this.releaseDate = releaseDate;
        this.audioFile = audioFile;
        this.imageFile = imageFile;
        this.views = views;
        this.likes = likes;
        this.album = album;
        this.genre = genre;
        this.artist = artist;
    }

    public Song(String id, String name, String releaseDate, String audioFile, String imageFile, long views, int likes, String album, String genre, String artist, long duration) {
        this.id = id;
        this.name = name;
        this.releaseDate = releaseDate;
        this.audioFile = audioFile;
        this.imageFile = imageFile;
        this.views = views;
        this.likes = likes;
        this.album = album;
        this.genre = genre;
        this.artist = artist;
        this.duration = duration;
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getReleaseDate() { return releaseDate; }
    public String getAudioFile() { return audioFile; }
    public String getImageFile() { return imageFile; }
    public long getViews() { return views; }
    public int getLikes() { return likes; }
    public String getAlbum() { return album; }
    public String getGenre() { return genre; }
    public String getArtist() { return artist; }
    public long getDuration() { return duration; }
}



