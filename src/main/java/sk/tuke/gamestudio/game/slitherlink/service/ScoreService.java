package sk.tuke.gamestudio.game.slitherlink.service;

import sk.tuke.gamestudio.game.slitherlink.entity.Score;

import java.util.List;

public interface ScoreService {

    void addScore(Score score);

    List<Score> getTopScores(String game);

    void reset();

}
