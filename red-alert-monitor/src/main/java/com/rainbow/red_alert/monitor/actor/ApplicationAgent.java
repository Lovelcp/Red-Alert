package com.rainbow.red_alert.monitor.actor;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.aliyun.openservices.log.Client;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.response.ListProjectResponse;
import com.rainbow.red_alert.monitor.service.config.ConfigService;
import com.rainbow.red_alert.monitor.service.config.Configuration;
import com.rainbow.red_alert.monitor.util.ActorUtil;
import com.rainbow.red_alert.monitor.actor.notification.NotificationDispatcherWorker;
import com.rainbow.red_alert.monitor.message.InitApplicationAgentMessage;

import java.util.Set;

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
    }

    /**
     * 项目初始化
     */
    private void initApplication() throws LogException {
        Configuration configuration = ConfigService.getConfiguration();
        String endpoint = configuration.getSdk()
                                       .getEndpoint();
        String accessKeyId = configuration.getSdk()
                                          .getAccessKeyId();
        String accessKeySecret = configuration.getSdk()
                                              .getAccessKeySecret();
        this.logClient = new Client(endpoint, accessKeyId, accessKeySecret);

        // 创建NotificationDispatcherWorker，接收推送消息
        super.getContext()
             .actorOf(Props.create(NotificationDispatcherWorker.class), "NotificationDispatcherWorker");

        // 创建LogProjectAgent
        createLogProjectAgents();
    }

    /**
     * 创建LogProjectAgent
     */
    private void createLogProjectAgents() throws LogException {
        Configuration configuration = ConfigService.getConfiguration();
        Set<String> logProjectNames = configuration.getRules()
                                                   .keySet();

        for (String logProjectName : logProjectNames) {
            ListProjectResponse response = logClient.ListProject(logProjectName, 0, 1);
            if (!response.getProjects()
                         .isEmpty()) {
                // 如果该project存在，则创建对应的LogProjectAgent
                super.getContext()
                     .actorOf(Props.create(LogProjectAgent.class, logProjectName, logClient), ActorUtil.getActorName(LogProjectAgent.class, logProjectName));
            }
        }
    }

}
