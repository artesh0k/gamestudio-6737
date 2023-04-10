package sk.tuke.gamestudio.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sk.tuke.gamestudio.SpringClient;
import sk.tuke.gamestudio.entity.Score;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ScoreServiceJPATest {

    @Autowired
    private ScoreService scoreService;

    @Test
    public void resetTest() {
        scoreService.reset();
        assertEquals(0, scoreService.getTopScores("slitherlink").size());
    }

    @Test
    public void addScore() {
        scoreService.reset();
        var date = new Date();

        scoreService.addScore(new Score("Jaro", "slitherlink", 100, date));

        var scores = scoreService.getTopScores("slitherlink");
        assertEquals(1, scores.size());
        assertEquals("slitherlink", scores.get(0).getGame());
        assertEquals("Jaro", scores.get(0).getPlayer());
        assertEquals(100, scores.get(0).getPoints());
        assertEquals(date, scores.get(0).getPlayedOn());
        scoreService.reset();
    }

    @Test
    public void getTopScores() {
        scoreService.reset();
        var date = new Date();
        scoreService.addScore(new Score("Jaro", "slitherlink", 120, date));
        scoreService.addScore(new Score("Katka", "slitherlink", 150, date));
        scoreService.addScore(new Score("Zuzka", "tiles", 180, date));
        scoreService.addScore(new Score("Jaro", "slitherlink", 100, date));

        var scores = scoreService.getTopScores("slitherlink");

        assertEquals(3, scores.size());

        assertEquals("slitherlink", scores.get(0).getGame());
        assertEquals("Katka", scores.get(0).getPlayer());
        assertEquals(150, scores.get(0).getPoints());
        assertEquals(date, scores.get(0).getPlayedOn());

        assertEquals("slitherlink", scores.get(1).getGame());
        assertEquals("Jaro", scores.get(1).getPlayer());
        assertEquals(120, scores.get(1).getPoints());
        assertEquals(date, scores.get(1).getPlayedOn());

        assertEquals("slitherlink", scores.get(2).getGame());
        assertEquals("Jaro", scores.get(2).getPlayer());
        assertEquals(100, scores.get(2).getPoints());
        assertEquals(date, scores.get(2).getPlayedOn());
        scoreService.reset();
    }

}
