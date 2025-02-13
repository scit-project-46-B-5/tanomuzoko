package org.scit.project.mainpage.repository;

import org.scit.project.sample.entity.SampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainRepository extends JpaRepository<SampleEntity, Long> {

}
