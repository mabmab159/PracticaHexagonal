package project.hex.data;

import org.springframework.stereotype.Component;
import project.hex.domains.Product;
import project.hex.domains.ProductGateway;
import project.hex.domains.ProductService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class DefaultProductGateway implements ProductGateway {

    private final ProductRepository productRepository;

    public DefaultProductGateway(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll().stream().map(p -> toModel(p)).collect(Collectors.toList());
    }

    @Override
    public Product findById(Long id) {
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return toModel(productEntity);
    }

    @Override
    public Product save(Product product) {
        return toModel(productRepository.save(toEntity(product)));
    }

    public void deleteById(Long id) {
        ProductEntity productEntity = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        productRepository.deleteById(productEntity.getId());
    }

    private Product toModel(ProductEntity productEntity) {
        return Product.builder()
                .id(productEntity.getId())
                .nombre(productEntity.getNombre())
                .precio(productEntity.getPrecio())
                .build();
    }

    private ProductEntity toEntity(Product product) {
        return ProductEntity.builder()
                .id(product.getId())
                .nombre(product.getNombre())
                .precio(product.getPrecio())
                .build();
    }
}
