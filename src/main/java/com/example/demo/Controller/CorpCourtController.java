package com.example.demo.Controller;

import com.example.demo.model.CorpCourt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/corpCourt")
public class CorpCourtController {

    private final JdbcTemplate jdbcTemplate;

    public CorpCourtController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/form")
    public String showForm(Model model) {
        model.addAttribute("corpCourt", new CorpCourt());
        return "corpCourt";
    }

    @PostMapping("/save")
    public String saveCorpCourt(@ModelAttribute CorpCourt corpCourt) {
        String sql = "INSERT INTO CorpCourt (CourtName, CourtPincode, CourtState, CourtCity, FJName, MJName, LJName) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
    
        jdbcTemplate.update(sql,
                corpCourt.getCourtName(),
                corpCourt.getCourtPincode(),
                corpCourt.getCourtState(),
                corpCourt.getCourtCity(),
                corpCourt.getFjName(),
                corpCourt.getMjName(),
                corpCourt.getLjName());
    
        return "redirect:/corpCourt/form";
    }
    

    @GetMapping("/list")
    public String listCourts(Model model) {
        List<CorpCourt> corpCourts = getAllCourts();
        model.addAttribute("corpCourts", corpCourts);
        return "corpCourtList";
    }

    private List<CorpCourt> getAllCourts() {
        String sql = "SELECT * FROM CorpCourt";
        return jdbcTemplate.query(sql, new RowMapper<CorpCourt>() {
            @Override
            public CorpCourt mapRow(ResultSet rs, int rowNum) throws SQLException {
                CorpCourt corpCourt = new CorpCourt();
                corpCourt.setCourtCorpID(rs.getInt("CourtCorpID"));
                corpCourt.setCourtName(rs.getString("CourtName"));
                corpCourt.setCourtPincode(rs.getString("CourtPincode"));
                corpCourt.setCourtState(rs.getString("CourtState"));
                corpCourt.setCourtCity(rs.getString("CourtCity"));
                corpCourt.setFjName(rs.getString("FJName"));
                corpCourt.setMjName(rs.getString("MJName"));
                corpCourt.setLjName(rs.getString("LJName"));
                return corpCourt;
            }
        });
    }
}
