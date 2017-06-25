package com.rainbow.red_alert.actor.notification;

import akka.actor.ActorSystem;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import com.rainbow.red_alert.message.notification.SendWeChatNotificationMessage;
import com.rainbow.red_alert.util.HttpUtil;

import java.io.UnsupportedEncodingException;

/**
 * 微信推送部分用了ServerChan：http://sc.ftqq.com/3.version
 * Http请求部分用了Akka-Http库，详情参考：http://doc.akka.io/docs/akka-http/current/java/http/common/http-model.html#overvie
 */
public class WeChatNotificationWorker extends UntypedActor {
    private LoggingAdapter log = Logging.getLogger(getContext().system(), this);
    private static final String SERVICE_CHAN_BASE_URI = "https://sc.ftqq.com/";

    private ActorSystem system = super.getContext()
                                      .system();
    private Materializer materializer = ActorMaterializer.create(system);

    public WeChatNotificationWorker() {
    }

    @Override
    public void preStart() throws Exception {
        super.preStart();
    }

    @Override
    public void onReceive(Object message) throws Throwable {
        if (message instanceof SendWeChatNotificationMessage) {
            sendWeChatNotification((SendWeChatNotificationMessage) message);
            log.debug("WeChat notification is sent successfully");
        }
    }

    /**
     * 发送基于ServerChan的微信推送
     *
     * @param message
     */
    private void sendWeChatNotification(SendWeChatNotificationMessage message) throws UnsupportedEncodingException {
        String uri = getFinalServerChanUri(message.getScKey(), message.getTitle(), message.getDescription());
        HttpRequest httpRequest = HttpRequest.create()
                                             .withUri(uri);
        Http.get(system)
            .singleRequest(httpRequest, materializer);
    }

    private String getFinalServerChanUri(String scKey, String title, String description) throws UnsupportedEncodingException {
        String uri = SERVICE_CHAN_BASE_URI + scKey + ".send?" + "text=" + HttpUtil.urlEncode(title);
        if (description != null) {
            uri += "&desp=" + HttpUtil.urlEncode(description);
        }
        return uri;
    }
}
