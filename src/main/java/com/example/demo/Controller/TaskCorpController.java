package com.example.demo.Controller;

import com.example.demo.model.TaskCorp;
import com.example.demo.model.CorporateCase; // Ensure you have this import
import com.example.demo.model.Lawyer;
import com.example.demo.model.Paralegal;

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

@Controller
@RequestMapping("/task")
public class TaskCorpController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/add")
    public String showTaskForm(Model model) {
        // Fetch Corporate Cases for the dropdown
        List<CorporateCase> corporateCases = jdbcTemplate.query("SELECT * FROM CorporateCase", new RowMapper<CorporateCase>() {
            @Override
            public CorporateCase mapRow(ResultSet rs, int rowNum) throws SQLException {
                CorporateCase corporateCase = new CorporateCase();
                corporateCase.setCorporateCaseID(rs.getInt("CorporateCaseID"));
                // Set other fields if necessary
                return corporateCase;
            }
        });

        model.addAttribute("corporateCases", corporateCases);
        model.addAttribute("taskCorp", new TaskCorp());
        return "taskCorp"; // Thymeleaf template for the task form
    }

    @PostMapping("/save")
    public String saveTask(@ModelAttribute TaskCorp taskCorp, Model model) {
        // Save the task
        String sql = "INSERT INTO TaskCorp (TaskID, CorporateCaseID, TaskDesc, Status, DueDate) VALUES (?, ?, ?, ?, ?)";
        int taskID = getNextTaskID(); // Method to get the next TaskID
        jdbcTemplate.update(sql, taskID, 
                            taskCorp.getCorporateCase().getCorporateCaseID(), // Get CorporateCaseID from CorporateCase object
                            taskCorp.getTaskDesc(), 
                            taskCorp.getStatus(), 
                            taskCorp.getDueDate());

        return "redirect:/task/all"; // Redirect to a page listing all tasks
    }

    private int getNextTaskID() {
        String sql = "SELECT COALESCE(MAX(TaskID), 0) + 1 FROM TaskCorp"; // Get the next TaskID
        Integer nextTaskID = jdbcTemplate.queryForObject(sql, Integer.class);
    
        // If nextTaskID is null, return 1 (or handle according to your logic)
        return nextTaskID != null ? nextTaskID : 1;
    }

    @GetMapping("/all")
    public String listTasks(Model model) {
        List<TaskCorp> tasks = jdbcTemplate.query(
            "SELECT * FROM TaskCorp", new RowMapper<TaskCorp>() {
                @Override
                public TaskCorp mapRow(ResultSet rs, int rowNum) throws SQLException {
                    TaskCorp task = new TaskCorp();
                    task.setTaskID(rs.getInt("TaskID"));

                    // Set the corporate case
                    CorporateCase corporateCase = new CorporateCase();
                    corporateCase.setCorporateCaseID(rs.getInt("CorporateCaseID"));
                    task.setCorporateCase(corporateCase);

                    task.setTaskDesc(rs.getString("TaskDesc"));
                    task.setStatus(rs.getString("Status"));
                    task.setDueDate(rs.getDate("DueDate").toLocalDate());
                    return task;
                }
            });
        model.addAttribute("tasks", tasks);
        return "taskList"; // Thymeleaf template for listing tasks
    }



   @GetMapping("/assign/{taskID}/{corporateCaseID}")
public String assignLawyersToTask(@PathVariable int taskID, @PathVariable int corporateCaseID, Model model) {
    // Fetch lawyers/paralegals available for assignment
    List<Lawyer> availableLawyers = jdbcTemplate.query("SELECT * FROM Lawyer", new RowMapper<Lawyer>() {
        @Override
        public Lawyer mapRow(ResultSet rs, int rowNum) throws SQLException {
            Lawyer lawyer = new Lawyer();
            lawyer.setLawyerID(rs.getInt("LawyerID"));
            // Assuming there's a field for name
            lawyer.setFname(rs.getString("fname")); // Make sure this matches your Lawyer entity
            return lawyer;
        }
    });

    // Debug statement
    System.out.println("Available Lawyers: " + availableLawyers);

    model.addAttribute("availableLawyers", availableLawyers);
    model.addAttribute("taskID", taskID);
    model.addAttribute("corporateCaseID", corporateCaseID);
    
    return "assignLawyers"; // Thymeleaf template for assigning lawyers
}


@PostMapping("/assign/save")
public String saveAssignedLawyers(@RequestParam List<Integer> lawyerIDs, @RequestParam int taskID, @RequestParam int corporateCaseID) {
    for (Integer lawyerID : lawyerIDs) {
        // Insert into TaskLawyerCorp
        String sql = "INSERT INTO TaskLawyerCorp (TaskID, LawyerID, CorporateCaseID) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, taskID, lawyerID, corporateCaseID);
    }

    return "redirect:/task/all"; // Redirect back to the task list
}


@GetMapping("/assign/paralegal/{taskID}/{corporateCaseID}")
public String assignParalegalsToTask(@PathVariable int taskID, @PathVariable int corporateCaseID, Model model) {
    // Fetch lawyers/paralegals available for assignment
    List<Paralegal> availableParalegals = jdbcTemplate.query("SELECT * FROM Paralegal", new RowMapper<Paralegal>() {
        @Override
        public Paralegal mapRow(ResultSet rs, int rowNum) throws SQLException {
            Paralegal paralegal = new Paralegal();
            paralegal.setParalegalID(rs.getInt("ParalegalID"));
            // Assuming there's a field for name
            paralegal.setFname(rs.getString("fname")); // Make sure this matches your Lawyer entity
            return paralegal;
        }
    });

    // Debug statement
    System.out.println("Available Paralegals: " + availableParalegals);

    model.addAttribute("availableParalegals", availableParalegals);
    model.addAttribute("taskID", taskID);
    model.addAttribute("corporateCaseID", corporateCaseID);
    
    return "assignParalegals"; // Thymeleaf template for assigning lawyers
}


@PostMapping("/assign/paralegal/save")
public String saveAssignedParalegals(@RequestParam List<Integer> paralegalIDs, @RequestParam int taskID, @RequestParam int corporateCaseID) {
    for (Integer paralegalID : paralegalIDs) {
        // Insert into TaskLawyerCorp
        String sql = "INSERT INTO TaskParalegalCorp (TaskID, ParalegalID, CorporateCaseID) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, taskID, paralegalID, corporateCaseID);
    }

    return "redirect:/task/all"; // Redirect back to the task list
}


}
