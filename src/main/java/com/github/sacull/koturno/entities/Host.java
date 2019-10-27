package com.github.sacull.koturno.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "hosts")
public class Host implements Comparable<Host>{

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String address;
    private LocalDateTime whenCreated;
    private boolean active;
    private String description;

    @ManyToOne
    private HGroup hostGroup;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "host")
    private List<Inaccessibility> inaccessibilities = new ArrayList<>();

    protected Host() {
    }

    public Host(String name,
                String address,
                String description,
                HGroup hostGroup,
                List<Inaccessibility> inaccessibilities) {
        this.name = name;
        this.address = address;
        this.whenCreated = LocalDateTime.now();
        this.active = true;
        this.description = description;
        this.hostGroup = hostGroup;
        this.inaccessibilities = inaccessibilities;
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress (String address) {
        this.address = address;
    }

    public LocalDateTime getWhenCreated() {
        return whenCreated;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public HGroup getHostGroup() {
        return hostGroup;
    }

    public void setHostGroup(HGroup hostGroup) {
        this.hostGroup = hostGroup;
    }

    public List<Inaccessibility> getInaccessibilities() {
        return this.inaccessibilities;
    }

    public void addInaccessibility(Inaccessibility inaccessibility) {
        this.inaccessibilities.add(inaccessibility);
    }

    public void clearInaccessibilities() {
        this.inaccessibilities = new ArrayList<>();
    }

    public String getDayWhenCreated() {
        return whenCreated.toLocalDate().toString();
    }

    @Override
    public String toString() {
        return "Host{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address ='" + address + '\'' +
                ", whenCreated=" + whenCreated +
                ", description='" + description + '\'' +
                ", inaccessibilities=" + inaccessibilities +
                '}';
    }

    @Override
    public int compareTo(Host o) {
        return this.getName().compareTo(o.getName());
    }

    public boolean compareAddress(Host h) {
        return this.getAddress().equalsIgnoreCase(h.getAddress());
    }
}
