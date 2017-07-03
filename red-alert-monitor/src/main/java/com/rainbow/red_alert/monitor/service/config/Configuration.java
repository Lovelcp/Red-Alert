package com.rainbow.red_alert.monitor.service.config;

import java.util.List;
import java.util.Map;

public class Configuration {
    private ServerConfig server;
    private Sdk sdk;
    private Map<String, Map<String, List<RuleConfig>>> rules;
    private Map<String, Map<String, List<String>>> subscribers;
    private List<User> users;

    public ServerConfig getServer() {
        return server;
    }

    public void setServer(ServerConfig server) {
        this.server = server;
    }

    public Sdk getSdk() {
        return sdk;
    }

    public void setSdk(Sdk sdk) {
        this.sdk = sdk;
    }

    public Map<String, Map<String, List<RuleConfig>>> getRules() {
        return rules;
    }

    public void setRules(Map<String, Map<String, List<RuleConfig>>> rules) {
        this.rules = rules;
    }

    public Map<String, Map<String, List<String>>> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Map<String, Map<String, List<String>>> subscribers) {
        this.subscribers = subscribers;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public static class ServerConfig {
        private String host;
        private int port;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public int getPort() {
            return port;
        }

        public void setPort(int port) {
            this.port = port;
        }
    }

    public static class Sdk {
        private String endpoint;
        private String accessKeyId;
        private String accessKeySecret;

        public String getEndpoint() {
            return endpoint;
        }

        public void setEndpoint(String endpoint) {
            this.endpoint = endpoint;
        }

        public String getAccessKeyId() {
            return accessKeyId;
        }

        public void setAccessKeyId(String accessKeyId) {
            this.accessKeyId = accessKeyId;
        }

        public String getAccessKeySecret() {
            return accessKeySecret;
        }

        public void setAccessKeySecret(String accessKeySecret) {
            this.accessKeySecret = accessKeySecret;
        }
    }

    public static class RuleConfig {
        private List<String> keywords;
        private String topic;

        public List<String> getKeywords() {
            return keywords;
        }

        public void setKeywords(List<String> keywords) {
            this.keywords = keywords;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }
    }

    public static class User {
        private String name;
        private String serverChanSckey;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getServerChanSckey() {
            return serverChanSckey;
        }

        public void setServerChanSckey(String serverChanSckey) {
            this.serverChanSckey = serverChanSckey;
        }
    }
}
