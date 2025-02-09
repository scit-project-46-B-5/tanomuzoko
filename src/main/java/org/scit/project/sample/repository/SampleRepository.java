package org.scit.project.sample.repository;

import org.scit.project.sample.entity.SampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SampleRepository extends JpaRepository<SampleEntity, Long>{
    
}
