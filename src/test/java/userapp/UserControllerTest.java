package userapp;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import java.util.Map;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.org.apache.commons.lang3.builder.EqualsBuilder;
import userapp.model.User;
import userapp.service.UserServiceImpl;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerTest {

    private static Map<Long, User> users = UserServiceImpl.getUsers();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        User user = createUser();
        user.setId(1L);
        users.put(1L, user);
    }

    @AfterEach
    void clear() {
        users.remove(1L);
    }

    @Test
    @DisplayName("Get all users")
    public void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("Create a new user")
    public void testAddUser() throws Exception {
        Long userId = 1L;
        User user = users.get(userId);
        User expected = new User();
        expected.setId(1L);
        expected.setEmail(user.getEmail());
        expected.setFirstName(user.getFirstName());
        expected.setLastName(user.getLastName());
        expected.setBirthDate(user.getBirthDate());
        expected.setAddress(user.getAddress());
        expected.setPhoneNumber(user.getPhoneNumber());

        String jsonRequest = objectMapper.writeValueAsString(user);

        MvcResult mvcResult = mockMvc.perform(post("/api/users")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        User actual = objectMapper.readValue(mvcResult.getResponse()
                .getContentAsString(), User.class);

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("Delete a user by id")
    void deleteUser_ValidId_Success() throws Exception {
        mockMvc.perform(delete("/api/users/delete/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andReturn();
    }

    @Test
    @DisplayName("Update all user fields by id")
    void updateUserAllFields_ValidId_Success() throws Exception {
        Long userId = 1L;

        User expected = createUser();
        expected.setId(userId);
        expected.setEmail("user777@example.com");
        expected.setFirstName("Jack");
        expected.setLastName("London");
        expected.setBirthDate(new Date(90, 9, 15));
        expected.setAddress("999 Main Street");
        expected.setPhoneNumber("+123456789");

        User user = users.get(userId);

        users.put(userId, user);

        String jsonRequest = objectMapper.writeValueAsString(user);

        MvcResult mvcResult = mockMvc.perform(put("/api/users/updateAllFields/1", userId)
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        User actual = objectMapper.readValue(mvcResult.getResponse()
                .getContentAsString(), User.class);

        users.put(userId, actual);

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("Update one field of user by id")
    void updateOneFieldUser_ValidId_Success() throws Exception {
        Long userId = 1L;

        User user = users.get(userId);

        User expected = users.get(userId);

        user.setLastName("Johnson");

        expected.setLastName(user.getLastName());

        String jsonRequest = objectMapper.writeValueAsString(user);

        MvcResult mvcResult = mockMvc.perform(patch("/api/users/update/{userId}", user.getId())
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        User actual = objectMapper.readValue(mvcResult.getResponse()
                .getContentAsString(), User.class);

        Assertions.assertNotNull(actual);
        EqualsBuilder.reflectionEquals(expected, actual, "id");
    }

    @Test
    @DisplayName("Get all users in range")
    public void testGetAllUsersInRange() throws Exception {
        users.remove(1L);
        User userInput = new User("John", new Date(90, 0, 1));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/users/getInRange")
                        .param("from", "1989-04-01") // April 1, 1989
                        .param("to", "1999-04-30")) // April 30, 1999
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    private User createUser() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setFirstName("Jack");
        user.setLastName("Jackson");
        user.setBirthDate(new Date(90, 9, 15));
        user.setAddress("123 Main Street");
        user.setPhoneNumber("+12345678");
        return user;
    }
}
