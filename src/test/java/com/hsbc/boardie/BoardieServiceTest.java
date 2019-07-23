package com.hsbc.boardie;

import com.hsbc.boardie.exceptions.*;
import com.hsbc.boardie.model.Action;
import com.hsbc.boardie.model.Post;
import com.hsbc.boardie.services.BoardieService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.Collection;

@SpringBootTest
@RunWith(SpringRunner.class)
public class BoardieServiceTest {

    @Autowired
    private BoardieService service;

    private String msg = "Assertion failed!";

    @After
    public void cleanUp(){
        service.getWall().clear();
    }

    @Test
    public void testCreateMessage() {
        String login = "Cartman"; String content = "But mooooooom!";
        Post post = service.createMessage(new Action(login, content));

        Assert.isTrue(post.getUser().getLogin().equals(login), msg);
        Assert.isTrue(post.getMessage().getMessage().equals(content), msg);
        Assert.isTrue(post.getMessage().getTimestamp() != null, msg);
    }

    @Test
    public void testCreateTwoMessages() {
        String login1 = "Cartman"; String content1 = "But mooooooom!";
        Post post1 = service.createMessage(new Action(login1, content1));

        String login2 = "Kenny"; String content2 = "###@$#$@#@@!@!";
        Post post2 = service.createMessage(new Action(login2, content2));

        Assert.isTrue(post1.getUser().getLogin().equals(login1), msg);
        Assert.isTrue(post1.getMessage().getMessage().equals(content1), msg);
        Assert.isTrue(post1.getMessage().getTimestamp() != null, msg);

        Assert.isTrue(post2.getUser().getLogin().equals(login2), msg);
        Assert.isTrue(post2.getMessage().getMessage().equals(content2), msg);
        Assert.isTrue(post2.getMessage().getTimestamp() != null, msg);
    }

    @Test
    public void testCreateTwoMessagesWithSameLogin() {
        String login1 = "Cartman"; String content1 = "But mooooooom!";
        Post post1 = service.createMessage(new Action(login1, content1));

        String content2 = "###@$#$@#@@!@!";
        Post post2 = service.createMessage(new Action(login1, content2));

        Assert.isTrue(post1.getUser().getLogin().equals(login1), msg);
        Assert.isTrue(post1.getMessage().getMessage().equals(content1), msg);
        Assert.isTrue(post1.getMessage().getTimestamp() != null, msg);

        Assert.isTrue(post2.getUser().getLogin().equals(login1), msg);
        Assert.isTrue(post2.getMessage().getMessage().equals(content2), msg);
        Assert.isTrue(post2.getMessage().getTimestamp() != null, msg);
    }

    @Test (expected = WrongMessageException.class)
    public void testCreateEmptyMessage() {
        String login = "Cartman"; String content = "";
        service.createMessage(new Action(login, content));
    }

    @Test (expected = WrongMessageException.class)
    public void testCreateMessageOver140Chars() {
        String tooLongMsg = new String(new char[141]).replace('\0', 'X');
        String login = "Cartman"; String content = tooLongMsg;
        service.createMessage(new Action(login, content));
    }

    @Test (expected = BadLoginException.class)
    public void testCreateMessageWithEmptyLogin() {
        String login = ""; String content = "Some message";
        service.createMessage(new Action(login, content));
    }

    @Test (expected = BadLoginException.class)
    public void testCreateMessageWithBadLogin() {
        String login = "   $%^"; String content = "Some message";
        service.createMessage(new Action(login, content));
    }

    @Test
    public void testGetWall(){
        String login1 = "Cartman"; String content1 = "But mooooooom!";
        Post post1 = service.createMessage(new Action(login1, content1));

        String login2 = "Kenny"; String content2 = "###@$#$@#@@!@!";
        Post post2 = service.createMessage(new Action(login2, content2));

        Collection<Post> board = service.getWall();

        Assert.isTrue(board.size() == 2, msg);
        Assert.isTrue(board.contains(post1), msg);
        Assert.isTrue(board.contains(post2), msg);
    }

    @Test
    public void testGetEmptyWall(){
        Collection<Post> board = service.getWall();

        Assert.isTrue(board.isEmpty(), msg);
    }

    @Test
    public void testGetUserWall(){
        String login1 = "Cartman"; String content1 = "But mooooooom!";
        Post post1 = service.createMessage(new Action(login1, content1));

        String login2 = "Kenny"; String content2 = "###@$#$@#@@!@!";
        Post post2 = service.createMessage(new Action(login2, content2));

        String content3 = "#@%#$%^#@$%!&^&*))()!";
        Post post3 = service.createMessage(new Action(login2, content3));

        Collection<Post> board = service.getUserWall(login2);

        Assert.isTrue(board.size() == 2, msg);
        Assert.isTrue(!board.contains(post1), msg);
        Assert.isTrue(board.contains(post2), msg);
        Assert.isTrue(board.contains(post3), msg);
    }

    @Test (expected = BadLoginException.class)
    public void testGetUserEmptyLogin(){
        String login1 = "Cartman"; String content1 = "But mooooooom!";
        service.createMessage(new Action(login1, content1));

        String login2 = "Kenny"; String content2 = "###@$#$@#@@!@!";
        service.createMessage(new Action(login2, content2));

        String login3 = "";

        service.getUserWall(login3);
    }

    @Test (expected = BadLoginException.class)
    public void testGetUserBadLogin(){
        String login1 = "Cartman"; String content1 = "But mooooooom!";
        service.createMessage(new Action(login1, content1));

        String login2 = "Kenny"; String content2 = "###@$#$@#@@!@!";
        service.createMessage(new Action(login2, content2));

        String login3 = "%% % ^ ^";

        service.getUserWall(login3);
    }

    @Test (expected = UserNotFoundException.class)
    public void testGetUserWallUnknownUser(){
        String login1 = "Cartman"; String content1 = "But mooooooom!";
        service.createMessage(new Action(login1, content1));

        String login2 = "Kenny"; String content2 = "###@$#$@#@@!@!";
        service.createMessage(new Action(login2, content2));

        String login3 = "Kyle";

        service.getUserWall(login3);
    }

    @Test
    public void testGetTimeline(){
        String login1 = "Cartman"; String content1 = "But mooooooom!";
        Post post1 = service.createMessage(new Action(login1, content1));

        String login2 = "Kenny"; String content2 = "###@$#$@#@@!@!";
        Post post2 = service.createMessage(new Action(login2, content2));

        String content3 = "#@%#$%^#@$%!&^&*))()!";
        Post post3 = service.createMessage(new Action(login2, content3));

        String login4 = "Kyle"; String content4 = "I've learned something today...";
        Post post4 = service.createMessage(new Action(login4, content4));

        String content5 = "Well... nothing.";
        Post post5 = service.createMessage(new Action(login4, content5));

        service.followUser(new Action(login1, login2));
        service.followUser(new Action(login1, login4));

        Collection<Post> board = service.getTimeline(login1);

        Assert.isTrue(board.size() == 4, msg);
        Assert.isTrue(!board.contains(post1), msg);
        Assert.isTrue(board.contains(post2), msg);
        Assert.isTrue(board.contains(post3), msg);
        Assert.isTrue(board.contains(post4), msg);
        Assert.isTrue(board.contains(post5), msg);
    }

    @Test (expected = UserNotFoundException.class)
    public void testGetTimelineUnknownUser(){
        String login1 = "Cartman"; String content1 = "But mooooooom!";
        service.createMessage(new Action(login1, content1));

        String login2 = "Kenny"; String content2 = "###@$#$@#@@!@!";
        service.createMessage(new Action(login2, content2));

        String content3 = "#@%#$%^#@$%!&^&*))()!";
        service.createMessage(new Action(login2, content3));

        String login4 = "Kyle";

        service.followUser(new Action(login1, login2));

        service.getTimeline(login4);
    }

    @Test (expected = BadLoginException.class)
    public void testGetTimelineEmptyLogin(){
        String login1 = "Cartman"; String content1 = "But mooooooom!";
        service.createMessage(new Action(login1, content1));

        String login2 = "Kenny"; String content2 = "###@$#$@#@@!@!";
        service.createMessage(new Action(login2, content2));

        String content3 = "#@%#$%^#@$%!&^&*))()!";
        service.createMessage(new Action(login2, content3));

        String login4 = "";

        service.followUser(new Action(login1, login2));

        service.getTimeline(login4);
    }

    @Test (expected = BadLoginException.class)
    public void testGetTimelineBadLogin(){
        String login1 = "Cartman"; String content1 = "But mooooooom!";
        service.createMessage(new Action(login1, content1));

        String login2 = "Kenny"; String content2 = "###@$#$@#@@!@!";
        service.createMessage(new Action(login2, content2));

        String content3 = "#@%#$%^#@$%!&^&*))()!";
        service.createMessage(new Action(login2, content3));

        String login4 = "@#$$ #@$@#";

        service.followUser(new Action(login1, login2));

        service.getTimeline(login4);
    }

    @Test
    public void testGetPostByID(){
        String login1 = "Cartman"; String content1 = "But mooooooom!";
        Post post1 = service.createMessage(new Action(login1, content1));

        String login2 = "Kenny"; String content2 = "###@$#$@#@@!@!";
        Post post2 = service.createMessage(new Action(login2, content2));

        long id = post2.getId();

        Post post3 = service.getPostByID(id);

        Assert.isTrue(post3.equals(post2), msg);
    }

    @Test (expected = MessageNotFoundException.class)
    public void testGetPostByIDNotFound(){
        String login1 = "Cartman"; String content1 = "But mooooooom!";
        Post post1 = service.createMessage(new Action(login1, content1));

        String login2 = "Kenny"; String content2 = "###@$#$@#@@!@!";
        Post post2 = service.createMessage(new Action(login2, content2));

        long id = Long.MAX_VALUE;

        Post post3 = service.getPostByID(id);
    }

    @Test
    public void testFollowUser(){
        String login1 = "Cartman"; String content1 = "But mooooooom!";
        Post post1 = service.createMessage(new Action(login1, content1));

        String login2 = "Kenny"; String content2 = "###@$#$@#@@!@!";
        Post post2 = service.createMessage(new Action(login2, content2));

        String content3 = "#@%#$%^#@$%!&^&*))()!";
        Post post3 = service.createMessage(new Action(login2, content3));

        String login4 = "Kyle"; String content4 = "I've learned something today...";
        Post post4 = service.createMessage(new Action(login4, content4));

        String content5 = "Well... nothing.";
        Post post5 = service.createMessage(new Action(login4, content5));

        service.followUser(new Action(login1, login2));
        service.followUser(new Action(login1, login4));

        Assert.isTrue(post1.getUser().getFollowing().contains(post2.getUser().getLogin()),msg);
        Assert.isTrue(post1.getUser().getFollowing().contains(post4.getUser().getLogin()),msg);
        Assert.isTrue(!post1.getUser().getFollowing().contains(post1.getUser().getLogin()),msg);
    }

    @Test (expected = BadLoginException.class)
    public void testFollowUserEmptyLogin(){
        String login1 = "";

        String login2 = "Kenny"; String content2 = "###@$#$@#@@!@!";
        service.createMessage(new Action(login2, content2));

        service.followUser(new Action(login1, login2));
    }

    @Test (expected = BadLoginException.class)
    public void testFollowUserBadLogin(){
        String login1 = "!@# 13";

        String login2 = "Kenny"; String content2 = "###@$#$@#@@!@!";
        service.createMessage(new Action(login2, content2));

        service.followUser(new Action(login1, login2));
    }

    @Test (expected = BadFollowException.class)
    public void testFollowYourself(){
        String login1 = "Cartman"; String content1 = "But mooooooom!";
        service.createMessage(new Action(login1, content1));

        service.followUser(new Action(login1, login1));
    }

    @Test (expected = UserNotFoundException.class)
    public void testFollowNotFound(){
        String login1 = "Cartman"; String content1 = "But mooooooom!";
        service.createMessage(new Action(login1, content1));

        String login2 = "Kenny"; String content2 = "###@$#$@#@@!@!";
        service.createMessage(new Action(login2, content2));

        String login3 = "Kyle";

        service.followUser(new Action(login1, login3));
    }
}
