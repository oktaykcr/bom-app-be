package com.oktaykcr.bomappbe.model.bom;

import com.oktaykcr.bomappbe.model.base.BaseModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.Instant;
import java.util.Date;

@Entity
@Data
public class Bom extends BaseModel {

    private String title;

    private String description;

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
