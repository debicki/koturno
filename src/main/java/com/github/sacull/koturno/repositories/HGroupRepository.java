package com.github.sacull.koturno.repositories;

import com.github.sacull.koturno.entities.HGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
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

    public List<HGroup> getAllGroups() {
        TypedQuery<HGroup> getAllGroupsQuery = em.createQuery("SELECT h FROM HGroup h", HGroup.class);
        return getAllGroupsQuery.getResultList();
    }

    public HGroup getDefaultHostGroup() {
        TypedQuery<HGroup> getAllGroupsQuery =
                em.createQuery("SELECT g FROM HGroup g", HGroup.class);
        Optional<HGroup> defaultGroup = getAllGroupsQuery.getResultStream()
                .filter(g -> g.getName().equals("default"))
                .findFirst();
        return defaultGroup.orElseGet(() -> this.save(new HGroup("default", "")));
    }

    public HGroup getByName(String name) {
        List<HGroup> groupsList = this.getAllGroups();
        for (HGroup group : groupsList) {
            if (group.getName().equals(name)) {
                return group;
            }
        }
        return null;
    }
}
