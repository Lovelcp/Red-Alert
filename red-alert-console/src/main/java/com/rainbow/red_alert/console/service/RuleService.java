package com.rainbow.red_alert.console.service;

import com.rainbow.red_alert.console.repository.LogStoreRepository;
import com.rainbow.red_alert.console.repository.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RuleService {
    @Autowired
    private RuleRepository ruleRepository;

    @Autowired
    private LogStoreRepository logStoreRepository;
}
