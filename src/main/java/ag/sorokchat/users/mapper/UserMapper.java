package ag.sorokchat.users.mapper;

import ag.sorokchat.users.model.Role;
import ag.sorokchat.users.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        Array rawRoles = rs.getArray("roles");
        List<Role> roles = rawRoles != null
                ? Arrays.stream((String[]) rawRoles.getArray())
                  .map(Role::valueOf)
                  .toList()
                : List.of();
        return User
                .builder()
                .id(rs.getLong("id"))
                .login(rs.getString("login"))
                .password(rs.getString("password"))
                .displayName(rs.getString("display_name"))
                .roles(roles)
                .build();
    }
}
