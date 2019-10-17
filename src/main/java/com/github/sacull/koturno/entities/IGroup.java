package com.github.sacull.koturno.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "i_groups")
public class IGroup {

    @Id
    @GeneratedValue
    private Long id;

    private String description;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "inaccessibilityGroup")
    private List<Inaccessibility> inaccessibilities = new ArrayList<>();

    protected IGroup() {
    }

    public IGroup(String description, List<Inaccessibility> inaccessibilities) {
        this.description = description;
        this.inaccessibilities = inaccessibilities;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Inaccessibility> getInaccessibilities() {
        return inaccessibilities;
    }

    public void addInaccessibility(Inaccessibility host) {
        this.inaccessibilities.add(host);
    }

    @Override
    public String toString() {
        return "HGroup{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", inaccessibilities=" + inaccessibilities +
                '}';
    }
}
