package project.hex.domains.Product;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final ProductGateway productGateway;

    public ProductService(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    public List<Product> findAll() {
        return productGateway.findAll();
    }

    public Product findById(Long id) {
        return productGateway.findById(id);
    }

    public Product save(Product product) {
        return productGateway.save(product);
    }

    public void deletedById(Long id) {
        productGateway.deleteById(id);
    }

    public Product updateById(Long id, Product product) {
        return productGateway.updateById(id, product);
    }

}
