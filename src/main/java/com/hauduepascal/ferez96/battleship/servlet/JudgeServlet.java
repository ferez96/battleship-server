package com.hauduepascal.ferez96.battleship.servlet;

import com.hauduepascal.ferez96.battleship.common.HtmlBuilder;
import com.hauduepascal.ferez96.battleship.service.JudgeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

import static javax.servlet.http.HttpServletResponse.SC_OK;

public class JudgeServlet extends HttpServlet {

    private static final Logger Log = LoggerFactory.getLogger(JudgeServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HtmlBuilder hb = new HtmlBuilder().title("Judge");
        hb.addBodyLine("<a href='/submit'>Nộp bài</a>");
        hb.addBodyLine("<a href='/auction'>Đấu giá</a>");

        resp.setStatus(SC_OK);
        resp.setCharacterEncoding("utf8");
        resp.getWriter().write(hb.build());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String contentType = req.getContentType();
        if (contentType.equals("text/plain")) {
            BufferedReader br = req.getReader();
            // auth
            String auth = br.readLine();
            if (!auth.equals(JudgeService.Conf.getProperty("auth", String.class)))
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "auth fail");

            // process request type
            String rt = br.readLine();
            switch (rt) {
                case "fight":
                    String player1 = br.readLine();
                    String player2 = br.readLine();
                    // TODO: play match
                    String gameId = "\"" + player1 + "_" + player2 + "\"";
                    resp.setStatus(SC_OK);
                    resp.setContentType("application/json");
                    resp.getWriter().println("{\"status\":\"success\",\"game_id\":" + gameId + "}");
                    break;
                case "get_result":
                    String matchId = br.readLine();
                    //TODO: return metadata
                    break;
                default:
                    Log.info("Unknown request type: " + rt);
                    doGet(req, resp);
            }
        } else {
            doGet(req, resp);
        }
    }
}
