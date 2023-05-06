package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

import java.time.Duration;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.List;


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
    private Field field = new Field(4,4,1, 1);
    private boolean marking;


    public void ChangeField(int rowCount, int columnCount){
        field = new Field(rowCount, columnCount, 1, 1);

    }

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
    }
    @RequestMapping("/rating")
    public String rating(@RequestParam String rating){
        if(userController.isLoggedUser()){
            ratingService.setRating(new Rating(userController.getLoggedUser().getLogin(), "slitherlink", Integer.parseInt(rating), new Date()));
        }
        return "redirect:/slitherlink";
    }

    @RequestMapping(value = "/comment")
    public String comment(String comment){
        if(userController.isLoggedUser()) {
            commentService.addComment(new Comment(userController.getLoggedUser().getLogin(), "slitherlink", comment, new Date()));
        }
        return "redirect:/slitherlink";
    }

    @RequestMapping(value = "/comment/HTML", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String commentHTML(@RequestParam(required = false) String comment){
        if(userController.isLoggedUser() && comment!=null){
            commentService.addComment(new Comment(userController.getLoggedUser().getLogin(), "slitherlink", comment, new Date()));
        }
        return getHTMLComments();
    }


    @RequestMapping("/new")
    public String newGame(){
        field = new Field(4,4,1, 1);
        return "redirect:/slitherlink";
    }

    @PostMapping("/mark")
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
        if(userController.isLoggedUser()){
            return Integer.toString(ratingService.getRating(userController.getLoggedUser().getLogin(), "slitherlink"));
        }
        return "0";
    }
    public String getState(){
        return field.getFieldState().toString();
    }

    @GetMapping("/api/isMarking")
    @ResponseBody
    public boolean isMarking() {
        return marking;
    }

    public String getHTMLComments(){
        StringBuilder sb = new StringBuilder();
        sb.append("<ol>\n");
            List<Comment> comments = commentService.getComments("slitherlink");
            for(Comment i : comments){
                sb.append("<li>\n");
                sb.append("<span>"+i.getPlayer()+": "+"</span>\n");
                sb.append("<span>"+i.getComment()+"</span>\n");
                Date date1 = i.getCommentedOn();
                Date date2 = new Date();
                long diffInMilliseconds = Math.abs(date2.getTime() - date1.getTime());
                long diffInSeconds = diffInMilliseconds / 1000;
                long diffInMinutes = diffInSeconds / 60;
                long diffInHours = diffInMinutes / 60;
                long diffInDays = diffInHours / 24;

                if(diffInSeconds<60){
                    sb.append("<span>"+diffInSeconds+" seconds ago</span>\n");
                } else if (diffInMinutes<60) {
                    sb.append("<span>"+diffInMinutes+" minutes ago</span>\n");
                } else if (diffInHours<24) {
                    sb.append("<span>"+diffInHours+" hours ago</span>\n");
                } else {
                    sb.append("<span>"+diffInDays+" days ago</span>\n");
                }

                sb.append("</li>\n");
            }
        sb.append("</ol>\n");
        return sb.toString();
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
                sb.append(getNameImage(element, row, column));

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
            return "<svg xmlns=\"http://www.w3.org/2000/svg\" fill=\"currentColor\" class=\"bi bi-dot\" viewBox=\"0 0 16 16\">\n" +
                    "  <path d=\"M8 9.5a1.5 1.5 0 1 0 0-3 1.5 1.5 0 0 0 0 3z\"/>\n" +
                    "</svg>";

        } else if (element instanceof Clue) {
            switch (((Clue) element).getClueState()) {
                case VISIBLE -> {
                    switch (((Clue) element).getValue()){
                        case 0: {
                            return "<svg xmlns=\"http://www.w3.org/2000/svg\" fill=\"currentColor\" class=\"bi bi-0-circle-fill\" viewBox=\"0 0 16 16\">\n" +
                                    "  <path d=\"M8 4.951c-1.008 0-1.629 1.09-1.629 2.895v.31c0 1.81.627 2.895 1.629 2.895s1.623-1.09 1.623-2.895v-.31c0-1.8-.621-2.895-1.623-2.895Z\"/>\n" +
                                    "  <path d=\"M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0Zm-8.012 4.158c1.858 0 2.96-1.582 2.96-3.99V7.84c0-2.426-1.079-3.996-2.936-3.996-1.864 0-2.965 1.588-2.965 3.996v.328c0 2.42 1.09 3.99 2.941 3.99Z\"/>\n" +
                                    "</svg>";
                        }
                        case 1: {
                            return "<svg xmlns=\"http://www.w3.org/2000/svg\" fill=\"currentColor\" class=\"bi bi-1-circle-fill\" viewBox=\"0 0 16 16\">\n" +
                                    "  <path d=\"M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0ZM9.283 4.002H7.971L6.072 5.385v1.271l1.834-1.318h.065V12h1.312V4.002Z\"/>\n" +
                                    "</svg>";
                        }
                        case 2: {
                            return "<svg xmlns=\"http://www.w3.org/2000/svg\" fill=\"currentColor\" class=\"bi bi-2-circle-fill\" viewBox=\"0 0 16 16\">\n" +
                                    "  <path d=\"M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0ZM6.646 6.24c0-.691.493-1.306 1.336-1.306.756 0 1.313.492 1.313 1.236 0 .697-.469 1.23-.902 1.705l-2.971 3.293V12h5.344v-1.107H7.268v-.077l1.974-2.22.096-.107c.688-.763 1.287-1.428 1.287-2.43 0-1.266-1.031-2.215-2.613-2.215-1.758 0-2.637 1.19-2.637 2.402v.065h1.271v-.07Z\"/>\n" +
                                    "</svg>";
                        }
                        case 3: {
                            return "<svg xmlns=\"http://www.w3.org/2000/svg\" fill=\"currentColor\" class=\"bi bi-3-circle-fill\" viewBox=\"0 0 16 16\">\n" +
                                    "  <path d=\"M16 8A8 8 0 1 1 0 8a8 8 0 0 1 16 0Zm-8.082.414c.92 0 1.535.54 1.541 1.318.012.791-.615 1.36-1.588 1.354-.861-.006-1.482-.469-1.54-1.066H5.104c.047 1.177 1.05 2.144 2.754 2.144 1.653 0 2.954-.937 2.93-2.396-.023-1.278-1.031-1.846-1.734-1.916v-.07c.597-.1 1.505-.739 1.482-1.876-.03-1.177-1.043-2.074-2.637-2.062-1.675.006-2.59.984-2.625 2.12h1.248c.036-.556.557-1.054 1.348-1.054.785 0 1.348.486 1.348 1.195.006.715-.563 1.237-1.342 1.237h-.838v1.072h.879Z\"/>\n" +
                                    "</svg>";
                        }
                    }
                }
                case HIDDEN -> {
                    return "<svg xmlns=\"http://www.w3.org/2000/svg\" fill=\"currentColor\" class=\"bi bi-square-fill black-square\" viewBox=\"0 0 16 16\">\n" +
                            "  <path d=\"M0 2a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V2z\"/>\n" +
                            "</svg>";
                }
            }

        } else if (element instanceof Line) {
            if (((Line) element).getLineState() == LineState.DRAWN) {
                if (row == 0 || row == field.getRowCount() - 1) {
                    return "<svg xmlns=\"http://www.w3.org/2000/svg\" fill=\"currentColor\" class=\"bi bi-three-dots\" viewBox=\"0 0 16 16\">\n" +
                            "  <path d=\"M3 9.5a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3zm5 0a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3zm5 0a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3z\"/>\n" +
                            "</svg>";
                } else if (column == 0 || column == field.getColumnCount() - 1) {
                    return "<svg xmlns=\"http://www.w3.org/2000/svg\" fill=\"currentColor\" class=\"bi bi-three-dots-vertical\" viewBox=\"0 0 16 16\">\n" +
                            "  <path d=\"M9.5 13a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0zm0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0zm0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0z\"/>\n" +
                            "</svg>";
                } else if (column % 2 != 0) {
                    return "<svg xmlns=\"http://www.w3.org/2000/svg\" fill=\"currentColor\" class=\"bi bi-three-dots\" viewBox=\"0 0 16 16\">\n" +
                            "  <path d=\"M3 9.5a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3zm5 0a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3zm5 0a1.5 1.5 0 1 1 0-3 1.5 1.5 0 0 1 0 3z\"/>\n" +
                            "</svg>";
                } else {
                    return "<svg xmlns=\"http://www.w3.org/2000/svg\" fill=\"currentColor\" class=\"bi bi-three-dots-vertical\" viewBox=\"0 0 16 16\">\n" +
                            "  <path d=\"M9.5 13a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0zm0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0zm0-5a1.5 1.5 0 1 1-3 0 1.5 1.5 0 0 1 3 0z\"/>\n" +
                            "</svg>";
                }
            } else if (((Line) element).getLineState() == LineState.MARKED) {
                return "<svg xmlns=\"http://www.w3.org/2000/svg\" fill=\"currentColor\" class=\"bi bi-x-square\" viewBox=\"0 0 16 16\">\n" +
                        "  <path d=\"M14 1a1 1 0 0 1 1 1v12a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1h12zM2 0a2 2 0 0 0-2 2v12a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2H2z\"/>\n" +
                        "  <path d=\"M4.646 4.646a.5.5 0 0 1 .708 0L8 7.293l2.646-2.647a.5.5 0 0 1 .708.708L8.707 8l2.647 2.646a.5.5 0 0 1-.708.708L8 8.707l-2.646 2.647a.5.5 0 0 1-.708-.708L7.293 8 4.646 5.354a.5.5 0 0 1 0-.708z\"/>\n" +
                        "</svg>";
            } else {
                return "<svg xmlns=\"http://www.w3.org/2000/svg\" fill=\"currentColor\" class=\"bi bi-square-fill black-square\" viewBox=\"0 0 16 16\">\n" +
                        "  <path d=\"M0 2a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v12a2 2 0 0 1-2 2H2a2 2 0 0 1-2-2V2z\"/>\n" +
                        "</svg>";
            }
        }

        return "X";
    }

}
