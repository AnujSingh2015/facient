package com.facient.cx.controller;

import com.facient.cx.entity.request.SignupRequest;
import com.facient.cx.service.MultiDBService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/facient")
public class FacientController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FacientController.class);

    @Autowired
    MultiDBService multiDBService;

    @PostMapping("/user")
    public void addUser(@RequestBody SignupRequest request) {
        LOGGER.info("Name: {}",request.getUsername());
        multiDBService.saveUserData(request);
    }

    @PostMapping("/order")
    public void addOrder(@RequestBody String order) {
        LOGGER.info("Product Name: {}",order);
        multiDBService.saveOrderData(order);
    }
}
