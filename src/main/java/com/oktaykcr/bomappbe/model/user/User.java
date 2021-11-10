package com.oktaykcr.bomappbe.model.user;

import com.oktaykcr.bomappbe.model.base.BaseModel;
import com.oktaykcr.bomappbe.model.bom.Bom;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter @Setter
public class User extends BaseModel {

    private String username;

    private String email;

    private String password;

    private Boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name= "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy="user")
    private Set<Bom> bomSet = new HashSet<>();
}
