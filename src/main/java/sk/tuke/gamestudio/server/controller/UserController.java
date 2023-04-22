package sk.tuke.gamestudio.server.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import sk.tuke.gamestudio.entity.User;

@Controller
@Scope(WebApplicationContext.SCOPE_SESSION)
public class UserController {

    private User loggedUser;

    @RequestMapping("/login")
    public String login(String login, String password){
        if(password.equals("heslo")) {
            loggedUser = new User(login);
            return "redirect:/slitherlink";
        }

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
