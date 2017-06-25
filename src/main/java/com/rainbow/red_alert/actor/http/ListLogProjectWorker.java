package com.rainbow.red_alert.actor.http;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.aliyun.openservices.log.Client;
import com.aliyun.openservices.log.common.Project;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.response.ListProjectResponse;
import com.rainbow.red_alert.message.http.FinishListLogProjectMessage;
import com.rainbow.red_alert.message.http.StartListLogProjectMessage;

import java.util.List;

public class ListLogProjectWorker extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private Client logClient;

    public ListLogProjectWorker(Client client) {
        this.logClient = client;
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        getSelf().tell(new StartListLogProjectMessage(), getSelf());
        log.debug("ListLogProjectWorker started successfully");
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof StartListLogProjectMessage) {
            List<Project> projects = getLogProjects();
            log.debug("Get log projects successfully");
            getContext().parent()
                        .tell(new FinishListLogProjectMessage(projects), getSelf());

            // TODO 自杀？
        }
    }

    /**
     * 获取日志服务中所有的projects
     *
     * @return
     * @throws LogException
     */
    private List<Project> getLogProjects() throws LogException {
        ListProjectResponse response = logClient.ListProject();
        return response.getProjects();
    }

}
