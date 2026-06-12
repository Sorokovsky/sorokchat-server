package ag.sorokchat.users.repository;

import ag.sorokchat.users.model.User;

import java.util.List;
import java.util.Optional;

public interface UsersRepository {
    Optional<User> findById(Long id);
    Optional<User> findByLogin(String login);
    List<User> findAll();
    User create(User user);
    void deleteById(Long id);
    boolean existsById(Long id);
    User update(User user);
}
