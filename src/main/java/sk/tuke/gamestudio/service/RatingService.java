package sk.tuke.gamestudio.service;

import sk.tuke.gamestudio.entity.Rating;

public interface RatingService {
    void setRating(Rating rating);
    int getRating(String player, String game);
    int getAverageRating(String game);
    void reset();

}
