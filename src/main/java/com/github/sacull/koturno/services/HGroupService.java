package com.github.sacull.koturno.services;

import com.github.sacull.koturno.entities.HGroup;
import com.github.sacull.koturno.repositories.HGroupRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HGroupService {

    private HGroupRepo hGroupRepo;

    @Autowired
    public HGroupService(HGroupRepo hGroupRepo) {
        this.hGroupRepo = hGroupRepo;
    }

    public HGroup getGroupByName(String name) {
        return hGroupRepo.findByName(name);
    }

    public HGroup save(HGroup group) {
        return hGroupRepo.save(group);
    }

    public HGroup getGroupById(Long id) {
        return hGroupRepo.getOne(id);
    }

    public void delete(HGroup hGroup) {
        hGroupRepo.delete(hGroup);
    }

    public List<HGroup> getAllGroups() {
        return hGroupRepo.findAll();
    }
}
