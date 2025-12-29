package com.community.exchange.facient.config.mysql.repo;

import com.community.exchange.facient.entity.mysql.FacientUser;
import com.community.exchange.facient.entity.mysql.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacientUserRepository extends JpaRepository<User, Long> {

}
