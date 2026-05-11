package com.ale.edu.gestionmatriculasacademicas.web.controller;

import com.ale.edu.gestionmatriculasacademicas.service.dto.AuditLogDTO;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AuditController {

    private final JdbcTemplate jdbcTemplate;

    public AuditController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/audit")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<AuditLogDTO> getAuditLogs() {
        String sql = "SELECT id, table_name, operation, old_data::text, new_data::text, created_at FROM sys_audit_log ORDER BY id DESC LIMIT 100";
        
        try {
            return jdbcTemplate.query(sql, new RowMapper<AuditLogDTO>() {
                @Override
                public AuditLogDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
                    AuditLogDTO dto = new AuditLogDTO();
                    dto.setId(rs.getLong("id"));
                    dto.setTableName(rs.getString("table_name"));
                    dto.setOperation(rs.getString("operation"));
                    dto.setOldData(rs.getString("old_data"));
                    dto.setNewData(rs.getString("new_data"));
                    if (rs.getTimestamp("created_at") != null) {
                        dto.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    }
                    return dto;
                }
            });
        } catch (Exception e) {
            // Tabla podría no existir aún si no se corrió el script
            return List.of();
        }
    }
}
