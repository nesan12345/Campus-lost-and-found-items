package com.campus.lostfound.model;

import java.time.LocalDate;

/**
 * 🔍 LostItem
 * Demonstrates Unit 2: Inheritance (extends Item), 'super' keyword, Method Overriding.
 */
public class LostItem extends Item {
    private String lastSeenPlace;
    private double rewardOffered;
    private String studentRollNo;

    public LostItem(String itemId, String title, String description, ItemCategory category,
                    String location, LocalDate reportDate, String contactEmail,
                    String lastSeenPlace, double rewardOffered, String studentRollNo) {
        // Calling superclass constructor (Unit 2: super keyword)
        super(itemId, title, description, category, location, reportDate, contactEmail);
        this.lastSeenPlace = lastSeenPlace;
        this.rewardOffered = rewardOffered;
        this.studentRollNo = studentRollNo;
    }

    // Implementing Abstract Method from Item (Unit 2: Polymorphism)
    @Override
    public String getDetailsSummary() {
        return String.format("Type: LOST | Student Roll: %s | Last Seen: %s | Reward: Rs. %.2f | Contact: %s",
                studentRollNo, lastSeenPlace, rewardOffered, contactEmail);
    }

    public String getLastSeenPlace() {
        return lastSeenPlace;
    }

    public void setLastSeenPlace(String lastSeenPlace) {
        this.lastSeenPlace = lastSeenPlace;
    }

    public double getRewardOffered() {
        return rewardOffered;
    }

    public void setRewardOffered(double rewardOffered) {
        this.rewardOffered = rewardOffered;
    }

    public String getStudentRollNo() {
        return studentRollNo;
    }

    public void setStudentRollNo(String studentRollNo) {
        this.studentRollNo = studentRollNo;
    }

    @Override
    public String toString() {
        return "[LOST ITEM] " + super.toString() + " | " + getDetailsSummary();
    }
}
