package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.Comment;
import sk.tuke.gamestudio.entity.Rating;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.game.slitherlink.elements.*;
import sk.tuke.gamestudio.game.slitherlink.field.Field;
import sk.tuke.gamestudio.game.slitherlink.field.GameState;
import sk.tuke.gamestudio.service.CommentService;
import sk.tuke.gamestudio.service.RatingService;
import sk.tuke.gamestudio.service.ScoreService;

import java.util.Date;


@Controller
@RequestMapping("/slitherlink")
@Scope(WebApplicationContext.SCOPE_SESSION)
public class SlitherlinkController {
    @Autowired
    private UserController userController;
    @Autowired
    private ScoreService scoreService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RatingService ratingService;
    private Field field = new Field(3,3,1, 1);
    private boolean marking;

    @RequestMapping
    public String slitherlink(@RequestParam(required = false) Integer row, @RequestParam(required = false) Integer column, Model model){
        processComand(row, column);
        fillModel(model);
        return "slitherlink";
    }

    private void processComand(Integer row, Integer column) {
        if (row != null && column != null)
            try{
                if (field.getFieldState() == GameState.PLAYING){
                    if (marking)
                        field.markLine(row, column);
                    else
                        field.drawLine(row, column);
                    if(field.getFieldState() == GameState.SOLVED && userController.isLoggedUser())
                        scoreService.addScore(new Score(userController.getLoggedUser().getLogin(), "slitherlink", field.getScore(), new Date()));
                }


            } catch (Exception e) {
                System.out.println("Input is not valid :" + e);
            }
    }

    private void fillModel(Model model){
        model.addAttribute("scores", scoreService.getTopScores("slitherlink"));
        model.addAttribute("comments", commentService.getComments("slitherlink"));
    }
    @RequestMapping("/rating")
    public String rating(String rating){
        ratingService.setRating(new Rating(userController.getLoggedUser().getLogin(), "slitherlink", Integer.parseInt(rating), new Date()));
        return "redirect:/slitherlink";
    }

    @RequestMapping("/comment")
    public String comment(String comment){
        commentService.addComment(new Comment(userController.getLoggedUser().getLogin(), "slitherlink", comment, new Date()));
        return "redirect:/slitherlink";
    }

    @RequestMapping("/new")
    public String newGame(){
        field = new Field(3,3,1, 1);
        return "redirect:/slitherlink";
    }

    @RequestMapping("/mark")
    public String changeMarking(){
        marking = !marking;
        return "slitherlink";
    }

    @RequestMapping(value = "/field", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String slitherlink(@RequestParam(required = false) Integer row, @RequestParam(required = false) Integer column){
        processComand(row, column);
        return getHTMLField();
    }

    public String getAverageRating(){
        return Integer.toString(ratingService.getAverageRating("slitherlink"));
    }
    public String getRating(){
        return Integer.toString(ratingService.getRating(userController.getLoggedUser().getLogin(), "slitherlink"));
    }
    public String getState(){
        return field.getFieldState().toString();
    }

    public boolean isMarking() {
        return marking;
    }

    public String getHTMLField(){

        StringBuilder sb = new StringBuilder();
        sb.append("<table>\n");
        for (int row = 0; row < field.getRowCount(); row++) {
            sb.append("<tr>\n");
            for (int column = 0; column < field.getColumnCount(); column++) {
                var element = field.getElement(row, column);
                sb.append("<td>\n");
                sb.append("<a href='/slitherlink?row=" + row + "&column=" + column + "'>\n");
                sb.append("<img src='/images/" + getNameImage(element, row, column) + ".png'>\n");

                sb.append("</a>\n");
                sb.append("</td>\n");
            }
            sb.append("</tr>\n");
        }

        sb.append("</table>\n");

        return sb.toString();
    }

    private String getNameImage(Element element, int row, int column) {

        if (element instanceof Dot) {
            return "dot";

        } else if (element instanceof Clue) {
            switch (((Clue) element).getClueState()) {
                case VISIBLE -> {
                    return "no"+((Clue) element).getValue();
                }
                case HIDDEN -> {
                    return "white";
                }
            }

        } else if (element instanceof Line) {
            if (((Line) element).getLineState() == LineState.DRAWN) {
                if (row == 0 || row == field.getRowCount() - 1) {
                    return "lineh";
                } else if (column == 0 || column == field.getColumnCount() - 1) {
                    return "linev";
                } else if (column % 2 != 0) {
                    return "lineh";
                } else {
                    return "linev";
                }
            } else if (((Line) element).getLineState() == LineState.MARKED) {
                return "cross";
            } else {
                return "white";
            }
        }

        return "X";
    }

}
