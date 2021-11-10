package com.oktaykcr.bomappbe.model.component;

import com.oktaykcr.bomappbe.model.base.BaseModel;
import com.oktaykcr.bomappbe.model.inventory.Inventory;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;
import java.util.Date;

@Entity
@Getter @Setter
public class Component extends BaseModel {

    private String partNumber;
    private String imageUrl;
    private String description;
    private String manufacturerName;
    private String supplierLink;
    private int quantityOnHand;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date updatedDate = Date.from(Instant.now());

    public Component() {
    }
}
