package project.hex.web;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.hex.domains.Product;
import project.hex.domains.ProductGateway;
import project.hex.domains.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("product")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    private ResponseEntity<List<ProductDTO>> getAllProduct() {
        List<Product> product = productService.findAll();
        List<ProductDTO> productDTO = product.stream().map(p -> toDTO(p)).collect(Collectors.toList());
        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    private ResponseEntity<ProductDTO> findProduct(@PathVariable Long id) {
        Product product = productService.findById(id);
        ProductDTO productDTO = toDTO(product);
        return ResponseEntity.ok().body(productDTO);
    }

    @PostMapping
    private ResponseEntity<ProductDTO> save(@RequestBody ProductDTO productDTO) {
        Product product = toModel(productDTO);
        Product productSave = productService.save(product);
        ProductDTO result = toDTO(productSave);
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<Void> deleteById(@PathVariable Long id) {
        productService.deletedById(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @Data
    @Builder
    private static class ProductDTO {
        private Long id;
        private String nombre;
        private double precio;
    }

    @Data
    @Builder
    private static class ProductBasicDTO {
        private Long id;
        private String nombre;
        private double precio;
    }

    private ProductBasicDTO toDTOBasic(Product product) {
        return ProductBasicDTO.builder()
                .id(product.getId())
                .nombre(product.getNombre())
                .precio(product.getPrecio())
                .build();
    }

    private ProductDTO toDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .nombre(product.getNombre())
                .precio(product.getPrecio())
                .build();
    }

    private Product toModel(ProductDTO productDTO) {
        return Product.builder()
                .id(productDTO.getId())
                .nombre(productDTO.getNombre())
                .precio(productDTO.getPrecio())
                .build();
    }
}