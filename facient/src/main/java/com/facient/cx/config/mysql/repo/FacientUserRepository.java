package com.facient.cx.config.mysql.repo;

import com.facient.cx.entity.mysql.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacientUserRepository extends JpaRepository<User, Long> {

}
