package com.example.demo.Controller;

import com.example.demo.model.Client;
import com.example.demo.model.CorporateCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/corporateCase")
public class CorporateCaseController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("corporateCase", new CorporateCase());
        model.addAttribute("clientNotFound", false); // Flag for client not found warning
        return "corporateCase"; // Name of your Thymeleaf template file
    }

    @PostMapping("/register")
    public String registerCorporateCase(@ModelAttribute("corporateCase") CorporateCase corporateCase,
                                        @RequestParam Integer clientID, 
                                        Model model) {

        // Fetch client based on the provided clientID using PreparedStatementSetter
        String sql = "SELECT * FROM Client WHERE ClientID = ?";
        List<Client> clients = jdbcTemplate.query(sql, new PreparedStatementSetter() {
            @Override
            public void setValues(java.sql.PreparedStatement ps) throws SQLException {
                ps.setInt(1, clientID);
            }
        }, new RowMapper<Client>() {
            @Override
            public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
                Client client = new Client();
                client.setClientId(rs.getInt("ClientID"));
                // Only setting ClientID based on your request
                return client;
            }
        });

        // Check if the client exists
        if (clients.isEmpty()) {
            model.addAttribute("clientNotFound", true);
            return "corporateCase"; // Return to the registration form
        }

        // Set the client in the corporate case
        corporateCase.setClient(clients.get(0)); // Get the first (and ideally only) client

        // Save the corporate case
        saveCorporateCase(corporateCase); // Call method with only the CorporateCase object

        // Redirect to the list of corporate cases or another appropriate page
        return "redirect:/corporateCase/all"; // Adjust as needed
    }

    @GetMapping("/all")
    public String listCorporateCases(Model model) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(
            "SELECT c.CorporateCaseID, c.CaseDesc, c.StartDate, c.EndDate, c.CaseStatus, cl.ClientID, cl.fName " +
            "FROM CorporateCase c JOIN Client cl ON c.ClientID = cl.ClientID");
    
        // if (rows.isEmpty()) {
        //     System.out.println("No corporate cases found.");
        // } else {
        //     System.out.println("Corporate Cases retrieved: " + rows.size());
        // }
    
        // for (Map<String, Object> row : rows) {
        //     System.out.println("CorporateCaseID: " + row.get("CorporateCaseID"));
        //     System.out.println("CaseDesc: " + row.get("CaseDesc"));
        //     System.out.println("StartDate: " + row.get("StartDate"));
        //     System.out.println("EndDate: " + row.get("EndDate"));
        //     System.out.println("CaseStatus: " + row.get("CaseStatus"));
        //     System.out.println("ClientID: " + row.get("ClientID"));
        //     System.out.println("ClientFName: " + row.get("fName"));
        // }
    
        model.addAttribute("corporateCases", rows);
        return "corporateCaseList"; // This should match the Thymeleaf template name
    }
    


    private void saveCorporateCase(CorporateCase corporateCase) {
        String query = "INSERT INTO CorporateCase (CaseDesc, StartDate, EndDate, ClientID, CaseStatus) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(query, corporateCase.getCaseDesc(), corporateCase.getStartDate(), 
                            corporateCase.getEndDate(), corporateCase.getClient().getClientId(), // Get the correct ClientID
                            corporateCase.getCaseStatus());
    }

    

}
