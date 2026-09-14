package com.campus.lostfound.dao;

import com.campus.lostfound.model.ClaimRequest;
import com.campus.lostfound.model.ClaimStatus;
import com.campus.lostfound.model.Item;
import java.util.List;
import java.util.Optional;

/**
 * 🗄️ ItemDAO Interface
 * Demonstrates Unit 2: Java Interfaces.
 * Demonstrates Unit 5: CRUD operations (Create, Read, Update, Delete) specification.
 */
public interface ItemDAO {
    // Create (Unit 5 CRUD)
    boolean saveItem(Item item);

    boolean saveClaim(ClaimRequest claim);

    // Read (Unit 5 CRUD)
    Optional<Item> findById(String itemId);

    List<Item> findAll();

    List<Item> findByStatus(ClaimStatus status);

    List<Item> searchByKeyword(String keyword);

    List<ClaimRequest> findClaimsByItemId(String itemId);

    List<ClaimRequest> findAllClaims();

    // Update (Unit 5 CRUD)
    boolean updateStatus(String itemId, ClaimStatus newStatus);

    boolean verifyClaim(String claimId);

    // Delete (Unit 5 CRUD)
    boolean deleteItem(String itemId);

    int countTotalItems();
}
