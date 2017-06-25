package com.rainbow.red_alert;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.rainbow.red_alert.actor.ApplicationAgent;

public class Main {
    public static void main(String[] args) {
        final ActorSystem system = ActorSystem.create("Red-Alert");
        ActorRef applicationAgent = system.actorOf(Props.create(ApplicationAgent.class), "ApplicationAgent");
    }
}
