package sk.tuke.gamestudio.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.service.UserService;

import javax.persistence.Access;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class UserController {

    private User loggedUser;

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String login(String login, String password){
        if(userService.isLoginCorrect(login, password)) {
            loggedUser = new User(login, password);
            return "redirect:/slitherlink";
        }

        return "redirect:/";
    }

    @RequestMapping("/register")
    public String register(String login, String password){
        userService.addUser(new User(login, password));
        return "redirect:/";
    }

    @RequestMapping("/logout")
    public String logout(){
        loggedUser = null;
        return "redirect:/";
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public boolean isLoggedUser() {
        return loggedUser != null;
    }
}
