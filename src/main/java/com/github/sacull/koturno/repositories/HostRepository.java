package com.github.sacull.koturno.repositories;

import com.github.sacull.koturno.entities.Host;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class HostRepository {

    @Autowired
    private EntityManager em;

    public Host getById(Long id) {
        return em.find(Host.class, id);
    }

    public void deleteById(Long id) {
        Host hostToDelete = getById(id);
        em.remove(hostToDelete);
    }

    public Host save(Host host) {
        if (host.getId() == null) {
            em.persist(host);
        } else {
            em.merge(host);
        }
        return host;
    }

    public List<Host> getAllHosts() {
        TypedQuery<Host> hostQuery = em.createQuery("SELECT h FROM Host h", Host.class);
        return hostQuery.getResultList();
    }


}
