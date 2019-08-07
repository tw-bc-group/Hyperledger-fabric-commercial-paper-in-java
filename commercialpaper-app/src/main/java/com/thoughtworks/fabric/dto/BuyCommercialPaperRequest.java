package com.thoughtworks.fabric.dto;

public class BuyCommercialPaperRequest {
    private String issuer;
    private String paperNumber;
    private String currentOwner;
    private String newOwner;
    private int price;
    private String purchaseDateTime;

    public BuyCommercialPaperRequest() {
    }

    public BuyCommercialPaperRequest(String issuer, String paperNumber, String currentOwner, String newOwner, int price, String purchaseDateTime) {
        this.issuer = issuer;
        this.paperNumber = paperNumber;
        this.currentOwner = currentOwner;
        this.newOwner = newOwner;
        this.price = price;
        this.purchaseDateTime = purchaseDateTime;
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

    public String getCurrentOwner() {
        return currentOwner;
    }

    public void setCurrentOwner(String currentOwner) {
        this.currentOwner = currentOwner;
    }

    public String getNewOwner() {
        return newOwner;
    }

    public void setNewOwner(String newOwner) {
        this.newOwner = newOwner;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPurchaseDateTime() {
        return purchaseDateTime;
    }

    public void setPurchaseDateTime(String purchaseDateTime) {
        this.purchaseDateTime = purchaseDateTime;
    }
}
