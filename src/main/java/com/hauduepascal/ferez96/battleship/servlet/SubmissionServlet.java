package com.hauduepascal.ferez96.battleship.servlet;

import com.hauduepascal.ferez96.battleship.app.BattleShipMain;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class SubmissionServlet extends HttpServlet {
    private static final Logger Log = LoggerFactory.getLogger(SubmissionServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect("/submit.html-judge");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        resp.setContentType("text/plain");
//        req.getReader().lines().forEach(out::write);
        if (ServletFileUpload.isMultipartContent(req)) {
            try {
                DiskFileItemFactory factory = new DiskFileItemFactory();
                File repository = new File(".tmp");
                factory.setRepository(repository);
                ServletFileUpload upload = new ServletFileUpload(factory);
                List<FileItem> items = upload.parseRequest(req);
                String dirName = null;
                byte[] set_cpp = null;
                byte[] play_cpp = null;
                for (FileItem item : items) {
                    String fn = item.getFieldName();
                    switch (fn) {
                        case "set_cpp":
                            set_cpp = item.get();
                            break;
                        case "play_cpp":
                            play_cpp = item.get();
                            break;
                        case "team_name":
                            dirName = item.getString();
                            break;
                        default:
                            out.write(item.getFieldName());
                            out.write("=");
                            out.write(item.getString());
                            out.write("\n");
                    }
                }
                if (dirName != null && set_cpp != null && play_cpp != null) {
                    Path playerDir = Paths.get(BattleShipMain.cfgProvider.getProperty("players.dir", String.class)).resolve(dirName);
                    Log.info("Upload player source code to: " + playerDir);
                    Files.createDirectories(playerDir);
                    try (PrintStream ps = new PrintStream(String.valueOf(playerDir.resolve("SET.CPP")))) {
                        ps.write(set_cpp);
                    } catch (IOException e) {
                        Log.error("Can't write to SET.CPP");
                    }
                    try (PrintStream ps = new PrintStream(String.valueOf(playerDir.resolve("PLAY.CPP")))) {
                        ps.write(set_cpp);
                    } catch (IOException e) {
                        Log.error("Can't write to PLAY.CPP");
                    }
                }
            } catch (FileUploadException ex) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Upload fail:" + ex);
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "require content-type: multipart/form-data");
        }
    }
}
