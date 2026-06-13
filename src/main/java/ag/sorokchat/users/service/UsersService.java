package ag.sorokchat.users.service;

import ag.sorokchat.users.model.Role;
import ag.sorokchat.users.contract.NewUserPayload;
import ag.sorokchat.users.model.User;
import ag.sorokchat.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository repository;
    private final PasswordEncoder passwordEncoder;

    public User create(NewUserPayload newUser) {
        return repository.create(new NewUserPayload(
                newUser.login(),
                passwordEncoder.encode(newUser.password()),
                newUser.displayName()
        ));
    }

    public List<User> getAll() {
        return repository.findAll();
    }

    public Optional<User> getById(Long id) {
        return repository.findById(id);
    }

    public Optional<User> getByLogin(String login) {
        return repository.findByLogin(login);
    }
}
