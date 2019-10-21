package com.github.sacull.koturno.entities;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "h_groups")
public class HGroup {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hostGroup")
    private List<Host> hosts = new ArrayList<>();

    protected HGroup() {
    }

    public HGroup(String name, String description, List<Host> hosts) {
        this.name = name;
        this.description = description;
        this.hosts = hosts;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Host> getHosts() {
        return hosts;
    }

    public void addHost(Host host) {
        this.hosts.add(host);
    }

    @Override
    public String toString() {
        return "HGroup{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", hosts=" + hosts +
                '}';
    }
}
