package com.oktaykcr.bomappbe.model.component;

import com.oktaykcr.bomappbe.model.base.BaseModel;
import com.oktaykcr.bomappbe.model.bom.Bom;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class ComponentUsed extends BaseModel {

    private int quantity;
    private int cost;
    private int leadTime;

    @ManyToOne
    @JoinColumn(name = "bom_id")
    private Bom bom;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "component_id", referencedColumnName = "id")
    private Component component;

    @Transient
    private int extendedCost;

    public ComponentUsed() {
    }

    public int getExtendedCost() {
        return quantity * cost;
    }
}
