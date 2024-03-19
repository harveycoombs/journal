package com.harveycoombs.journal;

import java.io.*;
import java.net.*;
import com.sun.net.httpserver.*;

public class Journal {
    public static String root = "/journal/";

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(3003), 0);
        server.createContext("/css/", new FileServer(root + "css/"));
        server.createContext("/js/", new FileServer(root + "js/"));
        server.createContext("/fonts/", new FileServer(root + "fonts/"));

        server.setExecutor(null);

        try {
            server.start();
            System.out.println("ONLINE\nURL: http://journal.harveycoombs.com/\nPORT: 3003");
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
    }

    static class FileServer implements HttpHandler {
        public String path;
        public String fileName;

        public FileServer(String _path) {
            path = _path;
        }

        @Override
        public void handle(HttpExchange t) throws IOException {
            byte[] contents;

            try {
                fileName = Tools.getURLParameter(t.getRequestURI().getPath());
                contents = Tools.fileContents(path + fileName);
            } catch (Exception ex) {
                System.out.println("ERR! " + ex.getMessage());
                String response = "";
                t.sendResponseHeaders(404, 0);

                OutputStream output = t.getResponseBody();
                output.write(response.getBytes());
                output.close();

                return;
            }

            String extension = fileName.substring(fileName.lastIndexOf(".") + 1);
            String mime = switch (extension) {
                case "css" -> "text/css";
                case "js" -> "application/javascript";
                case "woff" -> "font/woff";
                default -> "text/html";
            };

            Headers headers = t.getResponseHeaders();
            headers.set("Content-Type", mime);
            t.sendResponseHeaders(200, 0);

            OutputStream os = t.getResponseBody();

            os.write(contents);
            os.close();
        }
    }
}