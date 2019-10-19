package com.github.sacull.koturno.repositories;

import com.github.sacull.koturno.entities.IGroup;
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
public class IGroupRepository {

    @Autowired
    private EntityManager em;

    public IGroup getById(Long id) {
        return em.find(IGroup.class, id);
    }

    public void deleteById(Long id) {
        IGroup groupToDelete = getById(id);
        em.remove(groupToDelete);
    }

    public IGroup save(IGroup group) {
        if (group.getId() == null) {
            em.persist(group);
        } else {
            em.merge(group);
        }
        return group;
    }

    public List<IGroup> getAllGroups() {
        TypedQuery<IGroup> getAllGroupsQuery = em.createQuery("SELECT h FROM IGroup h", IGroup.class);
        return getAllGroupsQuery.getResultList();
    }

    public IGroup getDefaultInaccessibilityGroup() {
        TypedQuery<IGroup> getAllGroupsQuery =
                em.createQuery("SELECT g FROM IGroup g", IGroup.class);
        Optional<IGroup> defaultGroup = getAllGroupsQuery.getResultStream()
                .filter(g -> g.getName().equals("default"))
                .findFirst();
        return defaultGroup.orElseGet(() -> this.save(new IGroup("default", "", new ArrayList<>())));
    }
}
