package sk.tuke.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentServiceRest {

    @Autowired
    private CommentService commentService;

    //GET -> http://localhost:8080/api/comment/slitherlink
    @GetMapping("/{game}")
    public List<Comment> getComments(@PathVariable String game){
        return commentService.getComments(game);
    }

    //POST -> http://localhost:8080/api/comment
    @PostMapping
    public void addComment(@RequestBody Comment comment){
        commentService.addComment(comment);
    }

}