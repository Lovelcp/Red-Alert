package com.rainbow.red_alert.actor;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.aliyun.openservices.log.Client;
import com.aliyun.openservices.log.common.Project;
import com.rainbow.red_alert.actor.http.ListLogProjectWorker;
import com.rainbow.red_alert.actor.notification.NotificationDispatcherWorker;
import com.rainbow.red_alert.message.InitApplicationAgentMessage;
import com.rainbow.red_alert.message.http.FinishListLogProjectMessage;
import com.rainbow.red_alert.service.config.ConfigService;
import com.rainbow.red_alert.service.config.Configuration;
import com.rainbow.red_alert.util.ActorUtil;

import java.util.List;

public class ApplicationAgent extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private Client logClient;

    public ApplicationAgent() {
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        getSelf().tell(new InitApplicationAgentMessage(), getSelf());
        log.debug("ApplicationAgent started successfully");
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof InitApplicationAgentMessage) {
            // 项目初始化
            initApplication();
            log.debug("ApplicationAgent init successfully");
        }
        else if (message instanceof FinishListLogProjectMessage) {
            createLogProjectAgents((FinishListLogProjectMessage) message);
            log.debug("ApplicationAgent create log project agents successfully");
        }
    }

    /**
     * 项目初始化
     */
    private void initApplication() {
        Configuration configuration = ConfigService.getConfiguration();
        String endpoint = configuration.getSdk()
                                       .getEndpoint();
        String accessKeyId = configuration.getSdk()
                                          .getAccessKeyId();
        String accessKeySecret = configuration.getSdk()
                                              .getAccessKeySecret();
        this.logClient = new Client(endpoint, accessKeyId, accessKeySecret);
        // 创建ListLogProjectWorker，准备初始化projects
        super.getContext()
             .actorOf(Props.create(ListLogProjectWorker.class, logClient), "ListLogProjectWorker");
        // 创建NotificationDispatcherWorker，接收推送消息
        super.getContext()
             .actorOf(Props.create(NotificationDispatcherWorker.class), "NotificationDispatcherWorker");
    }

    /**
     * 创建LogProjectAgent
     *
     * @param message
     */
    private void createLogProjectAgents(FinishListLogProjectMessage message) {
        List<Project> projects = message.getProjects();
        for (Project project : projects) {
            String projectName = project.getProjectName();
            super.getContext()
                 .actorOf(Props.create(LogProjectAgent.class, projectName, logClient), ActorUtil.getActorName(LogProjectAgent.class, projectName));
        }
    }

}
