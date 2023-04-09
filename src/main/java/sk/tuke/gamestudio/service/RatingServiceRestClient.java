package sk.tuke.gamestudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;

import java.util.Arrays;
import java.util.List;

public class RatingServiceRestClient implements RatingService{

    private String url = "http://localhost:8080/api/rating";

    @Autowired
    private RestTemplate restTemplate;


    @Override
    public void setRating(Rating rating) {
        restTemplate.postForEntity(url, rating, Rating.class);
    }

    @Override
    public int getRating(String player, String game) {
        return restTemplate.getForEntity(url + "/" + game + "/" + player, Rating.class).getBody().getRating();
    }

    @Override
    public int getAverageRating(String game) {
        return restTemplate.getForEntity(url + "/" + game, int.class).getBody();
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported on web service");
    }
}
