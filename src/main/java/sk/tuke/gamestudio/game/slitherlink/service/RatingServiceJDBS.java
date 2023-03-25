package sk.tuke.gamestudio.game.slitherlink.service;

import sk.tuke.gamestudio.game.slitherlink.entity.Rating;

import java.sql.*;

public class RatingServiceJDBS implements RatingService{
    public static final String JDBC_URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String JDBS_USER = "postgres";
    public static final String JDBS_PASSWORD = "pstgrs1232023";
    public static final String DELETE_STATEMENT = "DELETE FROM rating";
    public static final String INSERT_STATEMENT = "INSERT INTO rating (game, player, rating, ratedOn) VALUES (?, ?, ?, ?)";
    public static final String SELECT_STATEMENT = "select rating from rating where game = ? and player = ?";
    private static final String SELECT_STATEMENT_AVG = "SELECT AVG(rating) FROM rating WHERE game = ?";


    @Override
    public void setRating(Rating rating) {
        try(
                var connection = DriverManager.getConnection(JDBC_URL, JDBS_USER, JDBS_PASSWORD);
                var statement = connection.prepareStatement(INSERT_STATEMENT)
        ){
            statement.setString(1, rating.getGame());
            statement.setString(2, rating.getPlayer());
            statement.setInt(3, rating.getRating());
            statement.setTimestamp(4, new Timestamp(rating.getRatedOn().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GameStudioException("Problem adding rating", e);
        }
    }

    @Override
    public int getRating(String player,String game) {
        try(
                var connection = DriverManager.getConnection(JDBC_URL, JDBS_USER, JDBS_PASSWORD);
                var statement = connection.prepareStatement(SELECT_STATEMENT)
        ){
            statement.setString(1, game);
            statement.setString(2, player);

            var res = statement.executeQuery();
            int rating = 0;
            if (res.next()) {
                rating = res.getInt(1);
            }
            return rating;

        } catch (SQLException e) {
            throw new GameStudioException("Problem getting rating", e);
        }
    }

    @Override
    public int getAvarageRating(String game) {
        try(
                var connection = DriverManager.getConnection(JDBC_URL, JDBS_USER, JDBS_PASSWORD);
                var statement = connection.prepareStatement(SELECT_STATEMENT_AVG)
        ){
            statement.setString(1, game);
            var res = statement.executeQuery();
            int rating = 0;
            if (res.next()) {
                rating = res.getInt(1);
            }
            return rating;
        } catch (SQLException e) {
            throw new GameStudioException("Problem getting average rating", e);
        }

    }

    @Override
    public void reset() {
        try (
                Connection connection = DriverManager.getConnection(JDBC_URL,JDBS_USER,JDBS_PASSWORD);
                Statement statement = connection.createStatement()
        ) {
            statement.executeUpdate(DELETE_STATEMENT);
        } catch (SQLException e) {
            throw new GameStudioException("Problem deleting rating", e);
        }

    }
}
