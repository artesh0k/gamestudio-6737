package sk.tuke.gamestudio.server.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.entity.User;
import sk.tuke.gamestudio.service.ScoreService;
import sk.tuke.gamestudio.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserServiceRest {

    @Autowired
    private UserService userService;

    //POST -> http://localhost:8080/api/user
    @PostMapping
    void addUser(@RequestBody User user){
        userService.addUser(user);
    }

    //GET -> http://localhost:8080/api/user/login/password
    @GetMapping("/{login}/{password}")
    boolean isLoginCorrect(@PathVariable String login,@PathVariable String password){
        return userService.isLoginCorrect(login, password);
    }

    //GET -> http://localhost:8080/api/user/login
    @GetMapping("/{login}")
    boolean isTheSameUser(@PathVariable String login){
        return userService.isTheSameUser(login);
    }

}
