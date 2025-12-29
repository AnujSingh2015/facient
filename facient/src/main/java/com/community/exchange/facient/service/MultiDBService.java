package com.community.exchange.facient.service;

import com.community.exchange.facient.config.mysql.repo.FacientUserRepository;
import com.community.exchange.facient.config.postgres.repo.FacientOrderRepository;
import com.community.exchange.facient.entity.postgres.FacientOrder;
import com.community.exchange.facient.entity.request.SignupRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MultiDBService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);
    private final FacientUserRepository mysqlRepo;
    private final FacientOrderRepository postgresRepo;

    public MultiDBService(FacientUserRepository mysqlRepo, FacientOrderRepository postgresRepo) {
        this.mysqlRepo = mysqlRepo;
        this.postgresRepo = postgresRepo;
    }

    public void saveUserData(SignupRequest request) {
        LOGGER.info("Username: {}",request.getUsername());
    }

    public void saveOrderData(String order) {
        LOGGER.info("Product name: {}",order);
        postgresRepo.save(new FacientOrder(null, order));
    }
}
