package com.campus.lostfound.dao;

import com.campus.lostfound.model.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 💾 ItemDAOImpl
 * Demonstrates Unit 5:
 * 1. Layout of JDBC API: Connection, PreparedStatement, Statement, ResultSet
 * 2. Executing SQL Queries and Updates (CRUD)
 * Demonstrates Unit 4: Java Collections Framework (ArrayList, ConcurrentHashMap)
 */
public class ItemDAOImpl implements ItemDAO {

    private final DatabaseManager dbManager;
    private final Map<String, Item> memoryCache = new ConcurrentHashMap<>();
    private final List<ClaimRequest> claimsCache = new ArrayList<>();

    public ItemDAOImpl() {
        this.dbManager = DatabaseManager.getInstance();
    }

    @Override
    public boolean saveItem(Item item) {
        if (item == null) return false;
        memoryCache.put(item.getItemId(), item);

        if (dbManager.isConnected()) {
            Connection conn = dbManager.getConnection();
            if (item instanceof LostItem) {
                LostItem li = (LostItem) item;
                String sql = "INSERT INTO lost_items VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) { // JDBC PreparedStatement (Unit 5)
                    ps.setString(1, li.getItemId());
                    ps.setString(2, li.getTitle());
                    ps.setString(3, li.getDescription());
                    ps.setString(4, li.getCategory().name());
                    ps.setString(5, li.getLocation());
                    ps.setString(6, li.getReportDate().format(Item.DATE_FORMATTER));
                    ps.setString(7, li.getContactEmail());
                    ps.setString(8, li.getStatus().name());
                    ps.setString(9, li.getLastSeenPlace());
                    ps.setDouble(10, li.getRewardOffered());
                    ps.setString(11, li.getStudentRollNo());
                    return ps.executeUpdate() > 0;
                } catch (SQLException e) {
                    System.err.println("⚠️ [JDBC saveItem] " + e.getMessage());
                }
            } else if (item instanceof FoundItem) {
                FoundItem fi = (FoundItem) item;
                String sql = "INSERT INTO found_items VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, fi.getItemId());
                    ps.setString(2, fi.getTitle());
                    ps.setString(3, fi.getDescription());
                    ps.setString(4, fi.getCategory().name());
                    ps.setString(5, fi.getLocation());
                    ps.setString(6, fi.getReportDate().format(Item.DATE_FORMATTER));
                    ps.setString(7, fi.getContactEmail());
                    ps.setString(8, fi.getStatus().name());
                    ps.setString(9, fi.getStorageLocation());
                    ps.setString(10, fi.getHandoverOfficer());
                    ps.setString(11, fi.getVerificationQuestion());
                    return ps.executeUpdate() > 0;
                } catch (SQLException e) {
                    System.err.println("⚠️ [JDBC saveItem] " + e.getMessage());
                }
            }
        }
        return true;
    }

    @Override
    public boolean saveClaim(ClaimRequest claim) {
        if (claim == null) return false;
        claimsCache.add(claim);

        if (dbManager.isConnected()) {
            String sql = "INSERT INTO claim_requests VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = dbManager.getConnection().prepareStatement(sql)) {
                ps.setString(1, claim.getClaimId());
                ps.setString(2, claim.getItemId());
                ps.setString(3, claim.getClaimantName());
                ps.setString(4, claim.getClaimantRollNo());
                ps.setString(5, claim.getClaimantPhone());
                ps.setString(6, claim.getProofAnswer());
                ps.setString(7, claim.getClaimDate().format(ClaimRequest.FORMATTER));
                ps.setBoolean(8, claim.isVerified());
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                System.err.println("⚠️ [JDBC saveClaim] " + e.getMessage());
            }
        }
        return true;
    }

    @Override
    public Optional<Item> findById(String itemId) {
        if (itemId == null) return Optional.empty();

        if (dbManager.isConnected()) {
            // First check lost_items
            String lostSql = "SELECT * FROM lost_items WHERE item_id = ?";
            try (PreparedStatement ps = dbManager.getConnection().prepareStatement(lostSql)) {
                ps.setString(1, itemId);
                try (ResultSet rs = ps.executeQuery()) { // JDBC ResultSet (Unit 5)
                    if (rs.next()) {
                        LostItem li = new LostItem(
                                rs.getString("item_id"),
                                rs.getString("title"),
                                rs.getString("description"),
                                ItemCategory.valueOf(rs.getString("category")),
                                rs.getString("location"),
                                LocalDate.parse(rs.getString("report_date"), Item.DATE_FORMATTER),
                                rs.getString("contact_email"),
                                rs.getString("last_seen_place"),
                                rs.getDouble("reward_offered"),
                                rs.getString("student_roll_no")
                        );
                        li.setStatus(ClaimStatus.valueOf(rs.getString("status")));
                        memoryCache.put(itemId, li);
                        return Optional.of(li);
                    }
                }
            } catch (SQLException ignored) {}

            // Check found_items
            String foundSql = "SELECT * FROM found_items WHERE item_id = ?";
            try (PreparedStatement ps = dbManager.getConnection().prepareStatement(foundSql)) {
                ps.setString(1, itemId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        FoundItem fi = new FoundItem(
                                rs.getString("item_id"),
                                rs.getString("title"),
                                rs.getString("description"),
                                ItemCategory.valueOf(rs.getString("category")),
                                rs.getString("location"),
                                LocalDate.parse(rs.getString("report_date"), Item.DATE_FORMATTER),
                                rs.getString("contact_email"),
                                rs.getString("storage_location"),
                                rs.getString("handover_officer"),
                                rs.getString("verification_question")
                        );
                        fi.setStatus(ClaimStatus.valueOf(rs.getString("status")));
                        memoryCache.put(itemId, fi);
                        return Optional.of(fi);
                    }
                }
            } catch (SQLException ignored) {}
        }

        return Optional.ofNullable(memoryCache.get(itemId));
    }

    @Override
    public List<Item> findAll() {
        return new ArrayList<>(memoryCache.values()); // Unit 4: ArrayList
    }

    @Override
    public List<Item> findByStatus(ClaimStatus status) {
        List<Item> filtered = new ArrayList<>();
        for (Item item : memoryCache.values()) {
            if (item.getStatus() == status) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    @Override
    public List<Item> searchByKeyword(String keyword) {
        List<Item> results = new ArrayList<>();
        if (keyword == null) return results;
        String query = keyword.toLowerCase().trim();

        for (Item item : memoryCache.values()) {
            if (item.getTitle().toLowerCase().contains(query) ||
                item.getDescription().toLowerCase().contains(query) ||
                item.getLocation().toLowerCase().contains(query) ||
                item.getCategory().name().toLowerCase().contains(query)) {
                results.add(item);
            }
        }
        return results;
    }

    @Override
    public List<ClaimRequest> findClaimsByItemId(String itemId) {
        List<ClaimRequest> matches = new ArrayList<>();
        for (ClaimRequest cr : claimsCache) {
            if (cr.getItemId().equalsIgnoreCase(itemId)) {
                matches.add(cr);
            }
        }
        return matches;
    }

    @Override
    public List<ClaimRequest> findAllClaims() {
        return new ArrayList<>(claimsCache);
    }

    @Override
    public boolean updateStatus(String itemId, ClaimStatus newStatus) {
        Item item = memoryCache.get(itemId);
        if (item != null) {
            item.setStatus(newStatus);
            if (dbManager.isConnected()) {
                String sql1 = "UPDATE lost_items SET status = ? WHERE item_id = ?";
                String sql2 = "UPDATE found_items SET status = ? WHERE item_id = ?";
                try (PreparedStatement ps1 = dbManager.getConnection().prepareStatement(sql1);
                     PreparedStatement ps2 = dbManager.getConnection().prepareStatement(sql2)) {
                    ps1.setString(1, newStatus.name());
                    ps1.setString(2, itemId);
                    ps1.executeUpdate();

                    ps2.setString(1, newStatus.name());
                    ps2.setString(2, itemId);
                    ps2.executeUpdate();
                } catch (SQLException ignored) {}
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean verifyClaim(String claimId) {
        for (ClaimRequest cr : claimsCache) {
            if (cr.getClaimId().equalsIgnoreCase(claimId)) {
                cr.setVerified(true);
                updateStatus(cr.getItemId(), ClaimStatus.CLAIMED);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteItem(String itemId) {
        memoryCache.remove(itemId);
        if (dbManager.isConnected()) {
            String sql1 = "DELETE FROM lost_items WHERE item_id = ?";
            String sql2 = "DELETE FROM found_items WHERE item_id = ?";
            try (PreparedStatement ps1 = dbManager.getConnection().prepareStatement(sql1);
                 PreparedStatement ps2 = dbManager.getConnection().prepareStatement(sql2)) {
                ps1.setString(1, itemId);
                ps1.executeUpdate();
                ps2.setString(1, itemId);
                ps2.executeUpdate();
            } catch (SQLException ignored) {}
        }
        return true;
    }

    @Override
    public int countTotalItems() {
        return memoryCache.size();
    }
}
