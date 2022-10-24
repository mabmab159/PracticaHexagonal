package project.hex.domains.User;

import java.util.List;

public interface UserGateway {

    List<User> findAll();

    User save(User user);

    User editUser(Long id, User user);
}
