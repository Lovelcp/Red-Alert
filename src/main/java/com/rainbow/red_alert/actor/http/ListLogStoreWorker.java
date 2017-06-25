package com.rainbow.red_alert.actor.http;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.aliyun.openservices.log.Client;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.response.ListLogStoresResponse;
import com.rainbow.red_alert.message.http.FinishListLogStoreMessage;
import com.rainbow.red_alert.message.http.StartListLogStoreMessage;

import java.util.ArrayList;

public class ListLogStoreWorker extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private String projectName;
    private Client logClient;

    public ListLogStoreWorker(String projectName, Client logClient) {
        this.projectName = projectName;
        this.logClient = logClient;
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        getSelf().tell(new StartListLogStoreMessage(), getSelf());
        log.debug("ListLogStoreWorker with project name[{}] started successfully", projectName);
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof StartListLogStoreMessage) {
            ArrayList<String> logStores = getLogStores();
            getContext().parent()
                        .tell(new FinishListLogStoreMessage(logStores), getSelf());
            log.debug("Get log stores successfully");

            // TODO 自杀？
        }
    }

    private ArrayList<String> getLogStores() throws LogException {
        ListLogStoresResponse response = logClient.ListLogStores(projectName, 0, 1000, "");
        return response.GetLogStores();
    }
}
