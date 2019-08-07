package com.thoughtworks.fabric.dto;

import org.json.JSONObject;

public class CommercialPaper {
    // Enumerate commercial paper state values
    public final static String ISSUED = "ISSUED";
    public final static String TRADING = "TRADING";
    public final static String REDEEMED = "REDEEMED";

    private String state = "";
    //    private String key = "";
    private String paperNumber;
    private String issuer;
    private String issueDateTime;
    private int faceValue;
    private String maturityDateTime;
    private String owner;

//    public String getKey() {
//        return key;
//    }

//    public void setKey(String key) {
//        this.key = key;
//    }

    public String getState() {
        return state;
    }

    public CommercialPaper setState(String state) {
        this.state = state;
        return this;
    }

    public CommercialPaper setIssued() {
        this.state = CommercialPaper.ISSUED;
        return this;
    }

    public CommercialPaper setTrading() {
        this.state = CommercialPaper.TRADING;
        return this;
    }

    public CommercialPaper setRedeemed() {
        this.state = CommercialPaper.REDEEMED;
        return this;
    }

    public String getOwner() {
        return owner;
    }

    public CommercialPaper setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    public CommercialPaper() {

    }

//    public CommercialPaper setKey() {
//        this.key = makeKey(new String[]{this.paperNumber});
//        return this;
//    }

    public String getPaperNumber() {
        return paperNumber;
    }

    public CommercialPaper setPaperNumber(String paperNumber) {
        this.paperNumber = paperNumber;
        return this;
    }

    public String getIssuer() {
        return issuer;
    }

    public CommercialPaper setIssuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    public String getIssueDateTime() {
        return issueDateTime;
    }

    public CommercialPaper setIssueDateTime(String issueDateTime) {
        this.issueDateTime = issueDateTime;
        return this;
    }

    public int getFaceValue() {
        return faceValue;
    }

    public CommercialPaper setFaceValue(int faceValue) {
        this.faceValue = faceValue;
        return this;
    }

    public String getMaturityDateTime() {
        return maturityDateTime;
    }

    public CommercialPaper setMaturityDateTime(String maturityDateTime) {
        this.maturityDateTime = maturityDateTime;
        return this;
    }

//    @Override
//    public String toString() {
//        return "Paper::" + this.key + "   " + this.getPaperNumber() + " " + getIssuer() + " " + getFaceValue();
//    }

    /**
     * Deserialize a state data to commercial paper
     *
     * @param {Buffer} data to form back into the object
     */
    public static CommercialPaper deserialize(String data) {
        JSONObject json = new JSONObject(data);

        String issuer = json.getString("issuer");
        String paperNumber = json.getString("paperNumber");
        String issueDateTime = json.getString("issueDateTime");
        String maturityDateTime = json.getString("maturityDateTime");
        String owner = json.getString("owner");
        int faceValue = json.getInt("faceValue");
        String state = json.getString("state");
        return createInstance(issuer, paperNumber, issueDateTime, maturityDateTime, faceValue, owner, state);
    }

//    public static byte[] serialize(CommercialPaper paper) {
//        return State.serialize(paper);
//    }

    /**
     * Factory method to create a commercial paper object
     */
    public static CommercialPaper createInstance(String issuer, String paperNumber, String issueDateTime,
                                                 String maturityDateTime, int faceValue, String owner, String state) {
        return new CommercialPaper().setIssuer(issuer).setPaperNumber(paperNumber).setMaturityDateTime(maturityDateTime)
                                    .setFaceValue(faceValue).setIssueDateTime(issueDateTime).setOwner(owner).setState(state);
    }

    public static String makeKey(String[] keyParts) {
        return String.join(":", keyParts);
    }
}
