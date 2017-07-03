package com.rainbow.red_alert.monitor.actor.notification;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.rainbow.red_alert.monitor.message.notification.StartSendNotificationMessage;
import com.rainbow.red_alert.monitor.service.config.ConfigService;
import com.rainbow.red_alert.monitor.service.config.Configuration;
import com.rainbow.red_alert.monitor.util.FormatUtil;
import com.rainbow.red_alert.monitor.message.notification.SendWeChatNotificationMessage;
import com.rainbow.red_alert.monitor.model.notification.WeChatSubscriber;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class NotificationDispatcherWorker extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private ActorRef weChatNotificationWorker;

    @Override
    public void preStart() throws Exception {
        super.preStart();
        this.weChatNotificationWorker = super.getContext()
                                             .system()
                                             .actorOf(Props.create(WeChatNotificationWorker.class), "WeChatNotificationWorker");
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof StartSendNotificationMessage) {
            log.debug("NotificationDispatcherWorker received StartSendNotificationMessage");
            dispatch((StartSendNotificationMessage) message);
        }
    }

    private void dispatch(StartSendNotificationMessage message) {
        List<String> subscriberNames = getSubscriberNames(message.getLogProjectName(), message.getLogStoreName());
        if (subscriberNames != null) {
            List<WeChatSubscriber> weChatSubscribers = getWeChatSubscribers(new HashSet<>(subscriberNames));
            if (weChatSubscribers != null) {
                sendWeChatNotifications(weChatSubscribers, message);
            }
        }
    }

    /**
     * 获取订阅者名称
     *
     * @param logProjectName
     * @param logStoreName
     * @return
     */
    private List<String> getSubscriberNames(String logProjectName, String logStoreName) {
        try {
            return ConfigService.getConfiguration()
                                .getSubscribers()
                                .get(logProjectName)
                                .get(logStoreName);
        }
        catch (Throwable t) {
            return null;
        }
    }

    /**
     * 获取微信订阅者
     *
     * @param subscriberNames
     * @return
     */
    private List<WeChatSubscriber> getWeChatSubscribers(Set<String> subscriberNames) {
        List<Configuration.User> users = ConfigService.getConfiguration()
                                                      .getUsers();
        List<WeChatSubscriber> weChatSubscribers = new LinkedList<>();

        for (Configuration.User user : users) {
            String userName = user.getName();
            if (userName != null && subscriberNames.contains(userName)) {
                String scKey = user.getServerChanSckey();
                if (scKey != null) {
                    WeChatSubscriber weChatSubscriber = new WeChatSubscriber(userName, scKey);
                    weChatSubscribers.add(weChatSubscriber);
                }
            }
        }

        return weChatSubscribers;
    }

    /**
     * 发送微信通知
     *
     * @param weChatSubscribers
     * @param message
     */
    private void sendWeChatNotifications(List<WeChatSubscriber> weChatSubscribers, StartSendNotificationMessage message) {
        for (WeChatSubscriber weChatSubscriber : weChatSubscribers) {
            SendWeChatNotificationMessage sendWeChatNotificationMessage = new SendWeChatNotificationMessage(weChatSubscriber.getServerChanSckey(),
                    getWeChatTitle(message.getLogProjectName(), message.getLogStoreName(), message.getRule()
                                                                                                  .getTopic()),
                    getWeChatDescription(message.getFromTimestamp(), message.getToTimestamp(), message.getRule()
                                                                                                      .getKeyword()));
            log.debug("Generate wechat notification message successfully.");
            this.weChatNotificationWorker.tell(sendWeChatNotificationMessage, getSelf());
        }
    }

    private String getWeChatTitle(String logProjectName, String logStoreName, String topic) {
        return "Project:" + logProjectName + "|Logstore:" + logStoreName + "|Topic:" + topic;
    }

    private String getWeChatDescription(int fromTimestamp, int toTimestamp, String keyword) {
        return FormatUtil.readableTime(fromTimestamp) + " ~ " + FormatUtil.readableTime(toTimestamp) + " with keyword:" + keyword;
    }
}
