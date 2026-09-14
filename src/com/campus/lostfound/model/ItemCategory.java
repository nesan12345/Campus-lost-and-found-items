package com.campus.lostfound.model;

/**
 * 🏷️ ItemCategory Enum
 * Demonstrates Unit 2: Java enum Class with custom constructors and display methods.
 */
public enum ItemCategory {
    ELECTRONICS("📱 Electronics & Gadgets"),
    BOOKS_NOTES("📚 Books & Academic Notes"),
    ID_CARDS_WALLETS("🪪 ID Cards, Wallets & Documents"),
    KEYS("🔑 Room & Vehicle Keys"),
    CLOTHING_BAGS("🎒 Backpacks & Clothing"),
    ACCESSORIES("👓 Watches, Glasses & Jewelry"),
    SPORTS_EQUIPMENT("🏸 Sports & Lab Equipment"),
    OTHERS("📦 Other Miscellaneous Belongings");

    private final String displayName;

    ItemCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
