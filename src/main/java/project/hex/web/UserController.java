package project.hex.web;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import project.hex.domains.User.User;
import project.hex.domains.User.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> getAllUser() {
        List<User> user = userService.findAll();
        List<UserDTO> userDTOS = user.stream()
                .map(p -> toDTO(p))
                .collect(Collectors.toList());
        return new ResponseEntity<>(userDTOS, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDTO> save(@RequestBody UserDTO userDTO) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userDTO.password = encoder.encode(userDTO.password);
        User user = userService.save(toModel(userDTO));
        return new ResponseEntity<>(toDTO(user), HttpStatus.CREATED);
    }

    private UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .nombre(user.getNombre())
                .apellidos(user.getApellidos())
                .correo(user.getCorreo())
                .password(user.getPassword())
                .build();
    }

    private User toModel(UserDTO userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .nombre(userDTO.getNombre())
                .apellidos(userDTO.getApellidos())
                .correo(userDTO.getCorreo())
                .password(userDTO.getPassword())
                .build();
    }

    @Data
    @Builder
    private static class UserDTO {
        private Long id;
        private String nombre;
        private String apellidos;
        private String correo;
        private String password;
    }

}
