package ag.sorokchat.users.repository;

import ag.sorokchat.users.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class DefaultUsersRepository implements UsersRepository {
    private final List<User> users = new LinkedList<>();

    @Override
    public Optional<User> findById(Long id) {
        return users
                .stream()
                .filter(user -> user.getId().equals(id))
                .findFirst();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return users
                .stream()
                .filter(user -> user.getLogin().equals(login))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return Collections.unmodifiableList(users);
    }

    @Override
    public User create(User user) {
        var newIndex = users.stream()
                .map(User::getId)
                .max(Long::compare)
                .map(id -> id + 1)
                .orElse(1L);
        user.setId(newIndex);
        users.add(user);
        return user;
    }

    @Override
    public User update(User user) {
        var candidate = findById(user.getId()).orElse(null);
        if (candidate == null) {
            return user;
        }
        candidate.setLogin(user.getLogin());
        candidate.setPassword(user.getPassword());
        candidate.setDisplayName(user.getDisplayName());
        return candidate;
    }

    @Override
    public boolean existsById(Long id) {
        return users.stream().anyMatch(user -> user.getId().equals(id));
    }

    @Override
    public void deleteById(Long id) {
        users.removeIf(user -> user.getId().equals(id));
    }
}
