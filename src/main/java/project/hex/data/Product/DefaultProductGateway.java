package project.hex.data.Product;

import org.springframework.stereotype.Component;
import project.hex.domains.Product.Product;
import project.hex.domains.Product.ProductGateway;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class DefaultProductGateway implements ProductGateway {
    private final ProductRepository productRepository;

    public DefaultProductGateway(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> findAll() {
        return productRepository.findAll()
                .stream()
                .map(p -> toModel(p))
                .collect(Collectors.toList());
    }

    @Override
    public Product findById(Long id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        return toModel(productEntity);
    }

    @Override
    public Product save(Product product) {
        return toModel(productRepository.save(toEntity(product)));
    }

    @Override
    public void deleteById(Long id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        productRepository.deleteById(productEntity.getId());
    }


    public Product updateById(Long id, Product product) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto " + "no encontrado"));
        productEntity.setNombre(product.getNombre());
        productEntity.setPrecio(product.getPrecio());
        return toModel(productRepository.save(productEntity));
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
