package sk.tuke.gamestudio.game.slitherlink.service;

import sk.tuke.gamestudio.game.slitherlink.entity.Rating;

public interface RatingService {
    void setRating(Rating rating);
    int getRating(String player, String game);
    int getAvarageRating(String game);
    void reset();

}
