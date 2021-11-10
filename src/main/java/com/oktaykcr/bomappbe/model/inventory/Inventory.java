package com.oktaykcr.bomappbe.model.inventory;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oktaykcr.bomappbe.model.base.BaseModel;
import com.oktaykcr.bomappbe.model.component.Component;
import com.oktaykcr.bomappbe.model.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@JsonIgnoreProperties(value = { "user", "components" })
public class Inventory extends BaseModel {

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy="inventory")
    private Set<Component> components = new HashSet<>();

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date updatedDate = Date.from(Instant.now());

    public Inventory() {
    }
}
