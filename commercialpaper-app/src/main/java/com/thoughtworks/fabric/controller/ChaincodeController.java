package com.thoughtworks.fabric.controller;

import com.thoughtworks.fabric.chaincode.DeployInstantiateChaincode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chaincode")
public class ChaincodeController {
    private Log logger = LogFactory.getLog(ChaincodeController.class);

    @PostMapping
    public ResponseEntity deployInstantiateChaincode() {
        try {
            DeployInstantiateChaincode.call();
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
