import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.game.slitherlink.entity.Comment;
import sk.tuke.gamestudio.game.slitherlink.entity.Rating;
import sk.tuke.gamestudio.game.slitherlink.entity.Score;
import sk.tuke.gamestudio.game.slitherlink.service.*;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RatingServiceTest {

    private final RatingService ratingService = new RatingServiceJDBS();

    @Test
    public void resetTest() {
        ratingService.reset();
        assertEquals(0, ratingService.getVotes("slitherlink").size());
    }

   @Test
    public void addVote() {
        ratingService.reset();
        var date = new Date();

        ratingService.addVote(new Rating("Jaro", "slitherlink", 100, date));

        var votes = ratingService.getVotes("slitherlink");

        assertEquals(1, votes.size());
        assertEquals("slitherlink", votes.get(0).getGame());
        assertEquals("Jaro", votes.get(0).getPlayer());
        assertEquals(100, votes.get(0).getVote());
        assertEquals(date, votes.get(0).getVotedAt());
    }

    @Test
    public void getLastVotes() {
        ratingService.reset();
        var date1 = new Date();
        ratingService.addVote(new Rating("Jaro", "slitherlink", 1, date1));
        var date2 = new Date();
        ratingService.addVote(new Rating("Katka", "slitherlink", 2, date2));
        var date3 = new Date();
        ratingService.addVote(new Rating("Zuzka", "tiles", 3, date3));
        var date4 = new Date();
        ratingService.addVote(new Rating("Jung-kook", "slitherlink", 4, date4));

        var votes = ratingService.getVotes("slitherlink");

        assertEquals(3, votes.size());
        System.out.println(votes.toString());
        assertEquals("slitherlink", votes.get(0).getGame());
        assertEquals("Jung-kook", votes.get(0).getPlayer());
        assertEquals(4, votes.get(0).getVote());
        assertEquals(date4, votes.get(0).getVotedAt());

        assertEquals("slitherlink", votes.get(1).getGame());
        assertEquals("Katka", votes.get(1).getPlayer());
        assertEquals(2, votes.get(1).getVote());
        assertEquals(date2, votes.get(1).getVotedAt());

        assertEquals("slitherlink", votes.get(2).getGame());
        assertEquals("Jaro", votes.get(2).getPlayer());
        assertEquals(1, votes.get(2).getVote());
        assertEquals(date1, votes.get(2).getVotedAt());
    }

}
