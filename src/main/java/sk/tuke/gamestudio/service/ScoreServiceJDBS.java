package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Score;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ScoreServiceJDBS implements ScoreService{

    public static final String JDBC_URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String JDBS_USER = "postgres";
    public static final String JDBS_PASSWORD = "pstgrs1232023";
    public static final String DELETE_STATEMENT = "DELETE FROM score";
    public static final String INSERT_STATEMENT = "INSERT INTO score (game, player, points, playedOn) VALUES (?, ?, ?, ?)";
    public static final String SELECT_STATEMENT = "select * from score where game = ? order by points desc limit 10";

    @Override
    public void addScore(Score score) {
        try(
                var connection = DriverManager.getConnection(JDBC_URL, JDBS_USER, JDBS_PASSWORD);
                var statement = connection.prepareStatement(INSERT_STATEMENT)
        ){
            statement.setString(1, score.getGame());
            statement.setString(2, score.getPlayer());
            statement.setInt(3, score.getPoints());
            statement.setTimestamp(4, new Timestamp(score.getPlayedOn().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GameStudioException("Problem adding score", e);
        }
    }

    @Override
    public List<Score> getTopScores(String game) {
        try(
                var connection = DriverManager.getConnection(JDBC_URL, JDBS_USER, JDBS_PASSWORD);
                var statement = connection.prepareStatement(SELECT_STATEMENT)
        ){
            statement.setString(1, game);
            try (var rs = statement.executeQuery()){
                List<Score> scores = new ArrayList<>();
                while (rs.next()){
                    scores.add(new Score(rs.getString(1), rs.getString(2), rs.getInt(3), rs.getTimestamp(4)));
                }
                return scores;
            }

        } catch (SQLException e) {
        throw new GameStudioException("Problem getting scores", e);
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
            throw new GameStudioException("Problem deleting score", e);
        }
    }
}
