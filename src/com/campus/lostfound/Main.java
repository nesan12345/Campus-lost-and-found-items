package com.campus.lostfound;

import com.campus.lostfound.exception.ItemNotFoundException;
import com.campus.lostfound.exception.LostFoundException;
import com.campus.lostfound.exception.UnauthorizedClaimException;
import com.campus.lostfound.model.*;
import com.campus.lostfound.service.LostFoundService;
import com.campus.lostfound.service.LostFoundServiceImpl;
import com.campus.lostfound.util.ReportExporter;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * 🏛️ Campus Lost & Found Management System - Main Application
 * A practical, student-focused terminal application.
 * Demonstrates Unit 1: Flow Control, Variables, Operators, Loops, Console I/O.
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static LostFoundService service;

    public static void main(String[] args) {
        printBanner();
        service = new LostFoundServiceImpl();

        // Check if headless automated demo is triggered
        if (args.length > 0 && args[0].equalsIgnoreCase("--demo")) {
            runAutomatedQuickDemo();
            service.shutdown();
            return;
        }

        // Unit 1: while loop for interactive menu
        boolean exit = false;
        while (!exit) {
            printMenu();
            System.out.print("👉 Select an Option [1-8]: ");
            String choice = scanner.nextLine().trim();

            // Unit 1: switch statement & break
            switch (choice) {
                case "1":
                    handleReportLostItem();
                    break;
                case "2":
                    handleReportFoundItem();
                    break;
                case "3":
                    handleViewAllInventory();
                    break;
                case "4":
                    handleSearchAndClaim();
                    break;
                case "5":
                    handleStaffClaimVerification();
                    break;
                case "6":
                    handleExportRecoveryReport();
                    break;
                case "7":
                    runAutomatedQuickDemo();
                    break;
                case "8":
                    exit = true;
                    System.out.println("\n👋 Thank you for using Campus Lost & Found System. Stay safe!");
                    break;
                default:
                    System.out.println("❌ Invalid choice. Please enter a number between 1 and 8.\n");
                    break;
            }
        }

        service.shutdown();
    }

    private static void printBanner() {
        System.out.println("================================================================================");
        System.out.println(" 🏫 VELLORE INSTITUTE OF TECHNOLOGY - CAMPUS LOST & FOUND SYSTEM                ");
        System.out.println(" 📦 Student & Staff Belongings Recovery & Management Portal                     ");
        System.out.println("================================================================================");
        System.out.println(" ☕ Java Runtime    : " + System.getProperty("java.version") + " (" + System.getProperty("java.vm.name") + ")");
        System.out.println(" 💾 Persistence     : MySQL Database with JDBC Driver (External Config)");
        System.out.println(" 🧵 Multithreading  : Background Auto-Archive Daemon Active (Unit 3)");
        System.out.println("================================================================================\n");
    }

    private static void printMenu() {
        System.out.println("\n=========================== 📋 MAIN SYSTEM MENU ===========================");
        System.out.println(" 1️⃣  Report a LOST Item (Module 1: Student Outing / Loss Report)");
        System.out.println(" 2️⃣  Report a FOUND Item (Module 1: Campus Security / Staff Handover)");
        System.out.println(" 3️⃣  View Active Campus Inventory (All Lost & Found Listings)");
        System.out.println(" 4️⃣  Search Inventory & Submit Ownership Claim (Module 2: Matching Engine)");
        System.out.println(" 5️⃣  Staff Desk: Verify Claims & Hand Over Belongings (Module 2)");
        System.out.println(" 6️⃣  Generate & Export Recovery Report to File (Module 3: File I/O)");
        System.out.println(" 7️⃣  ⚡ Run Automated End-to-End Evaluation Simulation");
        System.out.println(" 8️⃣  Exit System");
        System.out.println("===========================================================================");
    }

    // ==========================================
    // MODULE 1: REPORT LOST ITEM
    // ==========================================
    private static void handleReportLostItem() {
        System.out.println("\n--- 🔍 REPORT A LOST ITEM ---");
        try {
            System.out.print("Item Title (e.g. Blue Titan Water Bottle): ");
            String title = scanner.nextLine().trim();

            System.out.print("Brief Description: ");
            String desc = scanner.nextLine().trim();

            ItemCategory category = selectCategoryInteractive();

            System.out.print("Location where you think you lost it: ");
            String loc = scanner.nextLine().trim();

            System.out.print("Your Contact Email: ");
            String email = scanner.nextLine().trim();

            System.out.print("Last seen exact spot / table: ");
            String lastSeen = scanner.nextLine().trim();

            System.out.print("Your Student Roll Number (e.g. 23BCE1045): ");
            String roll = scanner.nextLine().trim();

            System.out.print("Optional Reward (Rs.) [0 for none]: ");
            String rewardStr = scanner.nextLine().trim();
            double reward = 0.0;
            if (!rewardStr.isEmpty()) {
                reward = Double.parseDouble(rewardStr);
            }

            LostItem item = service.reportLostItem(title, desc, category, loc, LocalDate.now(),
                    email, lastSeen, reward, roll);

            System.out.println("\n✅ LOST ITEM REGISTERED SUCCESSFULLY!");
            System.out.println("🆔 Item Identifier : " + item.getItemId());
            System.out.println("📌 Current Status   : " + item.getStatus());
            System.out.println("ℹ️ You will be contacted when a matching found item is turned in.");
        } catch (LostFoundException e) {
            System.out.println("❌ Registration Failed: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid number format for reward amount.");
        }
    }

    // ==========================================
    // MODULE 1: REPORT FOUND ITEM
    // ==========================================
    private static void handleReportFoundItem() {
        System.out.println("\n--- 🎁 REPORT A FOUND ITEM ---");
        try {
            System.out.print("Item Title (e.g. Scientific Calculator Casio fx-991EX): ");
            String title = scanner.nextLine().trim();

            System.out.print("Brief Description: ");
            String desc = scanner.nextLine().trim();

            ItemCategory category = selectCategoryInteractive();

            System.out.print("Found Location (e.g. SJT Lab 310): ");
            String loc = scanner.nextLine().trim();

            System.out.print("Finder / Security Email: ");
            String email = scanner.nextLine().trim();

            System.out.print("Current Storage Location (e.g. SJT Ground Floor Security Desk): ");
            String storage = scanner.nextLine().trim();

            System.out.print("Handover Officer / Staff Name: ");
            String officer = scanner.nextLine().trim();

            System.out.print("Ownership Verification Question (e.g. What color is the cover sticker?): ");
            String question = scanner.nextLine().trim();

            FoundItem item = service.reportFoundItem(title, desc, category, loc, LocalDate.now(),
                    email, storage, officer, question);

            System.out.println("\n✅ FOUND ITEM LOGGED SUCCESSFULLY!");
            System.out.println("🆔 Item Identifier : " + item.getItemId());
            System.out.println("🏢 Safe Storage     : " + item.getStorageLocation());
            System.out.println("🔐 Secret Proof Q   : " + item.getVerificationQuestion());
        } catch (LostFoundException e) {
            System.out.println("❌ Registration Failed: " + e.getMessage());
        }
    }

    // ==========================================
    // INVENTORY VIEW
    // ==========================================
    private static void handleViewAllInventory() {
        System.out.println("\n--- 📦 CAMPUS INVENTORY LISTING ---");
        List<Item> items = service.getAllItems();
        if (items.isEmpty()) {
            System.out.println("ℹ️ Inventory is currently empty.");
            return;
        }

        System.out.println("Total Items in Inventory: " + items.size());
        System.out.println("--------------------------------------------------------------------------------");
        for (Item item : items) {
            System.out.println(item);
        }
        System.out.println("--------------------------------------------------------------------------------");
    }

    // ==========================================
    // MODULE 2: SEARCH & CLAIM
    // ==========================================
    private static void handleSearchAndClaim() {
        System.out.println("\n--- 🔍 SEARCH INVENTORY & SUBMIT CLAIM ---");
        System.out.println("Recent Search: [" + service.getRecentSearch() + "]");
        System.out.print("Enter search keyword (or type 'UNDO' to pop previous keyword): ");
        String keyword = scanner.nextLine().trim();

        if (keyword.equalsIgnoreCase("UNDO")) {
            String undone = service.undoLastSearch();
            System.out.println("↩️ Popped previous search: " + (undone != null ? undone : "None"));
            return;
        }

        List<Item> results = service.searchItems(keyword);
        System.out.println("\n🎯 Search Results for '" + keyword + "' (" + results.size() + " matches found):");
        if (results.isEmpty()) {
            System.out.println("ℹ️ No items matched your keyword.");
            return;
        }

        for (Item item : results) {
            System.out.println(" • " + item);
        }

        System.out.print("\nWould you like to claim an item from this list? (y/n): ");
        String ans = scanner.nextLine().trim();
        if (ans.equalsIgnoreCase("y")) {
            System.out.print("Enter the Item ID to claim (e.g. FND-101): ");
            String itemId = scanner.nextLine().trim();

            try {
                System.out.print("Your Full Name: ");
                String name = scanner.nextLine().trim();

                System.out.print("Your Student Roll Number: ");
                String roll = scanner.nextLine().trim();

                System.out.print("Your Phone Number: ");
                String phone = scanner.nextLine().trim();

                System.out.print("Answer the Ownership Verification Proof: ");
                String proof = scanner.nextLine().trim();

                ClaimRequest claim = service.submitClaim(itemId, name, roll, phone, proof);
                System.out.println("\n🎉 CLAIM SUBMITTED SUCCESSFULLY!");
                System.out.println("📝 Claim ID : " + claim.getClaimId());
                System.out.println("ℹ️ Status   : PENDING_VERIFICATION. Visit the campus security desk with your ID card.");
            } catch (ItemNotFoundException | UnauthorizedClaimException e) {
                System.out.println("❌ Claim Rejected: " + e.getMessage());
            }
        }
    }

    // ==========================================
    // MODULE 2: STAFF VERIFICATION
    // ==========================================
    private static void handleStaffClaimVerification() {
        System.out.println("\n--- 🛡️ STAFF DESK: VERIFY & RESOLVE CLAIMS ---");
        List<ClaimRequest> claims = service.getAllClaims();
        if (claims.isEmpty()) {
            System.out.println("ℹ️ No claims currently filed in the system.");
            return;
        }

        for (ClaimRequest c : claims) {
            System.out.println(" • " + c);
        }

        System.out.print("\nEnter Claim ID to resolve (e.g. CLM-501) [or press Enter to cancel]: ");
        String claimId = scanner.nextLine().trim();
        if (claimId.isEmpty()) return;

        System.out.print("Do you approve and hand over the item? (yes/no): ");
        boolean approve = scanner.nextLine().trim().equalsIgnoreCase("yes");

        try {
            boolean success = service.verifyAndResolveClaim(claimId, approve);
            if (success) {
                System.out.println("✅ Claim APPROVED! Item marked as CLAIMED and handed over.");
            } else {
                System.out.println("🚫 Claim REJECTED! Item status reverted to UNCLAIMED.");
            }
        } catch (LostFoundException e) {
            System.out.println("❌ Error resolving claim: " + e.getMessage());
        }
    }

    // ==========================================
    // MODULE 3: REPORT EXPORT
    // ==========================================
    private static void handleExportRecoveryReport() {
        System.out.println("\n--- 🖨️ EXPORT RECOVERY REPORT (File I/O Streams) ---");
        try {
            File report = service.generateSummaryReport("reports");
            System.out.println("✅ Report generated successfully via Character & Byte Streams!");
            System.out.println("📁 Saved to: " + report.getAbsolutePath());
            System.out.println("\n[Preview of Exported File]:\n");
            System.out.println(ReportExporter.readReportPreview(report));
        } catch (Exception e) {
            System.out.println("❌ Failed to export report: " + e.getMessage());
        }
    }

    // ==========================================
    // AUTOMATED EVALUATION SIMULATION
    // ==========================================
    public static void runAutomatedQuickDemo() {
        System.out.println("\n================================================================================");
        System.out.println(" ⚡ EXECUTING AUTOMATED CAMPUS LOST & FOUND EVALUATION SIMULATION                ");
        System.out.println("================================================================================");

        try {
            // Step 1: Report Lost Item
            System.out.println("\n[Step 1] 🎓 Student reports lost Wireless Earbuds...");
            LostItem lost = service.reportLostItem(
                    "Sony WF-1000XM4 Noise Canceling Earbuds",
                    "Black charging case with a red carabiner strap",
                    ItemCategory.ELECTRONICS,
                    "Anna Auditorium - Row J",
                    LocalDate.now(),
                    "karthik.23bce@vitstudent.ac.in",
                    "Seat J-12 during technical symposium",
                    500.0,
                    "23BCE1188"
            );
            System.out.println("✅ Lost Item registered: " + lost.getItemId() + " -> Status: " + lost.getStatus());

            // Step 2: Search Inventory with Stack
            System.out.println("\n[Step 2] 🔍 Searching inventory for 'Earbuds'...");
            List<Item> searchResults = service.searchItems("Earbuds");
            System.out.println("✅ Search returned " + searchResults.size() + " matches. Stack pushed: " + service.getRecentSearch());

            // Step 3: Report Found Item
            System.out.println("\n[Step 3] 🎁 Security Guard reports finding a Black Leather Wallet...");
            FoundItem found = service.reportFoundItem(
                    "WildHorn Brown Leather Wallet",
                    "Contains VIT ID Card and metro pass",
                    ItemCategory.ID_CARDS_WALLETS,
                    "Main Cafeteria",
                    LocalDate.now(),
                    "guard.gate1@vit.ac.in",
                    "Central Security Office SJT",
                    "Inspector Mohan",
                    "What is the student name on the ID card?"
            );
            System.out.println("✅ Found Item registered: " + found.getItemId() + " -> Storage: " + found.getStorageLocation());

            // Step 4: Submit Claim
            System.out.println("\n[Step 4] 📝 Rightful owner submits ownership claim...");
            ClaimRequest claim = service.submitClaim(
                    found.getItemId(),
                    "Aditya Verma",
                    "23BCE1240",
                    "9876543210",
                    "The ID card has the name Aditya Verma, Roll 23BCE1240."
            );
            System.out.println("✅ Claim submitted: " + claim.getClaimId() + " -> Status: PENDING_VERIFICATION");

            // Step 5: Staff Verifies Claim
            System.out.println("\n[Step 5] 🛡️ Staff verifies proof and hands over belongings...");
            service.verifyAndResolveClaim(claim.getClaimId(), true);
            System.out.println("✅ Claim resolved! Item " + found.getItemId() + " is now CLAIMED.");

            // Step 6: Export Report via I/O Streams
            System.out.println("\n[Step 6] 🖨️ Exporting recovery report via Character Streams (PrintWriter/FileWriter)...");
            File report = service.generateSummaryReport("reports");
            System.out.println("✅ Report exported to: " + report.getPath() + " (Size: " + report.length() + " bytes)");

            System.out.println("\n🎉 AUTOMATED EVALUATION SIMULATION COMPLETED WITH 100% SUCCESS!");
            System.out.println("================================================================================\n");
        } catch (Exception e) {
            System.err.println("❌ Simulation Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static ItemCategory selectCategoryInteractive() {
        System.out.println("Select Category:");
        ItemCategory[] categories = ItemCategory.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.printf(" %d. %s%n", i + 1, categories[i].getDisplayName());
        }
        System.out.print("Choice [1-" + categories.length + ", default 1]: ");
        String c = scanner.nextLine().trim();
        int idx = 0;
        try {
            if (!c.isEmpty()) idx = Integer.parseInt(c) - 1;
        } catch (Exception ignored) {}
        if (idx < 0 || idx >= categories.length) idx = 0;
        return categories[idx];
    }
}
