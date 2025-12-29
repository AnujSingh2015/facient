package com.facient.cx.config.postgres.repo;

import com.facient.cx.entity.postgres.FacientOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacientOrderRepository extends JpaRepository<FacientOrder, Long> {

}