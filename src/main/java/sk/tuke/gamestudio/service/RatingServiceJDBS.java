package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

import java.sql.*;

public class RatingServiceJDBS implements RatingService{
    public static final String JDBC_URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String JDBS_USER = "postgres";
    public static final String JDBS_PASSWORD = "pstgrs1232023";
    public static final String DELETE_STATEMENT = "DELETE FROM rating";
    public static final String INSERT_STATEMENT = "INSERT INTO rating (game, player, rating, ratedOn) VALUES (?, ?, ?, ?)";
    public static final String SELECT_STATEMENT = "SELECT rating FROM rating WHERE game = ? AND player = ?";
    private static final String SELECT_STATEMENT_AVG = "SELECT AVG(rating) FROM rating WHERE game = ?";
    private static final String CHECK_STATEMENT = "SELECT COUNT(rating) FROM rating WHERE player = ?";
    public static final String UPDATE_STATEMENT = "UPDATE rating SET rating = ? WHERE player = ?";

    @Override
    public void setRating(Rating rating) {
        try(
                var connection = DriverManager.getConnection(JDBC_URL, JDBS_USER, JDBS_PASSWORD);
                var statement = connection.prepareStatement(CHECK_STATEMENT)
        ){
            statement.setString(1, rating.getPlayer());
            var res = statement.executeQuery();
            if(res.next()){
                if(res.getInt(1) == 0){
                    var statement1 = connection.prepareStatement(INSERT_STATEMENT);
                    statement1.setString(1, rating.getGame());
                    statement1.setString(2, rating.getPlayer());
                    statement1.setInt(3, rating.getRating());
                    statement1.setTimestamp(4, new Timestamp(rating.getRatedOn().getTime()));
                    statement1.executeUpdate();
                } else {
                    var statement1 = connection.prepareStatement(UPDATE_STATEMENT);
                    statement1.setInt(1, rating.getRating());
                    statement1.setString(2, rating.getPlayer());
                    statement1.execute();
                }
            }

        } catch (SQLException e) {
            throw new GameStudioException("Problem setting rating", e);
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
    public int getAverageRating(String game) {
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
