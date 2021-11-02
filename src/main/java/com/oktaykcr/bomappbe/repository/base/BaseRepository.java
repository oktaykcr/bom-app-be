package com.oktaykcr.bomappbe.repository.base;

import com.oktaykcr.bomappbe.model.base.BaseModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<T extends BaseModel> extends JpaRepository<T, String> {
    Page<T> findAll(Pageable pageable);

}
