package com.campus.lostfound.thread;

import com.campus.lostfound.dao.ItemDAO;
import com.campus.lostfound.model.ClaimStatus;
import com.campus.lostfound.model.Item;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

/**
 * ⏰ AutoArchiveWorkerThread
 * Demonstrates Unit 3:
 * 1. Thread Creation: 'extends Thread'
 * 2. Thread Life Cycle & Life Cycle Methods: start(), sleep(), interrupt(), isAlive()
 * 3. Java Synchronization: Synchronized block protecting shared collections
 */
public class AutoArchiveWorkerThread extends Thread {

    private final ItemDAO itemDAO;
    private final long intervalMs;
    private volatile boolean running = true;
    private static int archivedCount = 0;

    public AutoArchiveWorkerThread(ItemDAO itemDAO, long intervalMs) {
        super("AutoArchive-Worker");
        this.itemDAO = itemDAO;
        this.intervalMs = intervalMs;
        setDaemon(true); // Daemon thread runs in background
    }

    @Override
    public void run() {
        System.out.println("🚀 [" + getName() + "] Background Scanner Thread Started. State: " + getState());
        while (running && !isInterrupted()) {
            try {
                // Thread Life Cycle Method: sleep (TIMED_WAITING state)
                Thread.sleep(intervalMs);
                scanAndArchiveExpiredItems();
            } catch (InterruptedException e) {
                System.out.println("🛑 [" + getName() + "] Thread interrupted safely.");
                Thread.currentThread().interrupt();
                break;
            } catch (Exception e) {
                System.err.println("⚠️ Error in archive worker: " + e.getMessage());
            }
        }
        System.out.println("🏁 [" + getName() + "] Background Scanner Exited.");
    }

    /**
     * Java Synchronization (Unit 3): Synchronized method to inspect and transition items
     */
    private synchronized void scanAndArchiveExpiredItems() {
        LocalDate today = LocalDate.now();
        List<Item> items = itemDAO.findAll();

        for (Item item : items) {
            // If item has been unclaimed for more than 30 days, mark as EXPIRED_ARCHIVED
            if (item.getStatus() == ClaimStatus.UNCLAIMED) {
                int daysElapsed = Period.between(item.getReportDate(), today).getDays();
                if (daysElapsed >= 30) {
                    itemDAO.updateStatus(item.getItemId(), ClaimStatus.EXPIRED_ARCHIVED);
                    incrementArchivedCount();
                    System.out.println("\n📦 [AUTO-ARCHIVE DAEMON] Item [" + item.getItemId() +
                            " - " + item.getTitle() + "] exceeded 30 days. Auto-Archived!");
                }
            }
        }
    }

    // Java Synchronization method (Unit 3)
    private static synchronized void incrementArchivedCount() {
        archivedCount++;
    }

    public static synchronized int getArchivedCount() {
        return archivedCount;
    }

    public void stopWorker() {
        this.running = false;
        this.interrupt(); // Interrupt life cycle method
    }
}
