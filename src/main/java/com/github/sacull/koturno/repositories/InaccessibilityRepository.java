package com.github.sacull.koturno.repositories;

import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.entities.Inaccessibility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class InaccessibilityRepository {

    @Autowired
    private EntityManager em;

    public Inaccessibility getById(Long id) {
        return em.find(Inaccessibility.class, id);
    }

    public void deleteById(Long id) {
        Inaccessibility inaccessibilityToDelete = getById(id);
        em.remove(inaccessibilityToDelete);
    }

    public Inaccessibility save(Inaccessibility inaccessibility) {
        if (inaccessibility.getId() == null) {
            em.persist(inaccessibility);
        } else {
            em.merge(inaccessibility);
        }
        return inaccessibility;
    }

    public List<Inaccessibility> getAllInaccessibilities() {
        TypedQuery<Inaccessibility> inaccessibilityQuery =
                em.createQuery("SELECT i FROM Inaccessibility i", Inaccessibility.class);
        return inaccessibilityQuery.getResultList();
    }

    public Inaccessibility getLastInaccessibility(Host host) {
        return this.getById(
                host.getInaccessibilities()
                        .get(host.getInaccessibilities().size() - 1)
                        .getId());
    }

    public Inaccessibility getLastHostInaccessibility(Host host) {
        if (host.getInaccessibilities().size() > 0) {
            return this.getById(
                    host.getInaccessibilities()
                            .get(host.getInaccessibilities().size() - 1)
                            .getId());
        } else {
            return null;
        }
    }
}
