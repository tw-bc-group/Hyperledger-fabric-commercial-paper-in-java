package com.thoughtworks.fabric;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.fabric.shim.ChaincodeBase;
import org.hyperledger.fabric.shim.ChaincodeStub;

import java.util.Arrays;
import java.util.List;

public class CommercialPaperChaincode extends ChaincodeBase {

    private Log logger = LogFactory.getLog(CommercialPaperChaincode.class);

    @Override
    public Response init(ChaincodeStub stub) {
        logger.info("Chaincode CommercialPaper: Initialize CommercialPaper chaincode!");
        return newSuccessResponse();
    }

    @Override
    public Response invoke(ChaincodeStub stub) {
        logger.info("Chaincode CommercialPaper: Invoke java CommercialPaper chaincode");

        try {
            String function = stub.getFunction();
            switch (function) {
                case "issue":
                    return issue(stub);
                case "buy":
                    return buy(stub);
                case "redeem":
                    return redeem(stub);
                case "query":
                    return query(stub);
                default:
                    return newErrorResponse("Chaincode CommercialPaper: Invalid invoke function name. Expecting one of: [\\\"issue\\\", \\\"buy\\\", \\\"redeem\\\"]");
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return newErrorResponse(e);
        }
    }

    private Response query(ChaincodeStub stub) {
        List<String> parameters = stub.getParameters();
        String paperNumber = parameters.get(0);

        String paperKey = State.makeKey(new String[]{paperNumber});

        String stringState = stub.getStringState(paperKey);

        logger.info("Paper " + paperNumber + " found: " + CommercialPaper.deserialize(stringState));
        return newSuccessResponse(stringState, stub.getCreator());
    }

    private Response redeem(ChaincodeStub stub) {

        List<String> parameters = stub.getParameters();
        String issuer = parameters.get(0);
        String paperNumber = parameters.get(1);
        String redeemingOwner = parameters.get(2);

        String paperKey = CommercialPaper.makeKey(new String[]{paperNumber});

        String stringState = stub.getStringState(paperKey);
        CommercialPaper paper = CommercialPaper.deserialize(stringState);

        // Check paper is not REDEEMED
        if (paper.isRedeemed()) {
            throw new RuntimeException("Paper " + issuer + paperNumber + " already redeemed");
        }

        // Verify that the redeemer owns the commercial paper before redeeming it
        if (paper.getOwner().equals(redeemingOwner)) {
            paper.setOwner(paper.getIssuer());
            paper.setRedeemed();
        } else {
            throw new RuntimeException("Redeeming owner does not own paper" + issuer + paperNumber);
        }

        stub.putStringState(paperKey, CommercialPaper.serialize(paper));
        logger.info("Paper " + paperNumber + " redeemed by " + redeemingOwner + "successfully!");
        return newSuccessResponse("Paper " + paperNumber + " redeemed by " + redeemingOwner + "successfully!");
    }

    private Response buy(ChaincodeStub stub) {

        List<String> parameters = stub.getParameters();
        String issuer = parameters.get(0);
        String paperNumber = parameters.get(1);
        String currentOwner = parameters.get(2);
        String newOwner = parameters.get(3);
        int price = Integer.parseInt(parameters.get(4));
        String purchaseDateTime = parameters.get(5);

        // Retrieve the current paper using key fields provided
        String paperKey = State.makeKey(new String[]{paperNumber});
        String stringState = stub.getStringState(paperKey);
        CommercialPaper paper = CommercialPaper.deserialize(stringState);

        // Validate current owner
        if (!paper.getOwner().equals(currentOwner)) {
            throw new RuntimeException("Paper " + issuer + paperNumber + " is not owned by " + currentOwner);
        }

        // First buy moves state from ISSUED to TRADING
        if (paper.isIssued()) {
            paper.setTrading();
        }

        // Check paper is not already REDEEMED
        if (paper.isTrading()) {
            paper.setOwner(newOwner);
        } else {
            throw new RuntimeException(
                "Paper " + issuer + paperNumber + " is not trading. Current state = " + paper.getState());
        }

        // Update the paper
        stub.putStringState(paperKey, CommercialPaper.serialize(paper));

        logger.info("Paper " + paperNumber + " had brought by " + newOwner + " successfully.");
        return newSuccessResponse("Paper " + paperNumber + " had brought by " + newOwner + " successfully.");
    }

    private Response issue(ChaincodeStub stub) {
        List<String> parameters = stub.getParameters();
        String issuer = parameters.get(0);
        String paperNumber = parameters.get(1);
        String issueDateTime = parameters.get(2);
        String maturityDateTime = parameters.get(3);
        int faceValue = Integer.parseInt(parameters.get(4));

        // create an instance of the paper
        CommercialPaper paper = CommercialPaper.createInstance(issuer, paperNumber, issueDateTime, maturityDateTime,
                                                               faceValue, issuer, "");

        // Smart contract, rather than paper, moves paper into ISSUED state
        paper.setIssued();

        // Newly issued paper is owned by the issuer
        paper.setOwner(issuer);


        String paperKey = State.makeKey(new String[]{paperNumber});

        stub.putStringState(paperKey, CommercialPaper.serialize(paper));

        logger.error("Creator is " + Arrays.toString(stub.getCreator()) + "!!!!!");
        logger.info("Paper(" + paper.getPaperNumber() + ") issued successful!");
        return newSuccessResponse("Paper(" + paper.getPaperNumber() + ") issued successful!");
    }

    public static void main(String[] args) {
        new CommercialPaperChaincode().start(args);
    }
}
