package com.oktaykcr.bomappbe.repository.component;

import com.oktaykcr.bomappbe.model.component.ComponentUsed;
import com.oktaykcr.bomappbe.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ComponentUsedRepository extends BaseRepository<ComponentUsed> {
    Page<ComponentUsed> findAllByBomUserUsername(String username, Pageable pageable);
    Long countComponentByBomUserUsername(String username);
}
