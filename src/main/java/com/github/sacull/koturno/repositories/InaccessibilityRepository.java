package com.github.sacull.koturno.repositories;

import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.entities.Inaccessibility;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InaccessibilityRepository extends JpaRepository<Inaccessibility, Long> {

    Inaccessibility findByHostOrderByEndDesc(Host host);
}
