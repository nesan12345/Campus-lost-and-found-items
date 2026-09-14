package com.campus.lostfound.util;

import com.campus.lostfound.model.ClaimRequest;
import com.campus.lostfound.model.Item;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 🖨️ ReportExporter
 * Demonstrates Unit 4:
 * 1. Character-oriented Streams: FileWriter, BufferedWriter, PrintWriter, BufferedReader
 * 2. Byte-oriented Streams: FileOutputStream, ByteArrayOutputStream
 */
public class ReportExporter {

    public static File exportMonthlyRecoveryReport(List<Item> items, List<ClaimRequest> claims, String outputDir) throws IOException {
        File dir = new File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        File reportFile = new File(dir, "Campus_Recovery_Report_" + timestamp + ".txt");

        // Character-oriented streams (Unit 4)
        try (FileWriter fw = new FileWriter(reportFile);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter pw = new PrintWriter(bw)) {

            pw.println("================================================================================");
            pw.println("         🏛️ VELLORE INSTITUTE OF TECHNOLOGY - CAMPUS LOST & FOUND REPORT       ");
            pw.println("================================================================================");
            pw.println(" Generated On   : " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            pw.println(" Total Items    : " + items.size());
            pw.println(" Total Claims   : " + claims.size());
            pw.println("================================================================================\n");

            pw.println("📦 ACTIVE INVENTORY SUMMARY:");
            pw.println("--------------------------------------------------------------------------------");
            for (Item item : items) {
                pw.printf("[%s] %-25s | Category: %-15s | Status: %-12s%n",
                        item.getItemId(), item.getTitle(), item.getCategory().name(), item.getStatus().name());
                pw.println("   Details: " + item.getDetailsSummary());
                pw.println("--------------------------------------------------------------------------------");
            }

            pw.println("\n📝 VERIFIED CLAIMS & HANDOVERS:");
            pw.println("--------------------------------------------------------------------------------");
            for (ClaimRequest claim : claims) {
                pw.printf("[Claim: %s] Item: %s | Claimant: %s (%s) | Verified: %s%n",
                        claim.getClaimId(), claim.getItemId(), claim.getClaimantName(),
                        claim.getClaimantRollNo(), claim.isVerified() ? "YES ✅" : "PENDING ⏳");
            }

            pw.println("\n================================================================================");
            pw.println(" End of Official Campus Lost & Found Audit Report");
            pw.println("================================================================================");
            pw.flush();
        }

        return reportFile;
    }

    public static String readReportPreview(File reportFile) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (FileReader fr = new FileReader(reportFile);
             BufferedReader br = new BufferedReader(fr)) { // Unit 4 Character stream reader
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append(System.lineSeparator());
            }
        }
        return sb.toString();
    }
}
