package com.campus.lostfound.service;

import com.campus.lostfound.exception.ItemNotFoundException;
import com.campus.lostfound.exception.LostFoundException;
import com.campus.lostfound.exception.UnauthorizedClaimException;
import com.campus.lostfound.model.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * 💼 LostFoundService Interface
 * Demonstrates Unit 2: Java Interfaces.
 */
public interface LostFoundService {

    // Module 1: Item Reporting (CRUD)
    LostItem reportLostItem(String title, String description, ItemCategory category,
                           String location, LocalDate date, String contactEmail,
                           String lastSeenPlace, double reward, String rollNo)
            throws LostFoundException;

    FoundItem reportFoundItem(String title, String description, ItemCategory category,
                             String location, LocalDate date, String contactEmail,
                             String storageLocation, String officer, String verificationQ)
            throws LostFoundException;

    List<Item> getAllItems();

    Item getItemById(String itemId) throws ItemNotFoundException;

    boolean removeItem(String itemId) throws ItemNotFoundException;

    // Module 2: Claim & Matching Engine
    List<Item> searchItems(String keyword);

    ClaimRequest submitClaim(String itemId, String claimantName, String rollNo,
                            String phone, String proofAnswer)
            throws ItemNotFoundException, UnauthorizedClaimException;

    boolean verifyAndResolveClaim(String claimId, boolean approve)
            throws LostFoundException;

    List<ClaimRequest> getClaimsForItem(String itemId);

    List<ClaimRequest> getAllClaims();

    String undoLastSearch();

    String getRecentSearch();

    // Module 3: Reporting & Export
    File generateSummaryReport(String outputDir) throws IOException;

    int getArchivedItemsCount();

    void shutdown();
}
