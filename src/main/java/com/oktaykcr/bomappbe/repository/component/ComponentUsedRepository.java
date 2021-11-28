package com.oktaykcr.bomappbe.repository.component;

import com.oktaykcr.bomappbe.model.component.ComponentUsed;
import com.oktaykcr.bomappbe.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComponentUsedRepository extends BaseRepository<ComponentUsed> {
    Page<ComponentUsed> findAllByBomUserUsername(String username, Pageable pageable);
    Page<ComponentUsed> findAllByBomUserUsernameAndBomId(String username, String bomId, Pageable pageable);
    List<ComponentUsed> findAllByBomUserUsernameAndBomId(String username, String bomId);
    Long countComponentByBomUserUsername(String username);
}
