package userapp.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import userapp.exception.FromIsBeforeToException;
import userapp.exception.UserNotFoundException;
import userapp.model.User;

@Service
public class UserServiceImpl {

    private static final String EMAIL_PATTERN = "^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$";
    private static final int OLD_ENOUGH = 18;
    private static final Long INCREMENT_SIZE = 1L;
    private static Map<Long, User> users = new HashMap<>();

    public static Map<Long, User> getUsers() {
        return users;
    }

    public static List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    public User createUser(User user) {
        Date birthDate = user.getBirthDate();
        if (!isUserOldEnough(birthDate)) {
            throw new IllegalArgumentException("User must be at least 18 years old.");
        }

        long nextId = getNextUserId();
        user.setId(nextId);
        users.put(nextId, user);
        return user;
    }

    private long getNextUserId() {
        return users.size() + INCREMENT_SIZE;
    }

    private static boolean isUserOldEnough(Date birthDate) {
        LocalDate currentDate = LocalDate.now();
        LocalDate birthLocalDate = birthDate.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        int age = Period.between(birthLocalDate, currentDate).getYears();
        return age >= OLD_ENOUGH;
    }

    public static User deleteUser(Long userId) {
        return users.remove(userId);
    }

    public User getUserById(Long userId) {
        User user = users.get(userId);
        return user;
    }

    public User update(@PathVariable Long userId, @RequestBody User user) {
        User userToUpdate = getUserById(userId);
        if (userToUpdate != null) {
            if ((user.getBirthDate() == null) || (LocalDate.now()
                    .minusYears(user.getBirthDate().getYear()).getYear()) >= 18
                    && user.getEmail().matches(EMAIL_PATTERN)) {
                if (user.getEmail() != null) {
                    userToUpdate.setEmail(user.getEmail());
                }

                if (user.getFirstName() != null) {
                    userToUpdate.setFirstName(user.getFirstName());
                }

                if (user.getLastName() != null) {
                    userToUpdate.setLastName(user.getLastName());
                }

                if (user.getBirthDate() != null) {
                    userToUpdate.setBirthDate(user.getBirthDate());
                }

                if (user.getBirthDate() != null) {
                    userToUpdate.setBirthDate(user.getBirthDate());
                }

                if (user.getAddress() != null) {
                    userToUpdate.setAddress(user.getAddress());
                }

                if (user.getPhoneNumber() != null) {
                    userToUpdate.setPhoneNumber(user.getPhoneNumber());
                }

                users.put(userToUpdate.getId(), userToUpdate);
                return userToUpdate;
            } else {
                throw new IllegalArgumentException("Illegal argument for user");
            }
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    public User updateAllFields(@PathVariable Long userId, @RequestBody User user) {
        users.put(userId, getUserById(userId));
        User userFullUpdate = getUserById(userId);
        if (userFullUpdate != null) {
            if ((LocalDate.now().minusYears(user.getBirthDate().getYear())
                    .getYear()) >= OLD_ENOUGH
                    && user.getEmail().matches(EMAIL_PATTERN)) {
                userFullUpdate.setEmail(user.getEmail());
                userFullUpdate.setFirstName(user.getFirstName());
                userFullUpdate.setLastName(user.getLastName());
                userFullUpdate.setBirthDate(user.getBirthDate());
                userFullUpdate.setAddress(user.getAddress());
                users.put(userFullUpdate.getId(), userFullUpdate);
                return userFullUpdate;
            } else {
                throw new IllegalArgumentException("Illegal argument for user");
            }
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    public List<User> findByBirthdayRange(Date from, Date to) {
        List<User> result = new ArrayList<>();

        for (User user : users.values()) {
            if (from.before(to)) {
                if (user.getBirthDate().after(from) && user.getBirthDate().before(to)) {
                    result.add(user);
                }
            } else {
                throw new FromIsBeforeToException("From must be before to!");
            }
        }
        return result;
    }
}
