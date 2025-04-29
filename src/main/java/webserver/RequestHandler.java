package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

import webserver.http.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.http.response.Response;
import webserver.http.response.handler.Handler;
import webserver.http.request.parser.RequestParser;

public class RequestHandler implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            RequestParser requestParser = new RequestParser(in);
            Request request = requestParser.parseRequest();
            logger.debug("==========================HTTP Request parsing complete==========================");

            Dispatcher dispatcher = new Dispatcher(request);
            Handler handler = dispatcher.dispatch();
            logger.debug(handler.getClass().toString());
            logger.debug("==========================Selected Handler complete==========================");

            Response response = handler.handle(request);
            byte[] responseMessage = response.getResponseMessage();
            logger.debug("response message: {}", new String(responseMessage));
            logger.debug("==========================Write response Message==========================");

            out.write(responseMessage, 0, responseMessage.length);
            out.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
