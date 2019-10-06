package com.github.sacull.koturno.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "hosts")
public class Host {

    @Id
    @GeneratedValue
    private Long id;

    private String hostname;
    private String IPv4;
    private LocalDateTime whenCreated;
    private String description;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "host")
    private List<Inaccessibility> inaccessibilities = new ArrayList<>();

    protected Host() {
    }

    public Host(String hostname, String IPv4, String description, List<Inaccessibility> inaccessibilities) {
        this.hostname = hostname;
        this.IPv4 = IPv4;
        this.whenCreated = LocalDateTime.now();
        this.description = description;
        this.inaccessibilities = inaccessibilities;
    }

    public Long getId() {
        return this.id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIPv4() {
        return IPv4;
    }

    public void setIPv4(String IPv4) {
        this.IPv4 = IPv4;
    }

    public LocalDateTime getWhenCreated() {
        return whenCreated;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Inaccessibility> getInaccessibilities() {
        return this.inaccessibilities;
    }

    public void addInaccessibility(Inaccessibility inaccessibility) {
        this.inaccessibilities.add(inaccessibility);
    }

    public String getDayWhenCreated() {
        return whenCreated.toLocalDate().toString();
    }

    @Override
    public String toString() {
        return "Host{" +
                "id=" + id +
                ", hostname='" + hostname + '\'' +
                ", IPv4='" + IPv4 + '\'' +
                ", whenCreated=" + whenCreated +
                ", description='" + description + '\'' +
                ", inaccessibilities=" + inaccessibilities +
                '}';
    }
}
