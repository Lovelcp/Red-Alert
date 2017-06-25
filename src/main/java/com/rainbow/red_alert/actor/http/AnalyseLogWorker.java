package com.rainbow.red_alert.actor.http;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.aliyun.openservices.log.Client;
import com.aliyun.openservices.log.exception.LogException;
import com.aliyun.openservices.log.request.GetHistogramsRequest;
import com.aliyun.openservices.log.response.GetHistogramsResponse;
import com.rainbow.red_alert.message.http.InitFetchLogWorkerMessage;
import com.rainbow.red_alert.message.http.StartAnalyseLogMessage;
import com.rainbow.red_alert.message.notification.StartSendNotificationMessage;
import com.rainbow.red_alert.model.Rule;

public class AnalyseLogWorker extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private static final int QUERY_TIMESTAMP_INTERVAL_IN_SECONDS = 30; // 查询间隔30秒的日志内容
    private static final int LOG_AVAILABLE_WAITING_TIME_IN_MINUTES = 2; // 新写入的日志要过2分钟之后才能在阿里云的中查询到

    private Client logClient;
    private String logProjectName;
    private String logStoreName;
    private Rule rule;

    // rule content
    private String topic;
    private String keyword;

    private ActorSelection applicationAgent;

    public AnalyseLogWorker(Client logClient, String logProjectName, String logStoreName, Rule rule) {
        this.logClient = logClient;
        this.logProjectName = logProjectName;
        this.logStoreName = logStoreName;
        this.rule = rule;

        // rule content
        this.topic = rule.getTopic();
        this.keyword = rule.getKeyword();

        // application agent actor
        this.applicationAgent = super.getContext()
                                     .system()
                                     .actorSelection("/user/ApplicationAgent/NotificationDispatcherWorker");
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        getSelf().tell(new InitFetchLogWorkerMessage(), getSelf());
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof InitFetchLogWorkerMessage) {
            initFetchLogWorker();
            log.debug("AnalyseLogWorker with project name {} and log store name {} has been initialized.", logProjectName, logStoreName);
        }
        else if (message instanceof StartAnalyseLogMessage) {
            fetchAndAnalyseLogs((StartAnalyseLogMessage) message);
        }
    }

    /**
     * 初始化
     */
    private void initFetchLogWorker() {
        int now = (int) (System.currentTimeMillis() / 1000);
        int from = now - LOG_AVAILABLE_WAITING_TIME_IN_MINUTES * 60;
        int to = from + QUERY_TIMESTAMP_INTERVAL_IN_SECONDS;
        getSelf().tell(new StartAnalyseLogMessage(from, to), getSelf());
    }

    /**
     * 读取并且解析日志
     *
     * @param message
     * @throws LogException
     * @throws InterruptedException
     */
    private void fetchAndAnalyseLogs(StartAnalyseLogMessage message) throws LogException, InterruptedException {
        int fromTimestamp = message.getFromTimestamp();
        int toTimestamp = message.getToTimestamp();
        int now = (int) (System.currentTimeMillis() / 1000);

        if (now < fromTimestamp) {
            // 如果fromTimestamp晚于当前时间戳，则sleep一段时间
            Thread.sleep(LOG_AVAILABLE_WAITING_TIME_IN_MINUTES * 60 * 1000);
            getSelf().tell(message, getSelf());
        }
        else {
            // 查询日志数据
            long totalLogLines = getLogCount(fromTimestamp, toTimestamp);
            log.debug("Total log lines: {}", totalLogLines);

            if (totalLogLines > 0) {
                // 如果存在相关日志，则告警
                applicationAgent.tell(new StartSendNotificationMessage(logProjectName, logStoreName, rule, fromTimestamp, toTimestamp), getSelf());
                log.debug("AnalyseLogWorker with project name {} and log store name {} has found error log, start to send notification.", logProjectName,
                        logStoreName);
            }

            sendNextAnalyseMessage(toTimestamp);
        }
    }

    /**
     * 获得日志的条数
     *
     * @param fromTimestamp
     * @param toTimestamp
     * @return
     * @throws LogException
     * @throws InterruptedException
     */
    @SuppressWarnings("Duplicates")
    private long getLogCount(int fromTimestamp, int toTimestamp) throws LogException, InterruptedException {
        GetHistogramsResponse histogramsResp;
        while (true) {
            GetHistogramsRequest histogramsReq = new GetHistogramsRequest(logProjectName, logStoreName, topic, keyword, fromTimestamp, toTimestamp);
            histogramsResp = logClient.GetHistograms(histogramsReq);
            if (histogramsResp != null && histogramsResp.IsCompleted()) // IsCompleted() 返回
            {
                // true，表示查询结果是准确的，如果返回
                // false，则重复查询
                break;
            }
            Thread.sleep(200);
        }
        return histogramsResp.GetTotalCount();
    }

    /**
     * 发送下一条解析日志的消息
     *
     * @param toTimestamp
     */
    private void sendNextAnalyseMessage(int toTimestamp) {
        int fromTimestamp = toTimestamp + 1;
        toTimestamp += QUERY_TIMESTAMP_INTERVAL_IN_SECONDS;
        getSelf().tell(new StartAnalyseLogMessage(fromTimestamp, toTimestamp), getSelf());
    }
}
