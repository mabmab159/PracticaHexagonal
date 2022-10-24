package project.hex.web;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import project.hex.domains.User.User;
import project.hex.domains.User.UserService;

import java.util.Date;
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

    @GetMapping("/{usuario}")
    public String getUsuario(@PathVariable String usuario) {
        return getJWTToken(usuario);
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

    private String getJWTToken(String username) {
        String secretKey = "mySecretKey";
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        String token = Jwts
                .builder()
                .setId("softtekJWT")
                .setSubject(username)
                .claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // Token valido x 1 hora
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes())
                .compact();

        return "Bearer " + token;
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
