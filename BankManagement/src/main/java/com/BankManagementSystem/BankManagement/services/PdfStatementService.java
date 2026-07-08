package com.BankManagementSystem.BankManagement.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.BankManagementSystem.BankManagement.entity.Account;
import com.BankManagementSystem.BankManagement.entity.Transaction;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Service
public class PdfStatementService {

    public ByteArrayInputStream generateStatement(
            Account account,
            List<Transaction> transactions) {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            PdfWriter.getInstance(document, out);
            document.open();

            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

            Paragraph title = new Paragraph("BANK MANAGEMENT SYSTEM", titleFont);
            title.setAlignment(Paragraph.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Account Statement", headerFont));
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Account Number : " + account.getAccountNumber()));
            document.add(new Paragraph("Customer Name : "
                    + account.getCustomer().getFirstName() + account.getCustomer().getLastName()));
            document.add(new Paragraph("Current Balance : ₹"
                    + account.getBalance()));
            document.add(new Paragraph(" "));

            PdfPTable table = new PdfPTable(4);
            table.setWidthPercentage(100);

            table.addCell("Date");
            table.addCell("Type");
            table.addCell("Amount");
            table.addCell("Description");

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

            for (Transaction transaction : transactions) {

                table.addCell(transaction.getTransactionDate().format(formatter));
                table.addCell(transaction.getTransactionType().name());
                table.addCell(transaction.getAmount().toString());
                table.addCell(transaction.getDescription());
            }

            document.add(table);

            document.close();

        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}