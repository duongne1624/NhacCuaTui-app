package com.nhaccuatui.musicapplication.ADTO;

public class PlaylistSong {
    private int playlistSongId;
    private int playlistId;
    private int songId;
    private String addedAt;

    // Constructor
    public PlaylistSong(int playlistSongId, int playlistId, int songId, String addedAt) {
        this.playlistSongId = playlistSongId;
        this.playlistId = playlistId;
        this.songId = songId;
        this.addedAt = addedAt;
    }

    // Default Constructor
    public PlaylistSong() {}

    // Getters and Setters
    public int getPlaylistSongId() { return playlistSongId; }
    public void setPlaylistSongId(int playlistSongId) { this.playlistSongId = playlistSongId; }

    public int getPlaylistId() { return playlistId; }
    public void setPlaylistId(int playlistId) { this.playlistId = playlistId; }

    public int getSongId() { return songId; }
    public void setSongId(int songId) { this.songId = songId; }

    public String getAddedAt() { return addedAt; }
    public void setAddedAt(String addedAt) { this.addedAt = addedAt; }

    @Override
    public String toString() {
        return "PlaylistSong{" +
                "playlistSongId=" + playlistSongId +
                ", playlistId=" + playlistId +
                ", songId=" + songId +
                '}';
    }
}

