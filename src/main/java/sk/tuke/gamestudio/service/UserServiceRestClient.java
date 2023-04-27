package sk.tuke.gamestudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Score;
import sk.tuke.gamestudio.entity.User;

import java.util.Arrays;

public class UserServiceRestClient implements UserService{

    private String url = "http://localhost:8080/api/user";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addUser(User user) {
        restTemplate.postForEntity(url, user, User.class);

    }

    @Override
    public boolean isLoginCorrect(String login, String password) {
        return Boolean.TRUE.equals(restTemplate.getForEntity(url + "/" + login + "/" + password, Boolean.class).getBody());
    }

    @Override
    public boolean isTheSameUser(String login) {
        return Boolean.TRUE.equals(restTemplate.getForEntity(url + "/" + login, Boolean.class).getBody());
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported on web service");
    }
}
