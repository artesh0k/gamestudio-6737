package sk.tuke.gamestudio.game.slitherlink.entity;

import java.util.Date;

public class Rating {

    private String player;

    private String game;

    private int vote;

    private Date votedAt;

    public Rating(String player, String game, int vote, Date votedAt) {
        this.player = player;
        this.game = game;
        this.vote = vote;
        this.votedAt = votedAt;
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

    public int getVote() {
        return vote;
    }

    public void setVote(int vote) {
        this.vote = vote;
    }

    public Date getVotedAt() {
        return votedAt;
    }

    public void setVotedAt(Date votedAt) {
        this.votedAt = votedAt;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "player='" + player + '\'' +
                ", game='" + game + '\'' +
                ", vote=" + vote +
                ", votedAt=" + votedAt +
                '}';
    }
}
