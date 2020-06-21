package com.github.sacull.koturno.services;

import com.github.sacull.koturno.entities.Host;
import com.github.sacull.koturno.entities.Inaccessibility;
import com.github.sacull.koturno.repositories.InaccessibilityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InaccessibilityService {

    private InaccessibilityRepo inaccessibilityRepo;

    @Autowired
    public InaccessibilityService(InaccessibilityRepo inaccessibilityRepo) {
        this.inaccessibilityRepo = inaccessibilityRepo;
    }

    public List<Inaccessibility> getAllInaccessibility() {
        return inaccessibilityRepo.findAll();
    }

    public Inaccessibility save(Inaccessibility inaccessibility) {
        return inaccessibilityRepo.save(inaccessibility);
    }

    public Inaccessibility getLastInaccessibilityByHost(Host host) {
        return inaccessibilityRepo.findByHostAndActiveIsTrueOrderByEndDesc(host);
    }

    public List<Inaccessibility> findAllByActiveIsTrueOrderByStartDesc() {
        return inaccessibilityRepo.findAllByActiveIsTrueOrderByStartDesc();
    }

    public List<Inaccessibility> findAllByActiveIsTrue() {
        return inaccessibilityRepo.findAllByActiveIsTrue();
    }

    public List<Inaccessibility> findAllByOrderByStartDesc() {
        return inaccessibilityRepo.findAllByOrderByStartDesc();
    }

    public List<Inaccessibility> findAllByHost(Host host) {
        return inaccessibilityRepo.findAllByHost(host);
    }

    public void delete(Inaccessibility inaccessibility) {
        inaccessibilityRepo.delete(inaccessibility);
    }

    public List<Inaccessibility> findAllByHostOrderByStartDesc(Host host) {
        return inaccessibilityRepo.findAllByHostOrderByStartDesc(host);
    }

    public Inaccessibility getInaccessibilityById(Long id) {
        return inaccessibilityRepo.getOne(id);
    }

    public void removeAll() {
        inaccessibilityRepo.deleteAll();
    }
}
