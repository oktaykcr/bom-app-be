package com.oktaykcr.bomappbe.model.bom;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.oktaykcr.bomappbe.model.base.BaseModel;
import com.oktaykcr.bomappbe.model.component.ComponentUsed;
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
@JsonIgnoreProperties(value = {"user", "componentUsedSet"})
public class Bom extends BaseModel {

    private String title;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy="bom")
    private Set<ComponentUsed> componentUsedSet = new HashSet<>();

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date updatedDate = Date.from(Instant.now());

    public Bom() {
    }

    public Bom(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
