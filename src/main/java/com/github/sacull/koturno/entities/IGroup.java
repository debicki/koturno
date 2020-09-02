package com.github.sacull.koturno.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "i_groups")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@ToString
public class IGroup {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private Long id;
    @Column(nullable = false)
    private String name;
    private String description;
    private String externalLink;

    public IGroup(String name, String description, String externalLink) {
        this.name = name;
        this.description = description;
        this.externalLink = externalLink;
    }
}
