package com.hauduepascal.ferez96.battleship.service;

import com.hauduepascal.ferez96.battleship.app.ConfProvider;
import com.hauduepascal.ferez96.battleship.app.Global;
import com.hauduepascal.ferez96.battleship.common.Utils;
import com.hauduepascal.ferez96.battleship.controller.Player;
import com.hauduepascal.ferez96.battleship.controller.Ship;
import com.hauduepascal.ferez96.battleship.enums.TeamColor;
import com.hauduepascal.ferez96.battleship.servlet.AuctionServlet;
import com.hauduepascal.ferez96.battleship.servlet.SubmissionServlet;
import com.hauduepascal.ferez96.battleship.servlet.JudgeServlet;
import com.hauduepascal.ferez96.battleship.validator.PlayerValidator;
import org.apache.commons.io.FileUtils;
import org.cfg4j.provider.ConfigurationProvider;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class JudgeService extends Server implements IService {
    private static final Logger Log = LoggerFactory.getLogger(JudgeService.class);
    public static final ConfigurationProvider Conf = ConfProvider.get(Paths.get(ConfProvider.Instance.getProperty("judge_config_file", String.class)));

    public JudgeService() {
        this(Conf.getProperty("port", Integer.class));
    }

    public JudgeService(int port) {
        super(port);
        Log.info("Create JudgeService at port: " + port);
    }

    public static void main(String[] args) {
        IService server = new JudgeService();
        server.setupAndStart();
    }

    @Override
    public boolean setupAndStart() {
        ServletHandler handler = new ServletHandler();
        handler.addServletWithMapping(AuctionServlet.class, "/auction");
        handler.addServletWithMapping(SubmissionServlet.class, "/submit");
        handler.addServletWithMapping(JudgeServlet.class, "/judge");

        ResourceHandler rh0 = new ResourceHandler();
        rh0.setDirectoriesListed(false);
        ContextHandler context0 = new ContextHandler();
        context0.setContextPath("/");
        context0.setBaseResource(Resource.newResource(ClassLoader.getSystemClassLoader().getResource("html-judge")));
        context0.setHandler(rh0);

        this.setHandler(new HandlerCollection(context0, handler));
        try {
            start();
        } catch (Exception e) {
            Log.error("Can not start JudgeService", e);
            return false;
        }
        return true;
    }
}
