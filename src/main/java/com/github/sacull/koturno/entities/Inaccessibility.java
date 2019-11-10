package com.github.sacull.koturno.entities;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "inaccessibilities")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
@ToString
public class Inaccessibility {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne
    private Host host;

    private LocalDateTime start;

    private LocalDateTime end;

    @Getter(AccessLevel.NONE)
    private Boolean active;

    @Getter(AccessLevel.NONE)
    private Boolean offlineStatus;

    private String description;

    @ManyToOne
    private IGroup inaccessibilityGroup;

    @Builder
    public Inaccessibility(Host host,
                           String description,
                           IGroup inaccessibilityGroup) {
        this.host = host;
        this.start = LocalDateTime.now();
        this.end = LocalDateTime.now();
        this.active = true;
        this.offlineStatus = false;
        this.description = description;
        this.inaccessibilityGroup = inaccessibilityGroup;
    }

    public Boolean isActive() {
        return active;
    }

    public Boolean isOfflineStatus() {
        return offlineStatus;
    }

    public String getDayOfBegin() {
        return start.toLocalDate().toString();
    }

    public String getDayOfEnd() {
        return end.toLocalDate().toString();
    }

    public String getHourOfBegin() {
        if (start.toLocalTime().toString().length() > 8) {
            return start.toLocalTime().toString().substring(0,8);
        } else {
            return start.toLocalTime().toString();
        }
    }

    public String getHourOfEnd() {
        if (end.toLocalTime().toString().length() > 8) {
            return end.toLocalTime().toString().substring(0,8);
        } else {
            return end.toLocalTime().toString();
        }
    }
}
