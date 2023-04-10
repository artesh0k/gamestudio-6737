package sk.tuke.gamestudio.service;

import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.RatingServiceJDBS;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RatingServiceTest {

    private final RatingService ratingService = new RatingServiceJDBS();

    @Test
    public void resetTest() {
        ratingService.reset();
        assertEquals(0, ratingService.getAverageRating("slitherlink"));
    }

    @Test
    public void setRatingTest() {
        ratingService.reset();
        var date = new Date();

        ratingService.setRating(new Rating("Jaro", "slitherlink",100, date));
        assertEquals(100, ratingService.getRating("Jaro", "slitherlink"));
        ratingService.reset();
    }
    @Test
    public void updateRatingTest() {
        ratingService.reset();
        var date = new Date();

        ratingService.setRating(new Rating("Jaro", "slitherlink",100, date));
        ratingService.setRating(new Rating("Jaro", "slitherlink",25, date));
        assertEquals(25, ratingService.getRating("Jaro", "slitherlink"));
        ratingService.reset();
    }

    @Test
    public void getAverageRating() {
        ratingService.reset();
        var date = new Date();

        ratingService.setRating(new Rating("Jaro", "slitherlink", 10, date));
        ratingService.setRating(new Rating("Katka", "slitherlink", 10, date));
        ratingService.setRating(new Rating("Zuzka", "tiles", 3, date));
        ratingService.setRating(new Rating("Jung-kook", "slitherlink", 40, date));
        assertEquals(20,ratingService.getAverageRating("slitherlink"));
        ratingService.reset();
    }

}

