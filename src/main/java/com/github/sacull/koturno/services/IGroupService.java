package com.github.sacull.koturno.services;

import com.github.sacull.koturno.entities.IGroup;
import com.github.sacull.koturno.repositories.IGroupRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IGroupService {

    private IGroupRepo iGroupRepo;

    @Autowired
    public IGroupService(IGroupRepo iGroupRepo) {
        this.iGroupRepo = iGroupRepo;
    }

    public IGroup getGroup(String name) {
        return iGroupRepo.findByName(name);
    }
}
