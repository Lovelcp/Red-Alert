package com.rainbow.red_alert.console.controller;

import com.rainbow.red_alert.console.domain.Rule;
import com.rainbow.red_alert.console.dto.req.RuleReq;
import com.rainbow.red_alert.console.dto.resp.RuleResp;
import com.rainbow.red_alert.console.repository.RuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rule")
public class RuleController {
    @Autowired
    private RuleRepository ruleRepository;

    @GetMapping("/all")
    public List<RuleResp> getAllRules() {
        List<Rule> rules = ruleRepository.findAll();
        return rules.parallelStream().map(rule -> {
            RuleResp ruleResp = new RuleResp();
            return ruleResp;
        }).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public RuleResp getRule(
            @PathVariable("id")
                    Long id) {
        return null;
    }

    @PostMapping("/add")
    public void addRule(
            @RequestBody
                    RuleReq ruleReq) {

    }

}
