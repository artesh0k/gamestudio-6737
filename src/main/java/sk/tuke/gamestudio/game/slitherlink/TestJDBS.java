package sk.tuke.gamestudio.game.slitherlink;

import sk.tuke.gamestudio.game.slitherlink.entity.Comment;
import sk.tuke.gamestudio.game.slitherlink.entity.Rating;
import sk.tuke.gamestudio.game.slitherlink.entity.Score;
import sk.tuke.gamestudio.game.slitherlink.service.*;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;


public class TestJDBS {
    public static void main(String[] args) throws Exception{


      /*ScoreService service = new ScoreServiceJDBS();
      service.reset();
      service.addScore(new Score("julia", "slitherlink", 200, new Date()));
      service.addScore(new Score("tanya", "slitherlink", 100, new Date()));
      service.addScore(new Score("sonya", "slitherlink", 130, new Date()));
      service.addScore(new Score("sveta", "slitherlink", 101, new Date()));
      System.out.println(service.getTopScores("slitherlink"));*/
        /*CommentService commentService = new CommentServiceJDBS();
        commentService.reset();
        commentService.addComment(new Comment("valerii", "slitherlink", "hello??", new Date()));
        System.out.println(commentService.getComments("slitherlink"));*/
        RatingService ratingService = new RatingServiceJDBS();
        ratingService.reset();
        ratingService.addVote(new Rating("valerii","slitherlink", 10, new Date()));
        ratingService.addVote(new Rating("jan","slitherlink", 5, new Date()));
        ratingService.addVote(new Rating("stefan","slitherlink", 8, new Date()));
        ratingService.addVote(new Rating("blanka","slitherlink", 1, new Date()));
        ratingService.addVote(new Rating("juraj","slitherlink", 200, new Date()));
        System.out.println(ratingService.getVotes("slitherlink"));
        System.out.println(ratingService.getRating("slitherlink"));
    }
}
