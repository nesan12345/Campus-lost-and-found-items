package com.campus.lostfound.exception;

/**
 * 🔍 ItemNotFoundException
 * Demonstrates Unit 3: Subclass Custom Checked Exception.
 */
public class ItemNotFoundException extends LostFoundException {
    public ItemNotFoundException(String itemId) {
        super("Inventory item not found with identifier: " + itemId, "ERR_ITEM_NOT_FOUND");
    }
}
