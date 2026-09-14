package com.campus.lostfound.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 📝 ClaimRequest
 * Demonstrates Unit 2: Encapsulated Entity Model for Ownership Claims.
 */
public class ClaimRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final String claimId;
    private final String itemId;
    private final String claimantName;
    private final String claimantRollNo;
    private final String claimantPhone;
    private final String proofAnswer;
    private final LocalDateTime claimDate;
    private boolean verified;

    public ClaimRequest(String claimId, String itemId, String claimantName,
                        String claimantRollNo, String claimantPhone, String proofAnswer) {
        this.claimId = claimId;
        this.itemId = itemId;
        this.claimantName = claimantName;
        this.claimantRollNo = claimantRollNo;
        this.claimantPhone = claimantPhone;
        this.proofAnswer = proofAnswer;
        this.claimDate = LocalDateTime.now();
        this.verified = false;
    }

    public String getClaimId() { return claimId; }
    public String getItemId() { return itemId; }
    public String getClaimantName() { return claimantName; }
    public String getClaimantRollNo() { return claimantRollNo; }
    public String getClaimantPhone() { return claimantPhone; }
    public String getProofAnswer() { return proofAnswer; }
    public LocalDateTime getClaimDate() { return claimDate; }
    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }

    @Override
    public String toString() {
        return String.format("[Claim: %s] Item: %s | By: %s (%s) | Ph: %s | Verified: %s | Time: %s",
                claimId, itemId, claimantName, claimantRollNo, claimantPhone, verified ? "YES" : "NO",
                claimDate.format(FORMATTER));
    }
}
