package com.thoughtworks.fabric.controller;

import com.thoughtworks.fabric.chaincode.CommercialPaperChaincode_org1;
import com.thoughtworks.fabric.chaincode.CommercialPaperChaincode_org2;
import com.thoughtworks.fabric.config.Config;
import com.thoughtworks.fabric.dto.BuyCommercialPaperRequest;
import com.thoughtworks.fabric.dto.CommercialPaper;
import com.thoughtworks.fabric.dto.RedeemCommercialPaperRequest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/paper")
public class CommercialPaperController {
    private Log logger = LogFactory.getLog(CommercialPaperController.class);

    @GetMapping(value = "/{paperNumber}")
    public ResponseEntity queryCommercialPaper(@PathVariable(value = "paperNumber") String paperNumber,
                                               @RequestParam(value = "org") String orgName) {
        CommercialPaper paper;

        switch (orgName) {
            case Config.ORG1:
                paper = CommercialPaperChaincode_org1.getBy(paperNumber);
                logger.info(String.format("Paper[%s]: %s", paperNumber, paper));
                return ResponseEntity.ok(Objects.requireNonNull(paper));
            case Config.ORG2:
                paper = CommercialPaperChaincode_org2.getBy(paperNumber);
                logger.info(String.format("Paper[%s]: %s", paperNumber, paper));
                return ResponseEntity.ok(Objects.requireNonNull(paper));
            default:
                return ResponseEntity.badRequest().body("You should choose which org to query by.");
        }
    }

    @PostMapping
    public ResponseEntity createCommercialPaper(@RequestBody CommercialPaper paper) {
        if (!paper.getIssuer().equals(Config.ORG1)) {
            return ResponseEntity.badRequest().body("Only " + Config.ORG1 + " can issue paper!");
        }

        CommercialPaperChaincode_org1.create(paper);

        logger.info(String.format("Paper[%s] created: %s", paper.getPaperNumber(), paper));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/buy")
    public ResponseEntity buyCommercialPaper(@RequestBody BuyCommercialPaperRequest req) {
        if (!req.getNewOwner().equals(Config.ORG2)) {
            return ResponseEntity.badRequest().body("Only " + Config.ORG2 + " can buy paper!");
        }

        CommercialPaperChaincode_org2.buy(req);

        logger.info(String.format("Paper[%s] bought: %s", req.getPaperNumber(), req));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/redeem")
    public ResponseEntity redeemCommercialPaper(@RequestBody RedeemCommercialPaperRequest req) {
        if (!req.getRedeemingOwner().equals(Config.ORG2)) {
            return ResponseEntity.badRequest().body("Only " + Config.ORG2 + " can redeem paper!");
        }

        CommercialPaperChaincode_org2.redeem(req);

        logger.info(String.format("Paper[%s] redeemed: %s", req.getPaperNumber(), req));
        return ResponseEntity.ok().build();
    }

}
