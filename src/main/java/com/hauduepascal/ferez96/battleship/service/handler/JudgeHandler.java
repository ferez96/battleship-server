package com.hauduepascal.ferez96.battleship.service.handler;

import com.hauduepascal.ferez96.battleship.common.HtmlBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JudgeHandler extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HtmlBuilder hb = new HtmlBuilder().title("Judge");
        hb.addBodyLine("<a href='/submit'>Nộp bài</a>");
        hb.addBodyLine("<a href='/auction'>Đấu giá</a>");

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setCharacterEncoding("utf8");
        resp.getWriter().write(hb.build());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }
}
