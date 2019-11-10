package com.github.sacull.koturno.repositories;

import com.github.sacull.koturno.entities.Host;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HostRepository extends JpaRepository<Host, Long> {

    List<Host> findAllByOrderByName();
}
