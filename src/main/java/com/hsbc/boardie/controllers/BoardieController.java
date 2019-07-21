package com.hsbc.boardie.controllers;


import com.hsbc.boardie.model.Action;
import com.hsbc.boardie.model.Post;
import com.hsbc.boardie.service.BoardieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.Collection;

@RestController
public class BoardieController {

    @Autowired
    private BoardieService service;

    @GetMapping("/rest/wall")
    @ResponseBody
    public Collection<Post> getCurrentWall() {
        return service.getCurrentWall();
    }

    @GetMapping("/rest/timeline/{user}")
    public Collection<Post> getTimeline(@PathVariable("user") String login){
        return service.getTimeline(login);
    }

    @PostMapping("/rest/message")
    @ResponseStatus(HttpStatus.CREATED)
    public synchronized Post createMessage(@RequestBody Action action){
        return service.createMessage(action);
    }

    @PostMapping("/rest/follow")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public synchronized Action followUser(@RequestBody Action action){
        return service.followUser(action);
    }
}
