package com.thoughtworks.fabric.dto;

public class RedeemCommercialPaperRequest {
    private String issuer;
    private String paperNumber;
    private String redeemingOwner;

    public RedeemCommercialPaperRequest() {
    }

    public RedeemCommercialPaperRequest(String issuer, String paperNumber, String redeemingOwner) {
        this.issuer = issuer;
        this.paperNumber = paperNumber;
        this.redeemingOwner = redeemingOwner;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getPaperNumber() {
        return paperNumber;
    }

    public void setPaperNumber(String paperNumber) {
        this.paperNumber = paperNumber;
    }

    public String getRedeemingOwner() {
        return redeemingOwner;
    }

    public void setRedeemingOwner(String redeemingOwner) {
        this.redeemingOwner = redeemingOwner;
    }
}
