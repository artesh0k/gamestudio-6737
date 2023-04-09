package sk.tuke.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.service.RatingService;

@RestController
@RequestMapping("/api/rating")
public class RatingServiceRest {

    @Autowired
    private RatingService ratingService;

    //POST -> http://localhost:8080/api/rating
    @PostMapping
    public void setRating(Rating rating){
        ratingService.setRating(rating);
    }

    //GET -> http://localhost:8080/api/rating/player/slitherlink
    @GetMapping("/{game}/{player}")
    public int getRating(String player, String game){
        return ratingService.getRating(player, game);
    }

    //GET -> http://localhost:8080/api/rating/slitherlink
    @GetMapping("/{game}")
    public int getAverageRating(String game){
        return ratingService.getAverageRating(game);
    }
}
