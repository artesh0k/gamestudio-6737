package sk.tuke.gamestudio.game.slitherlink.service;

import sk.tuke.gamestudio.game.slitherlink.entity.Comment;
import sk.tuke.gamestudio.game.slitherlink.entity.Rating;

import java.util.List;

public interface RatingService {

    void addVote(Rating rating);
    List<Rating> getVotes(String game);
    double getRating(String game);
    void reset();

}
