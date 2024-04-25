package userapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import userapp.model.User;
import userapp.service.UserServiceImpl;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @PutMapping("/update/{userId}")
    public User updateUser(@PathVariable Long userId, @Validated @RequestBody User updatedUser) {
        return userService.update(userId, updatedUser);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PostMapping("/create")
    public User addUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete/{userId}")
    public User deleteUser(@PathVariable Long userId) {
        return userService.deleteUser(userId);
    }
    @PutMapping("/updateAllFields/{userId}")
    public User updateAllFieldsUser(@PathVariable Long userId, @RequestBody User user) {
        return userService.updateAllFields(userId, user);
    }

    @GetMapping("/getInRange")
    public List<User> getUserByBirthday(@RequestParam("from") @DateTimeFormat(pattern = "yyyy-MM-dd") Date from, @RequestParam(value = "to", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date to) {
        return userService.findByBirthdayRange(from, to);
    }
}
