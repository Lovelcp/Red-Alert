package com.rainbow.red_alert.actor;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.aliyun.openservices.log.Client;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.response.ListLogStoresResponse;
import com.rainbow.red_alert.message.InitLogProjectAgentMessage;
import com.rainbow.red_alert.service.config.ConfigService;
import com.rainbow.red_alert.service.config.Configuration;
import com.rainbow.red_alert.util.ActorUtil;

import java.util.Set;

public class LogProjectAgent extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private Client logClient;
    private String logProjectName;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        getSelf().tell(new InitLogProjectAgentMessage(), getSelf());
        log.debug("LogProjectAgent with project name[{}] started successfully", logProjectName);
    }

    public LogProjectAgent(String logProjectName, Client logClient) {
        this.logProjectName = logProjectName;
        this.logClient = logClient;
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof InitLogProjectAgentMessage) {
            initLogProject();
            log.debug("LogProjectAgent with project name[{}] init successfully", logProjectName);
        }
    }

    /**
     * 初始化
     */
    private void initLogProject() throws LogException {
        createLogStoreAgents();
    }

    /**
     * 创建logstore agent
     */
    private void createLogStoreAgents() throws LogException {
        Configuration configuration = ConfigService.getConfiguration();
        Set<String> logStoreNames = configuration.getRules()
                                                 .get(logProjectName)
                                                 .keySet();
        for (String logStoreName : logStoreNames) {
            ListLogStoresResponse response = logClient.ListLogStores(logProjectName, 0, 1, logStoreName);
            if (!response.GetLogStores()
                         .isEmpty()) {
                this.getContext()
                    .actorOf(Props.create(LogStoreAgent.class, logClient, logProjectName, logStoreName),
                            ActorUtil.getActorName(LogStoreAgent.class, logStoreName));
            }
        }
    }
}
