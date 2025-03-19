package com.nhaccuatui.musicapplication.ADTO;

public class Playlist {
    private int playlistId;
    private int userId;
    private String playlistName;
    private String createdAt;
    private String updatedAt;

    // Constructor
    public Playlist(int playlistId, int userId, String playlistName, String createdAt, String updatedAt) {
        this.playlistId = playlistId;
        this.userId = userId;
        this.playlistName = playlistName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Default Constructor
    public Playlist() {}

    // Getters and Setters
    public int getPlaylistId() { return playlistId; }
    public void setPlaylistId(int playlistId) { this.playlistId = playlistId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getPlaylistName() { return playlistName; }
    public void setPlaylistName(String playlistName) { this.playlistName = playlistName; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "Playlist{" +
                "playlistId=" + playlistId +
                ", userId=" + userId +
                ", playlistName='" + playlistName + '\'' +
                '}';
    }
}

