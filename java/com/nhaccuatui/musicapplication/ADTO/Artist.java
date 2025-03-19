package com.nhaccuatui.musicapplication.ADTO;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Artist {
    private int artistId;
    private String artistName;
    private String bio;
    private String avatarImage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor with parameters
    public Artist(int artistId, String artistName, String bio, String avatarImage, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.artistId = artistId;
        this.artistName = artistName;
        this.bio = bio;
        this.avatarImage = avatarImage;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Default Constructor
    public Artist() {}

    // Getters and Setters
    public int getArtistId() {
        return artistId;
    }

    public void setArtistId(int artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatarImage() {
        return avatarImage;
    }

    public void setAvatarImage(String avatarImage) {
        this.avatarImage = avatarImage;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Format LocalDateTime to String for display
    public String getCreatedAtFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return createdAt.format(formatter);
    }

    public String getUpdatedAtFormatted() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return updatedAt.format(formatter);
    }

    @Override
    public String toString() {
        return "Artist{" +
                "artistId=" + artistId +
                ", artistName='" + artistName + '\'' +
                ", bio='" + bio + '\'' +
                ", avatarImage='" + avatarImage + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
