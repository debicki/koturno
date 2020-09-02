package com.github.sacull.koturno.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "h_groups")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@ToString
public class HGroup {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String description;
    private String externalLink;

    public HGroup(String name, String description, String externalLink) {
        this.name = name;
        this.description = description;
        this.externalLink = externalLink;
    }
}
