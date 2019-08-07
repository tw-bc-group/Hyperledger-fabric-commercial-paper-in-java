package com.thoughtworks.fabric.chaincode;

import com.thoughtworks.fabric.client.CAClient;
import com.thoughtworks.fabric.client.ChannelClient;
import com.thoughtworks.fabric.client.FabricClient;
import com.thoughtworks.fabric.config.Config;
import com.thoughtworks.fabric.dto.CommercialPaper;
import com.thoughtworks.fabric.user.UserContext;
import com.thoughtworks.fabric.util.Util;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.ChaincodeResponse;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.EventHub;
import org.hyperledger.fabric.sdk.Orderer;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.thoughtworks.fabric.config.Config.CHAINCODE_1_NAME;
import static java.nio.charset.StandardCharsets.UTF_8;

public class CommercialPaperChaincode_org1 {

    static final byte[] EXPECTED_EVENT_DATA = "!".getBytes(UTF_8);
    static final String EXPECTED_EVENT_NAME = "event";

    private static void invoke(String funcName, String[] arguments) {
        try {
            Util.cleanUp();
            String caUrl = Config.CA_ORG1_URL;
            CAClient caClient = new CAClient(caUrl, null);
            // Enroll Admin to Org1MSP
            UserContext adminUserContext = new UserContext();
            adminUserContext.setName(Config.ADMIN);
            adminUserContext.setAffiliation(Config.ORG1);
            adminUserContext.setMspId(Config.ORG1_MSP);
            caClient.setAdminUserContext(adminUserContext);
            adminUserContext = caClient.enrollAdminUser(Config.ADMIN, Config.ADMIN_PASSWORD);

            FabricClient fabClient = new FabricClient(adminUserContext);
            ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
            Channel channel = channelClient.getChannel();
            Peer peer = fabClient.getInstance().newPeer(Config.ORG1_PEER_0, Config.ORG1_PEER_0_URL);
            EventHub eventHub = fabClient.getInstance().newEventHub("eventhub01", "grpc://localhost:7053");
            Orderer orderer = fabClient.getInstance().newOrderer(Config.ORDERER_NAME, Config.ORDERER_URL);
            channel.addPeer(peer);
            channel.addEventHub(eventHub);
            channel.addOrderer(orderer);
            channel.initialize();

            TransactionProposalRequest request = fabClient.getInstance().newTransactionProposalRequest();
            ChaincodeID ccid = ChaincodeID.newBuilder().setName(Config.CHAINCODE_1_NAME).build();
            request.setChaincodeID(ccid);
            request.setFcn(funcName);
            request.setArgs(arguments);
            request.setProposalWaitTime(1000000);

            Map<String, byte[]> tm2 = new HashMap<>();
            tm2.put("HyperLedgerFabric", "TransactionProposalRequest:JavaSDK".getBytes(UTF_8));
            tm2.put("method", "TransactionProposalRequest".getBytes(UTF_8));
            tm2.put("result", ":)".getBytes(UTF_8));
            tm2.put(EXPECTED_EVENT_NAME, EXPECTED_EVENT_DATA);
            request.setTransientMap(tm2);
            Collection<ProposalResponse> responses = channelClient.sendTransactionProposal(request);
            for (ProposalResponse res : responses) {
                ChaincodeResponse.Status status = res.getStatus();
                Logger.getLogger(CommercialPaperChaincode_org1.class.getName()).log(Level.INFO,
                                                                                    "Invoked " + funcName + " on " + Config.CHAINCODE_1_NAME + ". Status - " + status);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static CommercialPaper getBy(String paperNumber) {
        try {
            Util.cleanUp();
            String caUrl = Config.CA_ORG1_URL;
            CAClient caClient = new CAClient(caUrl, null);

            // Enroll Admin to Org1MSP
            UserContext adminUserContext = new UserContext();
            adminUserContext.setName(Config.ADMIN);
            adminUserContext.setAffiliation(Config.ORG1);
            adminUserContext.setMspId(Config.ORG1_MSP);
            caClient.setAdminUserContext(adminUserContext);
            adminUserContext = caClient.enrollAdminUser(Config.ADMIN, Config.ADMIN_PASSWORD);

            FabricClient fabClient = new FabricClient(adminUserContext);
            ChannelClient channelClient = fabClient.createChannelClient(Config.CHANNEL_NAME);
            Channel channel = channelClient.getChannel();
            Peer peer = fabClient.getInstance().newPeer(Config.ORG1_PEER_0, Config.ORG1_PEER_0_URL);
            EventHub eventHub = fabClient.getInstance().newEventHub("eventhub01", "grpc://localhost:7053");
            Orderer orderer = fabClient.getInstance().newOrderer(Config.ORDERER_NAME, Config.ORDERER_URL);
            channel.addPeer(peer);
            channel.addEventHub(eventHub);
            channel.addOrderer(orderer);
            channel.initialize();

            Logger.getLogger(CommercialPaperChaincode_org1.class.getName()).log(Level.INFO, "Querying for commercial papers ...");
            Collection<ProposalResponse> responsesQuery = channelClient.queryByChainCode(CHAINCODE_1_NAME, "query", new String[]{paperNumber});
            CommercialPaper paper = null;
            for (ProposalResponse pres : responsesQuery) {
                String stringResponse = new String(pres.getChaincodeActionResponsePayload());
                String resMessage = pres.getMessage();
                paper = CommercialPaper.deserialize(resMessage);
                Logger.getLogger(CommercialPaperChaincode_org1.class.getName()).log(Level.INFO, stringResponse);
            }
            return paper;
        } catch (Exception e) {
            Logger.getLogger(CommercialPaperChaincode_org1.class.getName()).log(Level.SEVERE, e.getMessage());
            return null;
        }
    }

    public static void create(CommercialPaper paper) {
        invoke("issue", new String[]{paper.getIssuer(), paper.getPaperNumber(), paper.getIssueDateTime(), paper.getMaturityDateTime(), String.valueOf(paper.getFaceValue())});
    }

}
