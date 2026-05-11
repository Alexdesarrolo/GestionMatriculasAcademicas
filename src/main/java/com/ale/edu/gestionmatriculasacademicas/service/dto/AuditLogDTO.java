package com.ale.edu.gestionmatriculasacademicas.service.dto;

import java.time.LocalDateTime;

public class AuditLogDTO {
    private Long id;
    private String tableName;
    private String operation;
    private String oldData;
    private String newData;
    private LocalDateTime createdAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }
    
    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }
    
    public String getOldData() { return oldData; }
    public void setOldData(String oldData) { this.oldData = oldData; }
    
    public String getNewData() { return newData; }
    public void setNewData(String newData) { this.newData = newData; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
