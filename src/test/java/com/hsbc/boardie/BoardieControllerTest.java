package com.hsbc.boardie;


import com.hsbc.boardie.controllers.BoardieController;
import com.hsbc.boardie.exceptions.*;
import com.hsbc.boardie.model.Action;
import com.hsbc.boardie.model.Message;
import com.hsbc.boardie.model.Post;
import com.hsbc.boardie.model.User;
import com.hsbc.boardie.services.BoardieService;
import net.minidev.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Assert;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.atomic.AtomicLong;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasValue;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = BoardieController.class)
@AutoConfigureMockMvc
public class BoardieControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BoardieService service;

    private String failed = "Assertion failed!";

    @Test
    public void checkEmptyWall() throws Exception {

        when(service.getWall()).thenReturn(new ArrayDeque<>());

        mockMvc.perform(get("/rest/wall").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8));

        verify(service, times(1)).getWall();
    }

    @Test
    public void checkWallWithPosts() throws Exception {

        AtomicLong al = new AtomicLong();
        Deque<Post> board = new ArrayDeque<>();
        board.addFirst(new Post(al.incrementAndGet(), new User("Cartman"), new Message("But mooooooom!")));
        board.addFirst(new Post(al.incrementAndGet(), new User("Kenny"), new Message("###@$#$@#@@!@!")));
        board.addFirst(new Post(al.incrementAndGet(), new User("Kyle"), new Message("I've learned something today...")));
        board.addFirst(new Post(al.incrementAndGet(), new User("Stan"), new Message("That's impossible!")));
        when(service.getWall()).thenReturn(board);

        mockMvc.perform(get("/rest/wall").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].user", hasValue("Stan")))
                .andExpect(jsonPath("$[1].message", hasValue("I've learned something today...")))
                .andExpect(jsonPath("$[2].user", hasValue("Kenny")))
                .andExpect(jsonPath("$[3].message", hasValue("But mooooooom!")));

        verify(service, times(1)).getWall();

    }

    @Test
    public void checkUserWall() throws Exception {

        Deque<Post> board = new ArrayDeque<>();
        board.addFirst(new Post(1, new User("Kenny"), new Message("###@$#$@#@@!@!")));
        board.addFirst(new Post(2, new User("Kenny"), new Message("$^%^$^#@?!")));
        when(service.getUserWall("Kenny")).thenReturn(board);

        mockMvc.perform(get("/rest/userwall/Kenny").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].user", hasValue("Kenny")))
                .andExpect(jsonPath("$[1].message", hasValue("###@$#$@#@@!@!")));

        verify(service, times(1)).getUserWall("Kenny");
    }

    @Test
    public void checkUserWallNoUser() throws Exception {

        Deque<Post> board = new ArrayDeque<>();
        board.addFirst(new Post(1, new User("Kenny"), new Message("###@$#$@#@@!@!")));
        when(service.getUserWall("Kenny")).thenReturn(board);
        when(service.getUserWall("Cartman")).thenThrow(new UserNotFoundException());

        String error =
        mockMvc.perform(get("/rest/userwall/Cartman").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getErrorMessage() + "";

        Assert.isTrue(error.contains("Login or user you follow not found!"), failed);

        verify(service, times(1)).getUserWall("Cartman");
    }

    @Test
    public void checkEmptyTimeline() throws Exception {

        when(service.getTimeline("Kyle")).thenReturn(new ArrayDeque<>());

        mockMvc.perform(get("/rest/timeline/Kyle").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(0)));

        verify(service, times(1)).getTimeline("Kyle");
    }

    @Test
    public void checkTimelineWithPosts() throws Exception {

        AtomicLong al = new AtomicLong();
        Deque<Post> board = new ArrayDeque<>();
        board.addFirst(new Post(al.incrementAndGet(), new User("Cartman"), new Message("But mooooooom!")));
        board.addFirst(new Post(al.incrementAndGet(), new User("Kenny"), new Message("###@$#$@#@@!@!")));
        board.addFirst(new Post(al.incrementAndGet(), new User("Stan"), new Message("That's impossible!")));
        when(service.getTimeline("Kyle")).thenReturn(board);

        mockMvc.perform(get("/rest/timeline/Kyle").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].user", hasValue("Stan")))
                .andExpect(jsonPath("$[1].message", hasValue("###@$#$@#@@!@!")))
                .andExpect(jsonPath("$[2].user", hasValue("Cartman")));

        verify(service, times(1)).getTimeline("Kyle");
    }

    @Test
    public void checkTimelineNoUser() throws Exception {

        Deque<Post> board = new ArrayDeque<>();
        board.addFirst(new Post(1, new User("Kenny"), new Message("###@$#$@#@@!@!")));
        when(service.getTimeline("Kenny")).thenReturn(board);
        when(service.getTimeline("Cartman")).thenThrow(new UserNotFoundException());

        String error =
                mockMvc.perform(get("/rest/timeline/Cartman").contentType(MediaType.APPLICATION_JSON_UTF8))
                        .andExpect(status().isBadRequest()).andReturn().getResponse().getErrorMessage() + "";

        Assert.isTrue(error.contains("Login or user you follow not found!"), failed);

        verify(service, times(1)).getTimeline("Cartman");
    }

    @Test
    public void checkPostById() throws Exception {

        long id = 5;
        Post post = new Post(id, new User("Cartman"), new Message("But mooooooom!"));
        when(service.getPostByID(id)).thenReturn(post);

        mockMvc.perform(get("/rest/post/5").contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.user", hasValue("Cartman")))
                .andExpect(jsonPath("$.message", hasValue("But mooooooom!")));

        verify(service, times(1)).getPostByID(5);
    }

    @Test
    public void checkPostByIdNotFound() throws Exception {

        when(service.getPostByID(6)).thenThrow(new MessageNotFoundException());

        String error =
                mockMvc.perform(get("/rest/post/6").contentType(MediaType.APPLICATION_JSON_UTF8))
                        .andExpect(status().isBadRequest()).andReturn().getResponse().getErrorMessage() + "";

        Assert.isTrue(error.contains("Message has not been found!"), failed);

        verify(service, times(1)).getPostByID(6);
    }

    @Test
    public void checkCreateMessage() throws Exception {

        long id = 5;
        Post post = new Post(id, new User("Stan"), new Message("That's impossible!"));
        when(service.createMessage(any(Action.class))).thenReturn(post);

        mockMvc.perform(post("/rest/message")
                .content("{\"login\":\"Stan\",\"content\":\"That's impossible!\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.user", hasValue("Stan")))
                .andExpect(jsonPath("$.message", hasValue("That's impossible!")));

        verify(service, times(1)).createMessage(any(Action.class));
    }

    @Test
    public void checkCreateEmptyOrTooLongMessage() throws Exception {

        when(service.createMessage(any(Action.class))).thenThrow(new WrongMessageException());

        String error =
        mockMvc.perform(post("/rest/message")
                .content("{\"login\":\"Stan\",\"content\":\"\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getErrorMessage() + "";

        Assert.isTrue(error.contains("Empty or too long message!"), failed);

        verify(service, times(1)).createMessage(any(Action.class));
    }

    @Test
    public void checkCreateMessageWithEmptyLogin() throws Exception {

        when(service.createMessage(any(Action.class))).thenThrow(new BadLoginException());

        String error =
                mockMvc.perform(post("/rest/message")
                        .content("{\"login\":\"\",\"content\":\"Something\"}")
                        .contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest()).andReturn().getResponse().getErrorMessage() + "";

        Assert.isTrue(error.contains("Empty login name!"), failed);

        verify(service, times(1)).createMessage(any(Action.class));
    }

    @Test
    public void checkFollowUser() throws Exception {

        User user = new User("Stan");
        user.addToFollowing("Cartman");
        when(service.followUser(any(Action.class))).thenReturn(user);

        JSONArray array = new JSONArray();
        array.appendElement("Cartman");

        mockMvc.perform(post("/rest/follow")
                .content("{\"login\":\"Stan\",\"content\":\"Cartman\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.login", is("Stan")))
                .andExpect(jsonPath("$.following", is(array)));

        verify(service, times(1)).followUser(any(Action.class));
    }

    @Test
    public void checkFollowToSelf() throws Exception {

        when(service.followUser(any(Action.class))).thenThrow(new BadFollowException());

        String error =
        mockMvc.perform(post("/rest/follow")
                .content("{\"login\":\"Stan\",\"content\":\"Stan\"}")
                .contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getErrorMessage() + "";

        Assert.isTrue(error.contains("You cannot follow yourself! :)"), failed);

        verify(service, times(1)).followUser(any(Action.class));
    }

    @Test
    public void checkFollowUserNoUser() throws Exception {

        when(service.followUser(any(Action.class))).thenThrow(new UserNotFoundException());

        String error =
                mockMvc.perform(post("/rest/follow")
                        .content("{\"login\":\"Stan\",\"content\":\"Trololololo\"}")
                        .contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest()).andReturn().getResponse().getErrorMessage() + "";

        Assert.isTrue(error.contains("Login or user you follow not found!"), failed);

        verify(service, times(1)).followUser(any(Action.class));
    }

    @Test
    public void checkFollowWithEmptyLogin() throws Exception {

        when(service.followUser(any(Action.class))).thenThrow(new BadLoginException());

        String error =
                mockMvc.perform(post("/rest/follow")
                        .content("{\"login\":\"\",\"content\":\"Stan\"}")
                        .contentType(MediaType.APPLICATION_JSON_UTF8).accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isBadRequest()).andReturn().getResponse().getErrorMessage() + "";

        Assert.isTrue(error.contains("Empty login name!"), failed);

        verify(service, times(1)).followUser(any(Action.class));
    }
}
