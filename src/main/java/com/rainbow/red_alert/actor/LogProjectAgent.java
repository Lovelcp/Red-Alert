package com.rainbow.red_alert.actor;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.aliyun.openservices.log.Client;
import com.rainbow.red_alert.actor.http.ListLogStoreWorker;
import com.rainbow.red_alert.message.InitLogProjectAgentMessage;
import com.rainbow.red_alert.message.http.FinishListLogStoreMessage;
import com.rainbow.red_alert.util.ActorUtil;

import java.util.ArrayList;

public class LogProjectAgent extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private Client logClient;
    private String projectName;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        getSelf().tell(new InitLogProjectAgentMessage(), getSelf());
        log.debug("LogProjectAgent with project name[{}] started successfully", projectName);
    }

    public LogProjectAgent(String projectName, Client logClient) {
        this.projectName = projectName;
        this.logClient = logClient;
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof InitLogProjectAgentMessage) {
            initLogProject();
            log.debug("LogProjectAgent with project name[{}] init successfully", projectName);
        }
        else if (message instanceof FinishListLogStoreMessage) {
            createLogStoreAgents((FinishListLogStoreMessage) message);
            log.debug("LogProjectAgent with project name[{}] create log store agents successfully", projectName);
        }
    }

    /**
     * 初始化
     */
    private void initLogProject() {
        this.getContext()
            .actorOf(Props.create(ListLogStoreWorker.class, projectName, logClient));
    }

    /**
     * 创建logstore agent
     *
     * @param message
     */
    private void createLogStoreAgents(FinishListLogStoreMessage message) {
        ArrayList<String> logStores = message.getLogStores();
        for (String logStoreName : logStores) {
            this.getContext()
                .actorOf(Props.create(LogStoreAgent.class, logClient, projectName, logStoreName), ActorUtil.getActorName(LogStoreAgent.class, logStoreName));
        }
    }
}
