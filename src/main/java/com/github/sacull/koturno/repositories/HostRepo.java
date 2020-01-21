package com.github.sacull.koturno.repositories;

import com.github.sacull.koturno.entities.HGroup;
import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HostRepo extends JpaRepository<Host, Long> {

    List<Host> findAllByHostGroupAndOwner(HGroup group, User user);

    Host findByAddressAndOwner(String address, User user);

    Long countAllByHostGroup(HGroup group);

    List<Host> findAllByOwnerOrderByName(User user);

    List<Host> findAllByOwner(User user);
}
