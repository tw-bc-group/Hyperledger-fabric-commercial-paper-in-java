package com.thoughtworks.fabric.controller;

import com.thoughtworks.fabric.network.CreateChannel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.thoughtworks.fabric.user.RegisterEnrollUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientController {
    private Log logger = LogFactory.getLog(ClientController.class);

    @PostMapping("/user")
    public ResponseEntity enrollUser() {
        try {
            RegisterEnrollUser.call();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/channel")
    public ResponseEntity createChannel() {
        try {
            CreateChannel.call();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
