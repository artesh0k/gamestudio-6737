package sk.tuke.gamestudio.game.slitherlink.service;

import sk.tuke.gamestudio.game.slitherlink.entity.Comment;
import sk.tuke.gamestudio.game.slitherlink.entity.Rating;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RatingServiceJDBS implements RatingService{
    public static final String JDBC_URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String JDBS_USER = "postgres";
    public static final String JDBS_PASSWORD = "pstgrs1232023";
    public static final String DELETE_STATEMENT = "DELETE FROM rating";
    public static final String INSERT_STATEMENT = "INSERT INTO rating (game, player, vote, votedAt) VALUES (?, ?, ?, ?)";
    public static final String SELECT_STATEMENT = "select * from rating where game = ? order by votedAt desc limit 10";
    private static final String SELECT_STATEMENT_AVG = "SELECT AVG(vote) FROM rating WHERE game = ?";

    @Override
    public void addVote(Rating rating) {
        try(
                var connection = DriverManager.getConnection(JDBC_URL, JDBS_USER, JDBS_PASSWORD);
                var statement = connection.prepareStatement(INSERT_STATEMENT);
        ){
            statement.setString(1, rating.getGame());
            statement.setString(2, rating.getPlayer());
            statement.setInt(3, rating.getVote());
            statement.setTimestamp(4, new Timestamp(rating.getVotedAt().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GameStudioException("Problem adding vote", e);
        }
    }

    @Override
    public List<Rating> getVotes(String game) {
        try(
                var connection = DriverManager.getConnection(JDBC_URL, JDBS_USER, JDBS_PASSWORD);
                var statement = connection.prepareStatement(SELECT_STATEMENT);
        ){
            statement.setString(1, game);
            try (var rs = statement.executeQuery();){
                List<Rating> ratings = new ArrayList<>();
                while (rs.next()){
                    ratings.add(new Rating(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getTimestamp(4)));
                }
                return ratings;
            }
        } catch (SQLException e) {
            throw new GameStudioException("Problem getting comments", e);
        }
    }

    @Override
    public double getRating(String game) {
        try(
                var connection = DriverManager.getConnection(JDBC_URL, JDBS_USER, JDBS_PASSWORD);
                var statement = connection.prepareStatement(SELECT_STATEMENT_AVG);
        ){
            statement.setString(1, game);
            var res = statement.executeQuery();
            double rating = 0;
            if (res.next()) {
                rating = res.getDouble(1);
            }
            return rating;
        } catch (SQLException e) {
            throw new GameStudioException("Problem getting comments", e);
        }
    }

    @Override
    public void reset() {
        try (
                Connection connection = DriverManager.getConnection(JDBC_URL,JDBS_USER,JDBS_PASSWORD);
                Statement statement = connection.createStatement();
        ) {
            statement.executeUpdate(DELETE_STATEMENT);
        } catch (SQLException e) {
            throw new GameStudioException("Problem deleting rating", e);
        }
    }
}
