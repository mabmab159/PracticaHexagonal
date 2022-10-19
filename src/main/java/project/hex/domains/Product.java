package project.hex.domains;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Product {
    private Long id;
    private String nombre;
    private Long precio;
}
