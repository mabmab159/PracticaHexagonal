package project.hex.domains.Product;

import java.util.List;

public interface ProductGateway {
    List<Product> findAll();

    Product findById(Long id);

    Product save(Product product);

    void deleteById(Long id);

    Product updateById(Long id, Product product);
}
