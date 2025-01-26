package ch.hearc.jee2024.tourismapi;

import ch.hearc.jee2024.tourismapi.entity.Location;
import ch.hearc.jee2024.tourismapi.entity.User;
import ch.hearc.jee2024.tourismapi.service.LocationService;
import ch.hearc.jee2024.tourismapi.service.UserService;
import ch.hearc.jee2024.tourismapi.utils.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TourismApiApplicationTests {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LocationService locationService;

    @Autowired
    private UserService userService;


    @Test
    public void getLocationsShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/locations"))
                .andExpect(status().isOk());
    }

    @Test
    public void getRatingsShouldReturnOk() throws Exception {
        User user = userService.createUser("User", "password", Role.USER);
        Location location = locationService.submitLocation("Test Location", "A test description", 46.2044, 6.1432, user.getId());

        mockMvc.perform(get("/api/locations/" + location.getId() + "/ratings"))
                .andExpect(status().isOk());
    }


    @Test
    public void submitLocationShouldReturnOk() throws Exception {
        User user = userService.createUser("TestUser", "password", Role.USER);

        String locationJson = "{" +
                "\"name\": \"Test Location\"," +
                "\"description\": \"A test location\"," +
                "\"latitude\": 46.2044," +
                "\"longitude\": 6.1432" +
                "}";

        mockMvc.perform(post("/api/locations")
                        .param("userId", user.getId().toString())
                        .content(locationJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getLocationShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/locations"))
                .andExpect(status().isOk());
    }

    @Test
    public void validateLocationShouldReturnOkForAdmin() throws Exception {
        User admin = userService.createUser("Admin", "password", Role.ADMIN);
        Location location = locationService.submitLocation("Test Location", "A test description", 46.2044, 6.1432, admin.getId());

        mockMvc.perform(put("/api/locations/" + location.getId() + "/validate")
                        .param("adminId", admin.getId().toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void validateLocationShouldReturnForbiddenForUser() throws Exception {
        User user = userService.createUser("User", "password", Role.USER);
        Location location = locationService.submitLocation("Test Location", "A test description", 46.2044, 6.1432, user.getId());

        mockMvc.perform(put("/api/locations/" + location.getId() + "/validate")
                        .param("adminId", user.getId().toString()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void rejectLocationShouldReturnOkForAdmin() throws Exception {
        User admin = userService.createUser("Admin", "password", Role.ADMIN);
        Location location = locationService.submitLocation("Test Location", "A test description", 46.2044, 6.1432, admin.getId());

        mockMvc.perform(delete("/api/locations/" + location.getId() + "/reject")
                        .param("adminId", admin.getId().toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void rejectLocationShouldReturnForbiddenForUser() throws Exception {
        User user = userService.createUser("User", "password", Role.USER);
        Location location = locationService.submitLocation("Test Location", "A test description", 46.2044, 6.1432, user.getId());

        mockMvc.perform(delete("/api/locations/" + location.getId() + "/reject")
                        .param("adminId", user.getId().toString()))
                .andExpect(status().isForbidden());
    }

    @Test
    public void addRatingShouldReturnBadRequest() throws Exception {
        User user = userService.createUser("User", "password", Role.USER);
        Location location = locationService.submitLocation("Test Location", "A test description", 46.2044, 6.1432, user.getId());

        String ratingRequestJson = "{" +
                "\"userId\": " + user .getId() + "," +
                "\"ratingValue\": 4" +
                "}";

        mockMvc.perform(post("/api/locations/" + location.getId() + "/rate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ratingRequestJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addRatingShouldReturnOk() throws Exception {
        User admin = userService.createUser("User", "password", Role.ADMIN);
        Location location = locationService.submitLocation("Test Location", "A test description", 46.2044, 6.1432, admin.getId());

        String ratingRequestJson = "{" +
                "\"userId\": " + admin.getId() + "," +
                "\"ratingValue\": 4" +
                "}";

        mockMvc.perform(post("/api/locations/" + location.getId() + "/rate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ratingRequestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(4));
    }

    @Test
    public void getUsersShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk());
    }

    @Test
    public void getAdminsShouldReturnOk() throws Exception {
        mockMvc.perform(get("/api/users/admins"))
                .andExpect(status().isOk());
    }

    @Test
    public void registerUserShouldReturnOk() throws Exception {
        String userJson = "{" +
                "\"name\": \"User Test\"," +
                "\"password\": \"Pa$$w0rd\"" +
                "}";

        mockMvc.perform(post("/api/users/register")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    public void registerAdminShouldReturnOk() throws Exception {
        String adminJson = "{" +
                "\"name\": \"Admin Test\"," +
                "\"password\": \"Pa$$w0rd\"," +
                "\"role\": \"ADMIN\"" +
                "}";

        mockMvc.perform(post("/api/users/register")
                        .content(adminJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    public void registerUserShouldReturnBadRequest() throws Exception {
        String userJson = "{" +
                "\"name\": \"User Test\"," +
                "\"password\": \"Pa$$w0rd\"," +
                "\"role\": \"TEST\"" +
                "}";

        mockMvc.perform(post("/api/users/register")
                        .content(userJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
