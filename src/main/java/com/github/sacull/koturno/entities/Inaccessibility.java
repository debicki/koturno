package com.github.sacull.koturno.entities;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inaccessibilities")
public class Inaccessibility {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Host host;

    private LocalDateTime start;
    private LocalDateTime end;
    private Boolean active;
    private String description;

    protected Inaccessibility() {
    }

    public Inaccessibility(Host host, String description) {
        this.host = host;
        this.start = LocalDateTime.now();
        this.end = LocalDateTime.now();
        this.active = true;
        this.description = description;
    }

    public Long getId() {
        return this.id;
    }

    public Host getHost() {
        return this.host;
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public LocalDateTime getStart() {
        return this.start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return this.end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDayOfBegin() {
        return start.toLocalDate().toString();
    }

    public String getDayOfEnd() {
        return end.toLocalDate().toString();
    }

    public String getHourOfBegin() {
        return start.toLocalTime().toString().substring(0,8);
    }

    public String getHourOfEnd() {
        return end.toLocalTime().toString().substring(0,8);
    }

    @Override
    public String toString() {
        return "Inaccessibility{" +
                "id=" + this.id +
                ", hostId=" + this.host +
                ", start=" + this.start +
                ", end=" + this.end +
                ", description='" + this.description + '\'' +
                '}';
    }
}
