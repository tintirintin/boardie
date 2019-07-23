package com.hsbc.boardie.controllers;


import com.hsbc.boardie.model.Action;
import com.hsbc.boardie.model.Post;
import com.hsbc.boardie.model.User;
import com.hsbc.boardie.services.BoardieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
public class BoardieController {

    private final BoardieService service;

    @Autowired
    public BoardieController(BoardieService service){
        this.service = service;
    }

    @GetMapping("/rest/wall")
    @ResponseBody
    public Collection<Post> getWall() {
        return service.getWall();
    }

    @GetMapping("/rest/userwall/{user}")
    @ResponseBody
    public Collection<Post> getUserWall(@PathVariable("user") String login) {
        return service.getUserWall(login);
    }

    @GetMapping("/rest/timeline/{user}")
    @ResponseBody
    public Collection<Post> getTimeline(@PathVariable("user") String login){
        return service.getTimeline(login);
    }

    @GetMapping("/rest/post/{id}")
    @ResponseBody
    public Post getPostByID(@PathVariable("id") long id){
        return service.getPostByID(id);
    }

    @PostMapping("/rest/message")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public synchronized Post createMessage(@RequestBody Action action){
        return service.createMessage(action);
    }

    @PostMapping("/rest/follow")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @ResponseBody
    public synchronized User followUser(@RequestBody Action action){
        return service.followUser(action);
    }
}
