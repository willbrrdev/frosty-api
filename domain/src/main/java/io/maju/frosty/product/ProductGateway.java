package io.maju.frosty.product;

import io.maju.frosty.pagination.Pagination;
import io.maju.frosty.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface ProductGateway {

    Product create(Product aProduct);

    Product update(Product aProduct);

    Optional<Product> findById(ProductID anId);

    void deleteById(ProductID anId);

    Pagination<Product> findAll(SearchQuery aQuery);

    List<ProductID> existsByIds(Iterable<ProductID> ids);
}
