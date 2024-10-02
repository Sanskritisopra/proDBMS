package com.example.demo.Controller;

import java.security.Principal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.springframework.jdbc.core.RowMapper;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.dto.UserDto;
import com.example.demo.model.CorporateCase;
import com.example.demo.model.Lawyer;
import com.example.demo.model.Paralegal;
import com.example.demo.model.TaskCorp;
import com.example.demo.service.UserService;

@Controller
public class UserController {
    
    @Autowired
    private UserDetailsService userDetailsService;
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private UserService userService;
    
    @GetMapping("/")
    public String home() {
        return "frontpage";
    }
    
    @GetMapping("/registration")
    public String getRegistrationPage(@ModelAttribute("user") UserDto userDto) {
        return "register";
    }
    
    @PostMapping("/registration")
    public String saveUser(@ModelAttribute("user") UserDto userDto, Model model) {
        if (userService.emailExists(userDto.getEmail())) {
            model.addAttribute("message", "Email already exists!"); // Set an error message
            return "register"; // Return to registration page
        }
        userService.save(userDto);
        model.addAttribute("message", "Registered Successfully!");
        return "register";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/login-success")
    public String loginSuccess(Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        
        // Check roles and redirect accordingly
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            switch (authority.getAuthority()) {
                case "ROLE_ADMIN":
                    return "redirect:/clients";
                case "ROLE_CLIENT":
                    return "redirect:/client-page";
                case "ROLE_LAWYER":
                    return "redirect:/lawyer-page";
                case "ROLE_PARALEGAL":
                    return "redirect:/paralegal-page";
                default:
                    break; // No action needed for unmatched roles
            }
        }
        
        // Default redirect if no role matches
        return "redirect:/login"; 
    }
    
    @GetMapping("/user-page")
    public String userPage(Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        return "user";
    }
    
    @GetMapping("/admin-page")
    public String adminPage(Model model, Principal principal) {
        // UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        // model.addAttribute("user", userDetails);
        return "redirect:/clients";
    }

    @GetMapping("/client-page")
    public String clientPage(Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        model.addAttribute("user", userDetails);
        return "client"; // Assuming you have a client.html template
    }

    // @GetMapping("/lawyer-page")
    // public String lawyerPage(Model model, Principal principal) {
    //     UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    //     model.addAttribute("user", userDetails);
    //     return "lawyer"; // Assuming you have a lawyer.html template
    // }

    @GetMapping("/lawyer-page")
    public String lawyerPage(Model model, Principal principal) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
        String loggedInEmail = userDetails.getUsername(); // Assuming username is the email
    
        // Check if the user has the role of 'LAWYER'
        if (userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_LAWYER"))) {
            // Fetch lawyer details from the database
            List<Lawyer> lawyers = fetchLawyerByEmail(loggedInEmail);
    
            if (!lawyers.isEmpty()) {
                // Lawyer found, status is "Approved"
                Lawyer lawyer = lawyers.get(0); // Get the first lawyer
                model.addAttribute("status", "Approved");
    
                // Fetch assigned cases and tasks
                List<Map<String, Object>> casesAssigned = fetchAssignedCases(lawyer.getLawyerID());
                List<Map<String, Object>> tasksAssigned = fetchAssignedTasks(lawyer.getLawyerID());
    
                // Only display cases and tasks if they exist
                if (!casesAssigned.isEmpty() || !tasksAssigned.isEmpty()) {
                    model.addAttribute("casesAssigned", casesAssigned);
                    model.addAttribute("tasksAssigned", tasksAssigned);
                    model.addAttribute("assignedCasesCount", casesAssigned.size());
                    model.addAttribute("assignedTasksCount", tasksAssigned.size());
                } else {
                    model.addAttribute("noAssignments", "No cases or tasks assigned.");
                }
            } else {
                // Lawyer not found, status is "Not Approved"
                model.addAttribute("status", "Not Approved");
            }
        } else {
            // Handle case where user is not a lawyer
            model.addAttribute("error", "Access Denied: You are not authorized to view this page.");
        }
    
        return "lawyer"; // Thymeleaf template for the lawyer page
    }
    
    // Method to fetch lawyer details by email
    private List<Lawyer> fetchLawyerByEmail(String email) {
        String sql = "SELECT * FROM Lawyer WHERE email = ?";
        
        List<Lawyer> availableLawyers = jdbcTemplate.query(sql, new RowMapper<Lawyer>() {
            @Override
            public Lawyer mapRow(ResultSet rs, int rowNum) throws SQLException {
                Lawyer lawyer = new Lawyer();
                lawyer.setLawyerID(rs.getInt("LawyerID"));
                lawyer.setFname(rs.getString("FName")); // Ensure column names match your database
                lawyer.setMname(rs.getString("MName"));
                lawyer.setLname(rs.getString("LName"));
                lawyer.setDob(rs.getDate("DateOfBirth").toLocalDate());
                lawyer.setQualification(rs.getString("Qualification"));
                lawyer.setExperience(rs.getInt("Experience"));
                lawyer.setPosition(rs.getString("Positions"));
                lawyer.setExpertise(rs.getString("Expertise"));
                lawyer.setPhoneNumber(rs.getString("PhoneNumber"));
                lawyer.setEmail(rs.getString("Email"));
                return lawyer;
            }
        }, email); // Use email directly as the query parameter
    
        return availableLawyers;
    }
    
    // Method to fetch assigned cases for the lawyer
    // Method to fetch assigned cases for the lawyer
private List<Map<String, Object>> fetchAssignedCases(int lawyerId) {
    String sql = "SELECT c.CorporateCaseID, c.CaseDesc " +
                 "FROM CorporateCase c " +
                 "JOIN TaskLawyerCorp tl ON c.CorporateCaseID = tl.CorporateCaseID " +
                 "WHERE tl.LawyerID = ?";

    try {
        return jdbcTemplate.queryForList(sql, lawyerId);
    } catch (Exception e) {
        // Handle SQL exception, possibly log it, and return an empty list
        System.err.println("Error fetching cases: " + e.getMessage());
        return List.of(); // Return an empty list if there's an error
    }
}

    
    // Method to fetch assigned tasks for the lawyer
   // Method to fetch assigned tasks for the lawyer
private List<Map<String, Object>> fetchAssignedTasks(int lawyerId) {
    String sql = "SELECT t.TaskID, t.TaskDesc, t.Status " +
                 "FROM TaskCorp t " +
                 "JOIN TaskLawyerCorp tl ON t.TaskID = tl.TaskID " +
                 "WHERE tl.LawyerID = ?";

    try {
        return jdbcTemplate.queryForList(sql, lawyerId);
    } catch (Exception e) {
        // Handle SQL exception, possibly log it, and return an empty list
        System.err.println("Error fetching tasks: " + e.getMessage());
        return List.of(); // Return an empty list if there's an error
    }
}

    
@GetMapping("/paralegal-page")
public String paralegalPage(Model model, Principal principal) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getName());
    String loggedInEmail = userDetails.getUsername(); // Assuming username is the email

    // Check if the user has the role of 'LAWYER'
    if (userDetails.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_PARALEGAL"))) {
        // Fetch lawyer details from the database
        List<Paralegal> paralegals = fetchParalegalByEmail(loggedInEmail);

        if (!paralegals.isEmpty()) {
            // Lawyer found, status is "Approved"
            Paralegal paralegal = paralegals.get(0); // Get the first lawyer
            model.addAttribute("status", "Approved");

            // Fetch assigned cases and tasks
            List<Map<String, Object>> casesAssigned = fetchAssignedCasesP(paralegal.getParalegalID());
            List<Map<String, Object>> tasksAssigned = fetchAssignedTasksP(paralegal.getParalegalID());

            // Only display cases and tasks if they exist
            if (!casesAssigned.isEmpty() || !tasksAssigned.isEmpty()) {
                model.addAttribute("casesAssigned", casesAssigned);
                model.addAttribute("tasksAssigned", tasksAssigned);
                model.addAttribute("assignedCasesCount", casesAssigned.size());
                model.addAttribute("assignedTasksCount", tasksAssigned.size());
            } else {
                model.addAttribute("noAssignments", "No cases or tasks assigned.");
            }
        } else {
            // Lawyer not found, status is "Not Approved"
            model.addAttribute("status", "Not Approved");
        }
    } else {
        // Handle case where user is not a lawyer
        model.addAttribute("error", "Access Denied: You are not authorized to view this page.");
    }

    return "paralegal"; // Thymeleaf template for the lawyer page
}

private List<Paralegal> fetchParalegalByEmail(String email) {
    String sql = "SELECT * FROM Paralegal WHERE email = ?";
    
    List<Paralegal> availableParalegals = jdbcTemplate.query(sql, new RowMapper<Paralegal>() {
        @Override
        public Paralegal mapRow(ResultSet rs, int rowNum) throws SQLException {
            Paralegal paralegal = new Paralegal();
            paralegal.setParalegalID(rs.getInt("ParalegalID"));
            paralegal.setFname(rs.getString("FName")); // Ensure column names match your database
            paralegal.setMname(rs.getString("MName"));
            paralegal.setLname(rs.getString("LName"));
            paralegal.setDob(rs.getDate("DateOfBirth").toLocalDate());
            paralegal.setQualification(rs.getString("Qualification"));
            paralegal.setExperience(rs.getInt("Experience"));
            paralegal.setPosition(rs.getString("Positions"));
            paralegal.setPhoneNumber(rs.getString("PhoneNumber"));
            paralegal.setEmail(rs.getString("Email"));
            return paralegal;
        }
    }, email); // Use email directly as the query parameter

    return availableParalegals;
}

// Method to fetch assigned cases for the lawyer
// Method to fetch assigned cases for the lawyer
private List<Map<String, Object>> fetchAssignedCasesP(int paralegalId) {
String sql = "SELECT c.CorporateCaseID, c.CaseDesc " +
             "FROM CorporateCase c " +
             "JOIN TaskParalegalCorp tl ON c.CorporateCaseID = tl.CorporateCaseID " +
             "WHERE tl.ParalegalID = ?";

try {
    return jdbcTemplate.queryForList(sql, paralegalId);
} catch (Exception e) {
    // Handle SQL exception, possibly log it, and return an empty list
    System.err.println("Error fetching cases: " + e.getMessage());
    return List.of(); // Return an empty list if there's an error
}
}


// Method to fetch assigned tasks for the lawyer
// Method to fetch assigned tasks for the lawyer
private List<Map<String, Object>> fetchAssignedTasksP(int paralegalId) {
String sql = "SELECT t.TaskID, t.TaskDesc, t.Status " +
             "FROM TaskCorp t " +
             "JOIN TaskParalegalCorp tl ON t.TaskID = tl.TaskID " +
             "WHERE tl.ParalegalID = ?";

try {
    return jdbcTemplate.queryForList(sql, paralegalId);
} catch (Exception e) {
    // Handle SQL exception, possibly log it, and return an empty list
    System.err.println("Error fetching tasks: " + e.getMessage());
    return List.of(); // Return an empty list if there's an error
}
}


}
