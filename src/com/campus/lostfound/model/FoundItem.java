package com.campus.lostfound.model;

import java.time.LocalDate;

/**
 * 🎁 FoundItem
 * Demonstrates Unit 2: Inheritance (extends Item), 'super' keyword, Method Overriding.
 */
public class FoundItem extends Item {
    private String storageLocation; // e.g. "Security Desk Main Gate", "Hostel Warden Office"
    private String handoverOfficer;
    private String verificationQuestion; // e.g. "What is the laptop wallpaper or color of pouch?"

    public FoundItem(String itemId, String title, String description, ItemCategory category,
                     String location, LocalDate reportDate, String contactEmail,
                     String storageLocation, String handoverOfficer, String verificationQuestion) {
        super(itemId, title, description, category, location, reportDate, contactEmail);
        this.storageLocation = storageLocation;
        this.handoverOfficer = handoverOfficer;
        this.verificationQuestion = verificationQuestion;
    }

    @Override
    public String getDetailsSummary() {
        return String.format("Type: FOUND | Held At: %s | Officer: %s | Security Q: %s",
                storageLocation, handoverOfficer, verificationQuestion);
    }

    public String getStorageLocation() {
        return storageLocation;
    }

    public void setStorageLocation(String storageLocation) {
        this.storageLocation = storageLocation;
    }

    public String getHandoverOfficer() {
        return handoverOfficer;
    }

    public void setHandoverOfficer(String handoverOfficer) {
        this.handoverOfficer = handoverOfficer;
    }

    public String getVerificationQuestion() {
        return verificationQuestion;
    }

    public void setVerificationQuestion(String verificationQuestion) {
        this.verificationQuestion = verificationQuestion;
    }

    @Override
    public String toString() {
        return "[FOUND ITEM] " + super.toString() + " | " + getDetailsSummary();
    }
}
