package sk.tuke.gamestudio.game.slitherlink.entity;

import java.util.Date;

public class Comment {
    private String player;

    private String game;

    private String content;

    private Date commentedAt;

    public Comment(String player, String game, String comment, Date commentedAt) {
        this.player = player;
        this.game = game;
        this.content = comment;
        this.commentedAt = commentedAt;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getComment() {
        return content;
    }

    public void setComment(String comment) {
        this.content = comment;
    }

    public Date getCommentedAt() {
        return commentedAt;
    }

    public void setCommentedAt(Date commentedAt) {
        this.commentedAt = commentedAt;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "player='" + player + '\'' +
                ", game='" + game + '\'' +
                ", comment='" + content + '\'' +
                ", commentedAt=" + commentedAt +
                '}';
    }
}
