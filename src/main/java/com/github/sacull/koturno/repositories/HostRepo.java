package com.github.sacull.koturno.repositories;

import com.github.sacull.koturno.entities.HGroup;
import com.github.sacull.koturno.entities.Host;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HostRepo extends JpaRepository<Host, Long> {

    List<Host> findAllByHostGroup(HGroup group);

    Host findByAddress(String address);

    Long countAllByHostGroup(HGroup group);

    List<Host> findAllByOrderByName();

    List<Host> findAllBy();

    Long countAllByActiveTrue();

    Long countAllByActiveFalse();

    Long countAllBy();
}
