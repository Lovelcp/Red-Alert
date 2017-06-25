package com.rainbow.red_alert.message.http;

import com.aliyun.openservices.log.common.Project;

import java.util.List;

public class FinishListLogProjectMessage {
    private List<Project> projects;

    public FinishListLogProjectMessage(List<Project> projects) {
        this.projects = projects;
    }

    public List<Project> getProjects() {
        return projects;
    }

    public void setProjects(List<Project> projects) {
        this.projects = projects;
    }
}
