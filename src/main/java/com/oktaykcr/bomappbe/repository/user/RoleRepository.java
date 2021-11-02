package com.oktaykcr.bomappbe.repository.user;

import com.oktaykcr.bomappbe.model.user.Role;
import com.oktaykcr.bomappbe.model.user.RoleEnum;
import com.oktaykcr.bomappbe.repository.base.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends BaseRepository<Role> {

    Optional<Role> findByName(RoleEnum name);
}
