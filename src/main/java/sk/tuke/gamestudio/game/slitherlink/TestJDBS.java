package sk.tuke.gamestudio.game.slitherlink;

import sk.tuke.gamestudio.game.slitherlink.entity.Score;
import sk.tuke.gamestudio.game.slitherlink.service.GameStudioException;
import sk.tuke.gamestudio.game.slitherlink.service.ScoreService;
import sk.tuke.gamestudio.game.slitherlink.service.ScoreServiceJDBS;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;


public class TestJDBS {
    public static void main(String[] args) throws Exception{
      ScoreService service = new ScoreServiceJDBS();
      service.reset();
      service.addScore(new Score("julia", "slitherlink", 200, new Date()));
      service.addScore(new Score("tanya", "slitherlink", 100, new Date()));
      service.addScore(new Score("sonya", "slitherlink", 130, new Date()));
      service.addScore(new Score("sveta", "slitherlink", 101, new Date()));
      System.out.println(service.getTopScores("slitherlink"));
    }
}
