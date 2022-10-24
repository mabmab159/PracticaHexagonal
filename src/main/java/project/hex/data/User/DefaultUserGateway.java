package project.hex.data.User;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        return toModel(userRepository.save(toEntity(user)));
    }

    @Override
    public User editUser(Long id, User user) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no " + "encontrado"));
        userEntity.setNombres(user.getNombre());
        userEntity.setApellidos(user.getApellidos());
        userEntity.setCorreo(user.getCorreo());
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userEntity.setPassword(encoder.encode(user.getPassword()));
        return toModel(userRepository.save(userEntity));
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
