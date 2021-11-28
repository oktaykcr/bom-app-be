package com.oktaykcr.bomappbe.repository.bom;

import com.oktaykcr.bomappbe.model.bom.Bom;
import com.oktaykcr.bomappbe.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BomRepository extends BaseRepository<Bom> {
    Page<Bom> findAllByUserUsername(String username, Pageable pageable);
    List<Bom> findAllByUserUsername(String username);
    Long countBomByUserUsername(String username);
    List<Bom> findAllByUserUsernameAndTitleContainingIgnoreCase(String username, String title);
}
