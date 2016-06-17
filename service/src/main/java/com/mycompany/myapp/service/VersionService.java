package com.mycompany.myapp.service;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.myapp.domain.Version;
import com.mycompany.myapp.repository.VersionRepository;

/**
 * Service Implementation for managing Version.
 */
@Service
@Transactional
public class VersionService {

    private final Logger log = LoggerFactory.getLogger(VersionService.class);
    
    @Inject
    private VersionRepository versionRepository;
    
    /**
     * Save a version.
     * 
     * @param version the entity to save
     * @return the persisted entity
     */
    public Version save(Version version) {
        log.debug("Request to save Version : {}", version);
        Version result = versionRepository.save(version);
        return result;
    }

    /**
     *  Get all the versions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Version> findAll(Pageable pageable) {
        log.debug("Request to get all Versions");
        Page<Version> result = versionRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one version by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Version findOne(Long id) {
        log.debug("Request to get Version : {}", id);
        Version version = versionRepository.findOne(id);
        return version;
    }

    /**
     *  Delete the  version by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Version : {}", id);
        versionRepository.delete(id);
    }
}
