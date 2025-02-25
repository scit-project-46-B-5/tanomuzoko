package org.scit.project.reply.repository;

import org.scit.project.reply.entity.ReplyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyRepository extends JpaRepository<ReplyEntity, Long>{
    
}
