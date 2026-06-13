package ag.sorokchat.users.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String login;
    private String displayName;
    private String password;
    private List<Role> roles;
}
