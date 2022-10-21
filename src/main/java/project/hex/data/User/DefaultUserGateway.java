package project.hex.data.User;

import org.springframework.stereotype.Component;
import project.hex.domains.User.User;
import project.hex.domains.User.UserGateway;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DefaultUserGateway implements UserGateway {

    private final UserRepository userRepository;

    public DefaultUserGateway(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll()
                .stream()
                .map(p -> toModel(p))
                .collect(Collectors.toList());
    }

    @Override
    public User save(User user) {
        return toModel(userRepository.save(toEntity(user)));
    }

    private User toModel(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .nombre(userEntity.getNombres())
                .apellidos(userEntity.getApellidos())
                .correo(userEntity.getCorreo())
                .password(userEntity.getPassword())
                .build();
    }

    private UserEntity toEntity(User user) {
        return UserEntity.builder()
                .id(user.getId())
                .nombres(user.getNombre())
                .apellidos(user.getApellidos())
                .correo(user.getCorreo())
                .password(user.getPassword())
                .build();
    }
}
