package com.github.sacull.koturno.repositories;

import com.github.sacull.koturno.entities.Host;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostRepository extends JpaRepository<Host, Long> {
}
