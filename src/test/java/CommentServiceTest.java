import org.junit.jupiter.api.Test;
import sk.tuke.gamestudio.game.slitherlink.entity.Comment;
import sk.tuke.gamestudio.game.slitherlink.service.CommentService;
import sk.tuke.gamestudio.game.slitherlink.service.CommentServiceJDBS;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentServiceTest {

    private final CommentService commentService = new CommentServiceJDBS();

    @Test
    public void resetTest() {
        commentService.reset();
        assertEquals(0, commentService.getComments("slitherlink").size());
    }

    @Test
    public void addComment() {
        commentService.reset();
        var date = new Date();
        commentService.addComment(new Comment("Jaro", "slitherlink", "hi", date));

        var comments = commentService.getComments("slitherlink");
        assertEquals(1, comments.size());
        assertEquals("slitherlink", comments.get(0).getGame());
        assertEquals("Jaro", comments.get(0).getPlayer());
        assertEquals("hi", comments.get(0).getComment());
        assertEquals(date, comments.get(0).getCommentedOn());
        commentService.reset();
    }

    @Test
    public void getLastComments() {

        commentService.reset();
        var date1 = new Date();
        commentService.addComment(new Comment("Jaro", "slitherlink", "hi", date1));
        var date2 = new Date();
        commentService.addComment(new Comment("Katka", "slitherlink", "hello", date2));
        var date3 = new Date();
        commentService.addComment(new Comment("Zuzka", "tiles", "good day", date3));
        var date4 = new Date();
        commentService.addComment(new Comment("Jung-kook", "slitherlink", "loong-time no see", date4));

        var comments = commentService.getComments("slitherlink");

        assertEquals(3, comments.size());
        assertEquals("slitherlink", comments.get(0).getGame());
        assertEquals("Jung-kook", comments.get(0).getPlayer());
        assertEquals("loong-time no see", comments.get(0).getComment());
        assertEquals(date4, comments.get(0).getCommentedOn());

        assertEquals("slitherlink", comments.get(1).getGame());
        assertEquals("Katka", comments.get(1).getPlayer());
        assertEquals("hello", comments.get(1).getComment());
        assertEquals(date2, comments.get(1).getCommentedOn());

        assertEquals("slitherlink", comments.get(2).getGame());
        assertEquals("Jaro", comments.get(2).getPlayer());
        assertEquals("hi", comments.get(2).getComment());
        assertEquals(date1, comments.get(2).getCommentedOn());
        commentService.reset();
    }

}
