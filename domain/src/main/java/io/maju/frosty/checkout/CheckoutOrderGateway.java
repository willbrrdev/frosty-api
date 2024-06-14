package io.maju.frosty.checkout;

import io.maju.frosty.pagination.Pagination;
import io.maju.frosty.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface CheckoutOrderGateway {

    CheckoutOrder create(CheckoutOrder anCheckinOrder);

    void deleteById(CheckoutOrderID anId);

    Optional<CheckoutOrder> findById(CheckoutOrderID anId);

    CheckoutOrder update(CheckoutOrder anCheckinOrder);

    Pagination<CheckoutOrder> findAll(SearchQuery aQuery);

    List<CheckoutOrderID> existsByIds(Iterable<CheckoutOrderID> ids);
}
