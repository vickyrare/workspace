package io.codecrafts.service;

/**
 * Created by waqqas on 4/22/2018.
 */
import io.codecrafts.model.Post;
import io.codecrafts.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserRestService {

    private final RestTemplate restTemplate;

    @Autowired
    public UserRestService(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    public List<User> findAll() {
        return Arrays.stream(restTemplate.getForObject("http://localhost:5000/api/users", User[].class)).collect(Collectors.toList());
    }
}
