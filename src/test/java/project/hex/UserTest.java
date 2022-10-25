package project.hex;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import project.hex.data.User.UserEntity;
import project.hex.data.User.UserRepository;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class UserTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Test
    public void getAllUsers() throws Exception {
        when(userRepository.findAll()).thenReturn(List.of(createUser(1L, "miguel", "berrio", "m@m", "123", false),
                createUser(2L, "juan", "berrio", "j@j", "987", false)));
        mockMvc.perform(MockMvcRequestBuilders.get("/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nombre", is("miguel")))
                .andExpect(jsonPath("$[0].apellidos", is("berrio")))
                .andExpect(jsonPath("$[0].correo", is("m@m")))
                .andExpect(jsonPath("$[0].password", is(desencriptarClave("123", bCryptPasswordEncoder.encode("123")))))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].nombre", is("juan")))
                .andExpect(jsonPath("$[1].apellidos", is("berrio")))
                .andExpect(jsonPath("$[1].correo", is("j@j")))
                .andExpect(jsonPath("$[1].password", is(desencriptarClave("987", bCryptPasswordEncoder.encode("987")))))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }


    private String desencriptarClave(String password, String hashString) {
        if (bCryptPasswordEncoder.matches(password, hashString)) {
            return password;
        }
        return "";
    }

    private UserEntity createUser(Long id, String nombres, String apellidos, String correo, String password, boolean isDeleted) {
        UserEntity user = new UserEntity();
        user.setId(id);
        user.setNombres(nombres);
        user.setApellidos(apellidos);
        user.setCorreo(correo);
        user.setPassword(password);
        user.setDeleted(isDeleted);
        return user;
    }
}
