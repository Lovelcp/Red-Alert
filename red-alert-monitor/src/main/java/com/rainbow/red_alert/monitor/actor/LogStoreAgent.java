package com.rainbow.red_alert.monitor.actor;

import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.aliyun.openservices.log.Client;
import com.rainbow.red_alert.monitor.service.config.ConfigService;
import com.rainbow.red_alert.monitor.service.config.Configuration;
import com.rainbow.red_alert.monitor.actor.rule.AnalyseLogWorker;
import com.rainbow.red_alert.monitor.message.InitLogStoreAgentMessage;
import com.rainbow.red_alert.monitor.model.Rule;

import java.util.List;

public class LogStoreAgent extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);

    private Client logClient;
    private String logProjectName;
    private String logStoreName;

    public LogStoreAgent(Client logClient, String logProjectName, String logStoreName) {
        this.logClient = logClient;
        this.logProjectName = logProjectName;
        this.logStoreName = logStoreName;
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
        getSelf().tell(new InitLogStoreAgentMessage(), getSelf());
        log.debug("LogStoreAgent with project name[{}] and store name[{}] started successfully", logProjectName, logStoreName);
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof InitLogStoreAgentMessage) {
            initLogStoreAgent();
            log.debug("LogStoreAgent with project name[{}] and store name[{}] init successfully", logProjectName, logStoreName);
        }
    }

    /**
     * 初始化logstore agent actor
     */
    private void initLogStoreAgent() {
        List<Configuration.RuleConfig> ruleConfigs = getRuleConfigs();
        if (ruleConfigs == null) {
            log.debug("LogStoreAgent with project name[{}] and store name[{}] has no configuration, so it will stop self", logProjectName, logStoreName);
            // 自杀
            getContext().stop(getSelf());
            return;
        }

        // 初始化AnalyseLogWorker
        for (Configuration.RuleConfig ruleConfig : ruleConfigs) {
            String topic = ruleConfig.getTopic();
            List<String> keywords = ruleConfig.getKeywords();

            if (keywords == null) {
                Rule rule = new Rule(topic, null);
                this.getContext()
                    .actorOf(Props.create(AnalyseLogWorker.class, logClient, logProjectName, logStoreName, rule));
            }
            else {
                for (String keyword : keywords) {
                    Rule rule = new Rule(topic, keyword);
                    this.getContext()
                        .actorOf(Props.create(AnalyseLogWorker.class, logClient, logProjectName, logStoreName, rule));
                }
            }
        }
    }

    /**
     * 获取该logstore对应的查询配置
     *
     * @return
     */
    private List<Configuration.RuleConfig> getRuleConfigs() {
        try {
            return ConfigService.getConfiguration()
                                .getRules()
                                .get(logProjectName)
                                .get(logStoreName);
        }
        catch (Throwable t) {
            return null;
        }
    }
}
