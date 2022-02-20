package com.example.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(
    name = "examples",
    indexes = {
        @Index(name = "index_examples_deleted", columnList = "deleted")
    }
)
public class Example {

    @Id
    @GeneratedValue(generator = "r11-generator")
    @GenericGenerator(name = "r11-generator", strategy = "com.example.helper.IdGenerator")
    private String id;

    @Column(name = "value")
    private String value;

    @Builder.Default
    @Column(name = "deleted")
    private boolean deleted = false;

}
