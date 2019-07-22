package com.hsbc.boardie.services;

import com.hsbc.boardie.exceptions.*;
import com.hsbc.boardie.model.Action;
import com.hsbc.boardie.model.Message;
import com.hsbc.boardie.model.Post;
import com.hsbc.boardie.model.User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class BoardieService {

    private static Deque<Post> board = new ArrayDeque<>();
    private AtomicLong postId = new AtomicLong();

    private User getUser(String login){
        if (login.isEmpty()){
            throw new BadLoginException();
        }
        for (Post post : board){
            if (post.getUser().getLogin().equals(login)){
                return post.getUser();
            }
        }
        return null;
    }

    public Collection<Post> getWall(){
        return board;
    }

    public Collection<Post> getUserWall(String login){
        User user = getUser(login);
        if (user == null) {
            throw new UserNotFoundException();
        }
        return board.stream().filter(post -> post.getUser().getLogin().equals(login)).collect(Collectors.toList());
    }

    public Collection<Post> getTimeline(String login){
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

    public Post getPostByID(long id){
        Post found = board.stream().filter(post -> post.getId() == id).findAny().orElse(null);
        if (found == null) {
            throw new MessageNotFoundException();
        }
        return found;
    }

    public synchronized Post createMessage(Action action){
        int maxMessageLength = 140;

        if (action.getLogin().isEmpty()) {
            throw new BadLoginException();
        }

        if (action.getContent().isEmpty() || action.getContent().length() > maxMessageLength) {
            throw new WrongMessageException();
        }

        // make login a-z 0-9 and underscore
        Message message = new Message(action.getContent());
        User user = getUser(action.getLogin());
        if (user == null){
            user = new User(action.getLogin());
        }
        Post post = new Post(postId.incrementAndGet(), user, message);
        board.addFirst(post);
        return post;
    }

    public synchronized User followUser(Action action){
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

        return user;
    }
}
