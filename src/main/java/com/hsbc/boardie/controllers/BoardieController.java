package com.hsbc.boardie.controllers;


import com.hsbc.boardie.exceptions.BadFollowException;
import com.hsbc.boardie.exceptions.BadLoginException;
import com.hsbc.boardie.exceptions.UserNotFoundException;
import com.hsbc.boardie.exceptions.WrongMessageException;
import com.hsbc.boardie.model.Message;
import com.hsbc.boardie.model.Action;
import com.hsbc.boardie.model.Post;
import com.hsbc.boardie.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class BoardieController {

    private static Deque<Post> board = new ArrayDeque<>();

    // PRIVATES
    private User getUser(String login){
        for (Post post : board){
            if (post.getUser().getLogin().equals(login)){
                return post.getUser();
            }
        }
        return null;
    }

    private Date getCurrentSystemDate(){
        return new Date(System.currentTimeMillis());
    }

    // MAPPINGS
    @GetMapping("/rest/wall")
    @ResponseBody
    public Collection<Post> getCurrentWall() {
        return board;
    }

    @GetMapping("/rest/timeline/{user}")
    public Collection<Post> getTimeline(@PathVariable("user") String login){

        if (login.isEmpty()){
            throw new BadLoginException();
        }
        User user = getUser(login);
        if (user == null) {
            throw new UserNotFoundException();
        }
        Set<String> following = user.getFollowing();

        List<Post> followingPosts = new ArrayList<>();
        for (String follow : following){
            followingPosts.addAll(board.stream().filter(post -> post.getUser().getLogin().equals(follow)).collect(Collectors.toList()));
        }
        return followingPosts;

    }


    @PostMapping("/rest/message")
    @ResponseStatus(HttpStatus.CREATED)
    public synchronized Post createMessage(@RequestBody Action action){
        int maxMessageLength = 140;

        if (action.getLogin().isEmpty()) {
            throw new BadLoginException();
        }

        if (action.getContent().isEmpty() || action.getContent().length() > maxMessageLength) {
            throw new WrongMessageException();
        }

        Message message = new Message(action.getContent(), getCurrentSystemDate());
        User user = getUser(action.getLogin());
        if (user == null){
            user = new User(action.getLogin());
        }
        Post post = new Post(user, message);
        board.addFirst(post);
        return post;
    }

    @PostMapping("/rest/follow")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public synchronized Action followUser(@RequestBody Action action){
        if (action.getLogin().isEmpty() || action.getContent().isEmpty()) {
            throw new BadLoginException();
        }
        if (action.getLogin().equals(action.getContent())){
            throw new BadFollowException();
        }

        User user = getUser(action.getLogin());
        User follow = getUser(action.getContent());

        if (user == null || follow == null){
            throw new UserNotFoundException();
        }

        user.addToFollowing(follow.getLogin());

        return action;
    }
}
