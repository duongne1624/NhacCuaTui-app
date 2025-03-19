package com.nhaccuatui.musicapplication.ADTO;

public class Comment {
    private int commentId;
    private int userId;
    private int songId;
    private String commentText;
    private String commentedAt;

    // Constructor
    public Comment(int commentId, int userId, int songId, String commentText, String commentedAt) {
        this.commentId = commentId;
        this.userId = userId;
        this.songId = songId;
        this.commentText = commentText;
        this.commentedAt = commentedAt;
    }

    // Default Constructor
    public Comment() {}

    // Getters and Setters
    public int getCommentId() { return commentId; }
    public void setCommentId(int commentId) { this.commentId = commentId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getSongId() { return songId; }
    public void setSongId(int songId) { this.songId = songId; }

    public String getCommentText() { return commentText; }
    public void setCommentText(String commentText) { this.commentText = commentText; }

    public String getCommentedAt() { return commentedAt; }
    public void setCommentedAt(String commentedAt) { this.commentedAt = commentedAt; }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId=" + commentId +
                ", userId=" + userId +
                ", songId=" + songId +
                ", commentText='" + commentText + '\'' +
                '}';
    }
}

