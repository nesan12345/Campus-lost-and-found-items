package com.campus.lostfound.model;

/**
 * 🚦 ClaimStatus Enum
 * Demonstrates Unit 2: Java enum Class, tracking item recovery lifecycle.
 */
public enum ClaimStatus {
    UNCLAIMED("Open / Unclaimed"),
    PENDING_VERIFICATION("Claim Submitted - Awaiting Proof Verification"),
    CLAIMED("Verified & Handed Over to Owner"),
    EXPIRED_ARCHIVED("Unclaimed Exceeded 30 Days - Archived");

    private final String description;

    ClaimStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return this.name() + " (" + description + ")";
    }
}
