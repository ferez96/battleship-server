package com.hauduepascal.ferez96.battleship.service;

import com.hauduepascal.ferez96.battleship.app.Global;
import com.hauduepascal.ferez96.battleship.handler.Hello;
import org.cfg4j.provider.ConfigurationProvider;
import org.cfg4j.provider.ConfigurationProviderBuilder;
import org.cfg4j.source.files.FilesConfigurationSource;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Paths;
import java.util.Collections;

public class WebServer extends Server {

    private static final Logger Log = LoggerFactory.getLogger(WebServer.class);

    public WebServer(int port) {
        super(port);
    }

    public boolean setupAndStart() {
        ContextHandler context = new ContextHandler("/hello");
        context.setContextPath("/hello");
        context.setHandler(new Hello());

        ContextHandlerCollection contexts = new ContextHandlerCollection();
        contexts.setHandlers(new Handler[]{
                context
        });
        setHandler(contexts);
        try {
            start();
        } catch (Exception e) {
            Log.error("Can not start WebServer", e);
            return false;
        }
        return true;
    }


    public static void main(String[] args) {
        Log.info("Start WebServer");
        ConfigurationProvider provider = new ConfigurationProviderBuilder()
                .withConfigurationSource(new FilesConfigurationSource(() -> Collections.singletonList(Global.PROJECT_PATH.resolve("application.yaml"))))
                .build();
        WebServer server = new WebServer(provider.getProperty("webserver.port", Integer.class));
        server.setupAndStart();
    }
}
