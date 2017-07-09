package com.rainbow.red_alert.console.dto.resp;

import java.util.Date;

public class RuleResp {
    private Long id;
    private Long logStoreId;
    private String keyword;
    private Date createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLogStoreId() {
        return logStoreId;
    }

    public void setLogStoreId(Long logStoreId) {
        this.logStoreId = logStoreId;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
