package ag.sorokchat.users.repository;

import ag.sorokchat.users.mapper.UserMapper;
import ag.sorokchat.users.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class DefaultUsersRepository implements UsersRepository {
    private final JdbcClient jdbcClient;
    private final UserMapper mapper;

    @Override
    public Optional<User> findById(Long id) {
        final var sql = "select * from sorokchat.users_with_roles where id = ?";
        return jdbcClient
                .sql(sql)
                .param(1, id)
                .query(mapper)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        final var sql = "select * from sorokchat.users_with_roles where login = ?";
        return jdbcClient
                .sql(sql)
                .param(1, login)
                .query(mapper)
                .stream()
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        final var sql = "select * from sorokchat.users_with_roles";
        return jdbcClient
                .sql(sql)
                .query(mapper)
                .list();
    }

    @Override
    public User create(User user) {
        final var sql = "select * from sorokchat.create_user(?, ?, ?)";
        return jdbcClient
                .sql(sql)
                .param(1, user.getLogin())
                .param(2, user.getPassword())
                .param(3, user.getDisplayName())
                .query(mapper)
                .stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public User update(User user) {
        final var sql = "update sorokchat.users set login = ?, display_name = ? where id = ?";
        if (existsById(user.getId())) {
            final var affectedRows = jdbcClient
                    .sql(sql)
                    .param(1, user.getLogin())
                    .param(2, user.getDisplayName())
                    .param(3, user.getId())
                    .update();
            return findById(user.getId()).orElse(null);
        }
        return user;
    }

    @Override
    public boolean existsById(Long id) {
        final var sql = "select exists(select 1 from sorokchat.users_with_roles where id = ?)";
        return jdbcClient
                .sql(sql)
                .param(1, id)
                .query(Boolean.class)
                .single();
    }

    @Override
    public void deleteById(Long id) {
        final var sql = "delete from sorokchat.users where id = ?";
        jdbcClient
                .sql(sql)
                .param(1, id)
                .update();
    }
}
