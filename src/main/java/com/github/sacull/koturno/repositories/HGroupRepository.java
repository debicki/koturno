package com.github.sacull.koturno.repositories;

import com.github.sacull.koturno.entities.HGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Optional;

@Repository
@Transactional
public class HGroupRepository {

    @Autowired
    private EntityManager em;

    public HGroup getById(Long id) {
        return em.find(HGroup.class, id);
    }

    public void deleteById(Long id) {
        HGroup groupToDelete = getById(id);
        em.remove(groupToDelete);
    }

    public HGroup save(HGroup group) {
        if (group.getId() == null) {
            em.persist(group);
        } else {
            em.merge(group);
        }
        return group;
    }

    public HGroup getDefaultHostGroup() {
        TypedQuery<HGroup> getAllGroupsQuery =
                em.createQuery("SELECT g FROM HGroup g", HGroup.class);
        Optional<HGroup> defaultGroup = getAllGroupsQuery.getResultStream()
                .filter(g -> g.getDescription().equals("default"))
                .findFirst();
        return defaultGroup.orElseGet(() -> this.save(new HGroup("default", new ArrayList<>())));
    }
}
