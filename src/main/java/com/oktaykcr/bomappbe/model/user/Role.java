package com.oktaykcr.bomappbe.model.user;

import com.oktaykcr.bomappbe.model.base.BaseModel;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "roles")
@Data
public class Role extends BaseModel {

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RoleEnum name;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<User> users;

}
