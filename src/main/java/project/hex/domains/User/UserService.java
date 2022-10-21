package project.hex.domains.User;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserGateway userGateway;

    public UserService(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    public List<User> findAll() {
        return userGateway.findAll();
    }

    public User save(User user) {
        return userGateway.save(user);
    }
}
