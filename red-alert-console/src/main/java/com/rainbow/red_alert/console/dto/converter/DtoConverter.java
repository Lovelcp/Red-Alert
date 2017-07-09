package com.rainbow.red_alert.console.dto.converter;

import com.rainbow.red_alert.console.domain.Rule;
import com.rainbow.red_alert.console.dto.resp.RuleResp;

public final class DtoConverter {
    private DtoConverter() {}

    public static RuleResp getRuleResp(Rule rule) {
        RuleResp ruleResp = new RuleResp();
        ruleResp.setId(rule.getId());
        ruleResp.setKeyword(rule.getKeyword());
        ruleResp.setLogStoreId(rule.getLogStoreId());
        ruleResp.setCreatedAt(rule.getCreatedAt());
        return ruleResp;
    }
}
