package project.hex.domains.User;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class User {
    private Long id;
    private String nombre;
    private String apellidos;
    private String correo;
    private String password;
}
