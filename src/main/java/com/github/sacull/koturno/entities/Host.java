package com.github.sacull.koturno.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table (name = "hosts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@ToString
public class Host implements Comparable<Host>{

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private Long id;

    private String name;

    @Column(unique = true, nullable = false)
    private String address;

    @Setter(AccessLevel.NONE)
    private LocalDateTime whenCreated;

    @Getter(AccessLevel.NONE)
    private Boolean active;

    private String description;

    @ManyToOne
    private HGroup hostGroup;

    @ManyToOne
    private User owner;

    @Builder
    public Host(String name,
                String address,
                String description,
                HGroup hostGroup,
                User owner) {
        this.name = name;
        this.address = address;
        this.whenCreated = LocalDateTime.now();
        this.active = true;
        this.description = description;
        this.hostGroup = hostGroup;
        this.owner = owner;
    }

    public String getDayWhenCreated() {
        return whenCreated.toLocalDate().toString();
    }

    public Boolean isActive() {
        return active;
    }

    @Override
    public int compareTo(Host o) {
        return this.getName().compareTo(o.getName());
    }

    public boolean compareAddress(Host h) {
        return this.getAddress().equalsIgnoreCase(h.getAddress());
    }
}
