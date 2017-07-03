package com.rainbow.red_alert.monitor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.rainbow.red_alert.monitor.actor.ApplicationAgent;

import java.util.concurrent.ExecutionException;

public class Application {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final ActorSystem system = ActorSystem.create("Red-Alert");
        ActorRef applicationAgent = system.actorOf(Props.create(ApplicationAgent.class), "ApplicationAgent");
    }
}
