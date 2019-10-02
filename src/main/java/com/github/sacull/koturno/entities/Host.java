package com.github.sacull.koturno.entities;

import javax.persistence.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "hosts")
public class Host {

    @Id
    @GeneratedValue
    private Long id;

    private InetAddress destination;
    private String description;

    @OneToMany(mappedBy = "host")
    private List<Inaccessibility> inaccessibilities = new ArrayList<>();

    protected Host() {
    }

    public Host(InetAddress destination, String description, List<Inaccessibility> inaccessibilities) {
        this.destination = destination;
        this.description = description;
        this.inaccessibilities = inaccessibilities;
    }

    public Long getId() {
        return this.id;
    }

    public InetAddress getDestination() {
        return this.destination;
    }

    public void setDestination(InetAddress destination) {
        this.destination = destination;
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

    public String getHostname() {
        return this.destination.getHostName();
    }

    public String getAddress() {
        return (this.destination.getAddress()[0] & 0xFF) + "." +
                (this.destination.getAddress()[1] & 0xFF) + "." +
                (this.destination.getAddress()[2] & 0xFF) + "." +
                (this.destination.getAddress()[3] & 0xFF);
    }

    @Override
    public String toString() {
        return "Host{" +
                "id=" + this.id +
                ", hostname='" + this.getHostname() + '\'' +
                ", address='" + this.getAddress() + '\'' +
                ", description='" + this.description + '\'' +
                ", inaccessibilities=" + this.inaccessibilities +
                '}';
    }
}
