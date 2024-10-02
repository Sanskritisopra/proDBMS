package com.example.demo.Controller;

import com.example.demo.model.AppointmentCorp;
import com.example.demo.model.Client;
import com.example.demo.model.CorporateCase;
import com.example.demo.model.Lawyer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/appointment")
public class AppointCorpController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/add")
    public String showAppointmentForm(Model model) {
        // Fetch Corporate Case IDs
        List<Integer> corporateCaseIDs = jdbcTemplate.queryForList("SELECT CorporateCaseID FROM CorporateCase", Integer.class);
        model.addAttribute("corporateCaseIDs", corporateCaseIDs);
    
        // Fetch Lawyer IDs
        List<Integer> lawyerIDs = jdbcTemplate.queryForList("SELECT LawyerID FROM Lawyer", Integer.class);
        model.addAttribute("lawyerIDs", lawyerIDs);
    
        // Fetch Client IDs
        List<Integer> clientIDs = jdbcTemplate.queryForList("SELECT ClientID FROM Client", Integer.class);
        model.addAttribute("clientIDs", clientIDs);
        
        model.addAttribute("appointmentCorp", new AppointmentCorp());
        return "appointmentCorp"; // Thymeleaf template
    }
    

    @PostMapping("/save")
    public String saveAppointment(
        @RequestParam("corporateCaseID") Integer corporateCaseID,
        @RequestParam("lawyerID") Integer lawyerID,
        @RequestParam("clientID") Integer clientID,
        @RequestParam("appointmentDate") String appointmentDate,
        @RequestParam("appointmentTime") String appointmentTime,
        @RequestParam("location") String location,
        Model model) {

        // Check if CorporateCase, Lawyer, and Client exist
        if (!exists("CorporateCase", "CorporateCaseID", corporateCaseID) || 
            !exists("Lawyer", "LawyerID", lawyerID) || 
            !exists("Client", "ClientID", clientID)) {
            model.addAttribute("error", "Invalid case, lawyer, or client ID.");
            return "appointmentCorp";
        }

        // Create AppointmentCorp instance
        AppointmentCorp appointment = new AppointmentCorp();
        appointment.setAppointmentDate(LocalDate.parse(appointmentDate));
        appointment.setAppointmentTime(LocalTime.parse(appointmentTime));
        appointment.setLocation(location);

        // Fetch and set the related entities
        CorporateCase corporateCase = new CorporateCase();
        corporateCase.setCorporateCaseID(corporateCaseID); // Ensure you have this method

        Lawyer lawyer = new Lawyer();
        lawyer.setLawyerID(lawyerID); // Ensure you have this method

        Client client = new Client();
        client.setClientId(clientID); // Ensure you have this method

        appointment.setCorporateCase(corporateCase);
        appointment.setLawyer(lawyer);
        appointment.setClient(client);

        // Save appointment
        String sql = "INSERT INTO AppointmentCorp (CorporateCaseID, LawyerID, ClientID, AppointmentDate, AppointmentTime, Location) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, corporateCaseID, lawyerID, clientID, 
                            appointment.getAppointmentDate(), appointment.getAppointmentTime(), location);

        return "redirect:/appointment/all"; // Adjust redirection as needed
    }

    private boolean exists(String table, String column, Integer id) {
        String sql = "SELECT COUNT(*) as count FROM " + table + " WHERE " + column + " = ?";
        
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql, id);
        
        if (result.isEmpty()) {
            return false;
        }
        
        Long count = (Long) result.get(0).get("count"); // Changed to Long
        return count != null && count > 0; // No need to cast to Integer
    }

    @GetMapping("/all")
    public String listAppointments(Model model) {
        List<AppointmentCorp> appointments = jdbcTemplate.query(
            "SELECT * FROM AppointmentCorp", new RowMapper<AppointmentCorp>() {
                @Override
                public AppointmentCorp mapRow(ResultSet rs, int rowNum) throws SQLException {
                    AppointmentCorp appointment = new AppointmentCorp();
                    appointment.setAppointmentID(rs.getInt("AppointmentID"));
    
                    // Fetch related entities using their IDs
                    CorporateCase corporateCase = new CorporateCase();
                    corporateCase.setCorporateCaseID(rs.getInt("CorporateCaseID"));
                    appointment.setCorporateCase(corporateCase);
    
                    Lawyer lawyer = new Lawyer();
                    lawyer.setLawyerID(rs.getInt("LawyerID"));
                    appointment.setLawyer(lawyer);
    
                    Client client = new Client();
                    client.setClientId(rs.getInt("ClientID")); // Ensure correct method name for clientID
                    appointment.setClient(client);
    
                    // Set date and time
                    appointment.setAppointmentDate(rs.getDate("AppointmentDate") != null 
                            ? rs.getDate("AppointmentDate").toLocalDate() : null);
                    appointment.setAppointmentTime(rs.getTime("AppointmentTime") != null 
                            ? rs.getTime("AppointmentTime").toLocalTime() : null);
                    
                    appointment.setLocation(rs.getString("Location"));
                    return appointment;
                }
            });
    
        model.addAttribute("appointments", appointments);
        return "appointcorp"; // Thymeleaf template for listing
    }
    

}
