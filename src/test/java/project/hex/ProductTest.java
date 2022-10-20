package project.hex;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import project.hex.data.ProductEntity;
import project.hex.data.ProductRepository;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
public class ProductTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getAllProducts() throws Exception {
        when(productRepository.findAll()).thenReturn(List.of(createProduct(1L, "nombre1", 1L), createProduct(2L, "nombre2", 2L)));
        mockMvc.perform(MockMvcRequestBuilders.get("/product"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].nombre", is("nombre1")))
                .andExpect(jsonPath("$[0].precio", is(1)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].nombre", is("nombre2")))
                .andExpect(jsonPath("$[1].precio", is(2)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    //Insertar test para obtener un solo producto

    @Test
    public void saveProduct() throws Exception {
        ProductEntity productEntity = createProduct(1L, "nombre1", 1L);
        when(productRepository.save(createProduct(1L, "nombre1", 1L))).thenReturn(productEntity);
        mockMvc.perform(MockMvcRequestBuilders.post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productEntity)))
                .andExpect(jsonPath("$.*", hasSize(3)))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nombre", is("nombre1")))
                .andExpect(jsonPath("$.precio", is(1)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    //Insertar test de actualizar

    @Test
    public void deleteProduct() throws Exception {
        when(productRepository.findById(1L)).thenReturn(Optional.of(createProduct(1L, "nombre1", 1L)));
        mockMvc.perform(MockMvcRequestBuilders.delete("/product/1"))
                .andExpect(status().isNoContent());
    }


    private ProductEntity createProduct(Long id, String name, Long precio) {
        ProductEntity product = new ProductEntity();
        product.setId(id);
        product.setNombre(name);
        product.setPrecio(precio);
        return product;
    }

}
