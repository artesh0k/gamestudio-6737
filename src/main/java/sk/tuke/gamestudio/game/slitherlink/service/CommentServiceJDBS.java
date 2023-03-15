package sk.tuke.gamestudio.game.slitherlink.service;

import sk.tuke.gamestudio.game.slitherlink.entity.Comment;
import sk.tuke.gamestudio.game.slitherlink.entity.Score;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CommentServiceJDBS implements CommentService{
    public static final String JDBC_URL = "jdbc:postgresql://localhost/gamestudio";
    public static final String JDBS_USER = "postgres";
    public static final String JDBS_PASSWORD = "pstgrs1232023";
    public static final String DELETE_STATEMENT = "DELETE FROM comment";
    public static final String INSERT_STATEMENT = "INSERT INTO comment (game, player, content, commentedAt) VALUES (?, ?, ?, ?)";
    public static final String SELECT_STATEMENT = "select * from comment where game = ? order by commentedAt desc limit 10";

    @Override
    public void addComment(Comment comment) {
        try(
                var connection = DriverManager.getConnection(JDBC_URL, JDBS_USER, JDBS_PASSWORD);
                var statement = connection.prepareStatement(INSERT_STATEMENT);
        ){
            statement.setString(1, comment.getGame());
            statement.setString(2, comment.getPlayer());
            statement.setString(3, comment.getComment());
            statement.setTimestamp(4, new Timestamp(comment.getCommentedAt().getTime()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new GameStudioException("Problem adding comment", e);
        }
    }

    @Override
    public List<Comment> getComments(String game) {
        try(
                var connection = DriverManager.getConnection(JDBC_URL, JDBS_USER, JDBS_PASSWORD);
                var statement = connection.prepareStatement(SELECT_STATEMENT);
        ){
            statement.setString(1, game);
            try (var rs = statement.executeQuery();){
                List<Comment> comments = new ArrayList<>();
                while (rs.next()){
                    comments.add(new Comment(rs.getString(1), rs.getString(2), rs.getString(3), rs.getTimestamp(4)));
                }
                return comments;
            }
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
            throw new GameStudioException("Problem deleting comment", e);
        }
    }
}
