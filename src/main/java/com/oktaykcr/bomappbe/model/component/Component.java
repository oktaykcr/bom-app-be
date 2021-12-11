package com.oktaykcr.bomappbe.model.component;

import com.oktaykcr.bomappbe.model.base.BaseModel;
import com.oktaykcr.bomappbe.model.inventory.Inventory;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Getter @Setter
public class Component extends BaseModel {

    private String mouserPartNumber;
    private String imageUrl;
    private String dataSheetUrl;
    private String description;
    private String manufacturerPartNumber;
    private String manufacturerName;
    private String supplierLink;
    private int quantityOnHand;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;

    public Component() {
    }
}
