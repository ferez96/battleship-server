package com.hauduepascal.ferez96.battleship.servlet;

import com.hauduepascal.ferez96.battleship.app.BattleShipMain;
import com.hauduepascal.ferez96.battleship.controller.Ship;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

@Deprecated
public class AuctionServlet extends HttpServlet {
    int nShip = BattleShipMain.cfgProvider.getProperty("nship", Integer.class);
    Ship[] ships = new Ship[nShip];

    {
        for (int i = 0; i < nShip; ++i) ships[i] = new Ship();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("text/html-judge;charset=utf-8");
        resp.getWriter().println("<html-judge>" +
                "<head>" +
                "<title>Dau gia tau</title>" +
                "</head>" +
                "<body>"
        );
        for (Ship ship : ships) resp.getWriter().println(ship + "<br>");
        resp.getWriter().println("</body>" +
                "</html-judge>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Receive request type:" + req.getContentType());
        Scanner sc = new Scanner(req.getReader());
        int price1[] = new int[nShip];
        int price2[] = new int[nShip];
        for (int i = 0; i < nShip; ++i) price1[i] = sc.nextInt();
        for (int i = 0; i < nShip; ++i) price2[i] = sc.nextInt();
        int[] result = new int[nShip];
        for (int i = 0; i < nShip; ++i) {
            if (price1[i] > price2[i]) result[i] = 1;
            if (price1[i] < price2[i]) result[i] = 2;
            if (price1[i] == price2[i]) result[i] = 0;
        }

        resp.setStatus(HttpServletResponse.SC_OK);
        resp.setContentType("application/json");
        resp.getWriter().println(Arrays.toString(result));
    }
}
