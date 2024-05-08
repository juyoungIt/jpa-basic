package com.howard.jpabasic.section8.section8_3;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Children {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private Parent parent;

    public void setParentRelation(Parent parent) {
        this.parent = parent;
        parent.getChildList().add(this);
    }

}
