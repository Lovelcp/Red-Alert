package com.rainbow.red_alert.console.controller;

import com.rainbow.red_alert.console.dto.req.RuleReq;
import com.rainbow.red_alert.console.domain.Rule;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/rule")
public class RuleController {

    @GetMapping("/all")
    public List<Rule> getAllRules() {
        return null;
    }

    @GetMapping("/{id}")
    public Rule getRule(
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
