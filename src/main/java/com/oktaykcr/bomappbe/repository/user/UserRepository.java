package com.oktaykcr.bomappbe.repository.user;

import com.oktaykcr.bomappbe.model.user.User;
import com.oktaykcr.bomappbe.repository.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User> {

    User findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

}
