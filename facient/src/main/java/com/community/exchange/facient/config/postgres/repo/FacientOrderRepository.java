package com.community.exchange.facient.config.postgres.repo;

import com.community.exchange.facient.entity.postgres.FacientOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacientOrderRepository extends JpaRepository<FacientOrder, Long> {

}