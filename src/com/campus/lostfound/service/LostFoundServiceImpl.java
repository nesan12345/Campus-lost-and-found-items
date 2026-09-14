package com.campus.lostfound.service;

import com.campus.lostfound.dao.ItemDAO;
import com.campus.lostfound.dao.ItemDAOImpl;
import com.campus.lostfound.exception.ItemNotFoundException;
import com.campus.lostfound.exception.LostFoundException;
import com.campus.lostfound.exception.UnauthorizedClaimException;
import com.campus.lostfound.model.*;
import com.campus.lostfound.thread.AutoArchiveWorkerThread;
import com.campus.lostfound.util.ReportExporter;
import com.campus.lostfound.util.SearchHistoryStack;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * 🛠️ LostFoundServiceImpl
 * Core Business Logic Implementation integrating Units 1 through 5.
 */
public class LostFoundServiceImpl implements LostFoundService {

    private final ItemDAO itemDAO;
    private final SearchHistoryStack searchStack;
    private final AutoArchiveWorkerThread archiveWorker;
    private static int itemSequenceCounter = 100;
    private static int claimSequenceCounter = 500;

    public LostFoundServiceImpl() {
        this.itemDAO = new ItemDAOImpl();
        this.searchStack = new SearchHistoryStack();

        // Seed initial sample inventory items
        seedInitialCampusData();

        // Unit 3: Start background multi-threaded scanner daemon (runs every 10 seconds)
        this.archiveWorker = new AutoArchiveWorkerThread(itemDAO, 10000);
        this.archiveWorker.start();
    }

    private void seedInitialCampusData() {
        // Sample Found Items
        FoundItem fi1 = new FoundItem("FND-101", "Blue HP Scientific Calculator",
                "HP 10s+ scientific calculator left on desk", ItemCategory.ELECTRONICS,
                "SJT 402 Classroom", LocalDate.now().minusDays(2), "desk.sjt@vit.ac.in",
                "Security Desk - SJT Ground Floor", "Officer Raman", "What is written on the back sticker?");

        FoundItem fi2 = new FoundItem("FND-102", "Titan Black Leather Wallet",
                "Black bifold wallet containing cash and metro card", ItemCategory.ID_CARDS_WALLETS,
                "Foodys Food Court", LocalDate.now().minusDays(1), "security.main@vit.ac.in",
                "Main Gate Central Security Room", "Officer Ramesh", "Name the bank card inside");

        // Sample Lost Item
        LostItem li1 = new LostItem("LST-201", "MacBook Pro M2 Silver (14-inch)",
                "Space grey laptop with stickers of GitHub and Docker", ItemCategory.ELECTRONICS,
                "Central Library 2nd Floor", LocalDate.now().minusDays(3), "rohan.23bce@vitstudent.ac.in",
                "Quiet Study Cubicle #14", 1500.0, "23BCE1090");

        itemDAO.saveItem(fi1);
        itemDAO.saveItem(fi2);
        itemDAO.saveItem(li1);
    }

    // ==========================================
    // MODULE 1: ITEM REPORTING & CRUD
    // ==========================================
    @Override
    public LostItem reportLostItem(String title, String description, ItemCategory category,
                                  String location, LocalDate date, String contactEmail,
                                  String lastSeenPlace, double reward, String rollNo)
            throws LostFoundException {

        // Unit 1: Flow Control validation
        if (title == null || title.trim().isEmpty()) {
            throw new LostFoundException("Item Title cannot be blank!", "ERR_INVALID_TITLE");
        }
        if (rollNo == null || rollNo.trim().isEmpty()) {
            throw new LostFoundException("Student Roll Number is required!", "ERR_INVALID_ROLL");
        }

        String id = "LST-" + (++itemSequenceCounter);
        LostItem item = new LostItem(id, title, description, category, location, date,
                contactEmail, lastSeenPlace, reward, rollNo);

        itemDAO.saveItem(item); // Unit 5: JDBC Persistence
        return item;
    }

    @Override
    public FoundItem reportFoundItem(String title, String description, ItemCategory category,
                                    String location, LocalDate date, String contactEmail,
                                    String storageLocation, String officer, String verificationQ)
            throws LostFoundException {

        if (title == null || title.trim().isEmpty()) {
            throw new LostFoundException("Item Title cannot be blank!", "ERR_INVALID_TITLE");
        }

        String id = "FND-" + (++itemSequenceCounter);
        FoundItem item = new FoundItem(id, title, description, category, location, date,
                contactEmail, storageLocation, officer, verificationQ);

        itemDAO.saveItem(item);
        return item;
    }

    @Override
    public List<Item> getAllItems() {
        return itemDAO.findAll(); // Unit 4: ArrayList
    }

    @Override
    public Item getItemById(String itemId) throws ItemNotFoundException {
        return itemDAO.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException(itemId));
    }

    @Override
    public boolean removeItem(String itemId) throws ItemNotFoundException {
        getItemById(itemId); // validates existence
        return itemDAO.deleteItem(itemId);
    }

    // ==========================================
    // MODULE 2: CLAIM & MATCHING ENGINE
    // ==========================================
    @Override
    public List<Item> searchItems(String keyword) {
        // Unit 4: Stack tracking recent searches
        searchStack.pushSearch(keyword);
        return itemDAO.searchByKeyword(keyword);
    }

    @Override
    public ClaimRequest submitClaim(String itemId, String claimantName, String rollNo,
                                   String phone, String proofAnswer)
            throws ItemNotFoundException, UnauthorizedClaimException {

        Item item = getItemById(itemId);

        // Unit 1: Flow control & Unit 3: Custom Exception
        if (item.getStatus() == ClaimStatus.CLAIMED) {
            throw new UnauthorizedClaimException("Item [" + itemId + "] is already CLAIMED and handed over!");
        }

        if (proofAnswer == null || proofAnswer.trim().isEmpty()) {
            throw new UnauthorizedClaimException("Ownership verification proof cannot be empty!");
        }

        String claimId = "CLM-" + (++claimSequenceCounter);
        ClaimRequest claim = new ClaimRequest(claimId, itemId, claimantName, rollNo, phone, proofAnswer);

        itemDAO.saveClaim(claim);
        itemDAO.updateStatus(itemId, ClaimStatus.PENDING_VERIFICATION);
        return claim;
    }

    @Override
    public boolean verifyAndResolveClaim(String claimId, boolean approve)
            throws LostFoundException {

        List<ClaimRequest> allClaims = itemDAO.findAllClaims();
        ClaimRequest target = null;
        for (ClaimRequest c : allClaims) {
            if (c.getClaimId().equalsIgnoreCase(claimId)) {
                target = c;
                break;
            }
        }

        if (target == null) {
            throw new LostFoundException("Claim ID " + claimId + " not found!", "ERR_CLAIM_NOT_FOUND");
        }

        if (approve) {
            target.setVerified(true);
            itemDAO.updateStatus(target.getItemId(), ClaimStatus.CLAIMED);
            return true;
        } else {
            itemDAO.updateStatus(target.getItemId(), ClaimStatus.UNCLAIMED);
            return false;
        }
    }

    @Override
    public List<ClaimRequest> getClaimsForItem(String itemId) {
        return itemDAO.findClaimsByItemId(itemId);
    }

    @Override
    public List<ClaimRequest> getAllClaims() {
        return itemDAO.findAllClaims();
    }

    @Override
    public String undoLastSearch() {
        return searchStack.popLastSearch();
    }

    @Override
    public String getRecentSearch() {
        return searchStack.peekRecentSearch();
    }

    // ==========================================
    // MODULE 3: REPORTING & FILE I/O EXPORT
    // ==========================================
    @Override
    public File generateSummaryReport(String outputDir) throws IOException {
        // Unit 4: Character & Byte streams file export
        List<Item> items = itemDAO.findAll();
        List<ClaimRequest> claims = itemDAO.findAllClaims();
        return ReportExporter.exportMonthlyRecoveryReport(items, claims, outputDir);
    }

    @Override
    public int getArchivedItemsCount() {
        return AutoArchiveWorkerThread.getArchivedCount();
    }

    @Override
    public void shutdown() {
        if (archiveWorker != null) {
            archiveWorker.stopWorker();
        }
    }
}
