package io.maju.frosty.checkin;

import io.maju.frosty.pagination.Pagination;
import io.maju.frosty.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface CheckinOrderGateway {

    CheckinOrder create(CheckinOrder anCheckinOrder);

    void deleteById(CheckinOrderID anId);

    Optional<CheckinOrder> findById(CheckinOrderID anId);

    CheckinOrder update(CheckinOrder anCheckinOrder);

    Pagination<CheckinOrder> findAll(SearchQuery aQuery);

    List<CheckinOrderID> existsByIds(Iterable<CheckinOrderID> ids);
}
