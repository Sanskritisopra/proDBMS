package com.example.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/invoice")
public class InvoiceCorpController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Display form to add a new invoice
    @GetMapping("/add")
    public String showAddInvoiceForm(@RequestParam("caseId") int caseId, Model model) {
        String sql = "SELECT * FROM CorporateCase WHERE CorporateCaseID = ?";
        List<Map<String, Object>> corporateCases = jdbcTemplate.queryForList(sql, caseId);
        
        if (corporateCases.isEmpty()) {
            return "error";  // Handle error when CorporateCase is not found
        }

        model.addAttribute("corporateCase", corporateCases.get(0));
        return "addinvoCorp";  // Thymeleaf template to add the invoice
    }

    // Handle form submission for adding a new invoice
    @PostMapping("/save")
    public String saveInvoice(@RequestParam int corporateCaseID,
                              @RequestParam double amount,
                              @RequestParam String dueDate,
                              @RequestParam String status) {
        String query = "INSERT INTO InvoiceCorp (CorporateCaseID, InvoiceDate, Amount, DueDate, Status) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, corporateCaseID, LocalDate.now(), amount, dueDate, status);
        return "redirect:/invoice/list";
    }

    // Display list of invoices and cases without invoices
    @GetMapping("/list")
    public String listInvoices(Model model) {
        String invoiceQuery = "SELECT ic.InvoiceID, ic.Amount, ic.InvoiceDate, ic.Status, cc.CaseDesc " +
                              "FROM InvoiceCorp ic JOIN CorporateCase cc ON ic.CorporateCaseID = cc.CorporateCaseID";
        List<Map<String, Object>> invoices = jdbcTemplate.queryForList(invoiceQuery);

        String caseQuery = "SELECT cc.CorporateCaseID, cc.CaseDesc FROM CorporateCase cc " +
                           "LEFT JOIN InvoiceCorp ic ON cc.CorporateCaseID = ic.CorporateCaseID WHERE ic.InvoiceID IS NULL";
        List<Map<String, Object>> casesWithoutInvoices = jdbcTemplate.queryForList(caseQuery);

        model.addAttribute("invoices", invoices);
        model.addAttribute("casesWithoutInvoices", casesWithoutInvoices);
        return "invoicelistCorp";  // Thymeleaf template to display invoices
    }

    @PostMapping("/updateStatus")
public String updateInvoiceStatus(@RequestParam("invoiceID") int invoiceID, 
                                  @RequestParam("status") String status) {
    String sql = "UPDATE InvoiceCorp SET Status = ? WHERE InvoiceID = ?";
    jdbcTemplate.update(sql, status, invoiceID);
    return "redirect:/invoice/list";  // Redirect back to the invoice list
}
}
