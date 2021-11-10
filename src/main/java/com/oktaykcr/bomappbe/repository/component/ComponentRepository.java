package com.oktaykcr.bomappbe.repository.component;

import com.oktaykcr.bomappbe.model.component.Component;
import com.oktaykcr.bomappbe.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComponentRepository extends BaseRepository<Component> {
    Page<Component> findAllByInventoryUserUsername(String username, Pageable pageable);
    Long countComponentByInventoryUserUsername(String username);
    Optional<Component> findByPartNumber(String partNumber);
}
