package com.campus.lostfound.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 📦 Item (Abstract Base Class)
 * Demonstrates Unit 2: Encapsulation, Abstract Class, Protected fields, 'this' keyword, 'final' fields.
 * Demonstrates Unit 4: Serializable interface for file stream support.
 */
public abstract class Item implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    protected final String itemId;      // 'final' keyword - immutable unique ID
    protected String title;
    protected String description;
    protected ItemCategory category;
    protected String location;
    protected LocalDate reportDate;
    protected String contactEmail;
    protected ClaimStatus status;

    public Item(String itemId, String title, String description, ItemCategory category,
                String location, LocalDate reportDate, String contactEmail) {
        this.itemId = itemId;
        this.title = title;
        this.description = description;
        this.category = category;
        this.location = location;
        this.reportDate = reportDate != null ? reportDate : LocalDate.now();
        this.contactEmail = contactEmail;
        this.status = ClaimStatus.UNCLAIMED;
    }

    // Abstract Method: Demonstrates Polymorphism & Abstraction (Unit 2)
    public abstract String getDetailsSummary();

    // Getters and Setters (Encapsulation)
    public String getItemId() {
        return itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemCategory getCategory() {
        return category;
    }

    public void setCategory(ItemCategory category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    // Polymorphic method overriding (Unit 2)
    @Override
    public String toString() {
        return String.format("[%s] %s | %s | Location: %s | Status: %s | Date: %s",
                itemId, title, category.getDisplayName(), location, status,
                reportDate.format(DATE_FORMATTER));
    }
}
