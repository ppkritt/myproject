package com.tong.server;

import com.tong.command.Command;
import com.tong.protocol.Protocol;
import com.tong.protocol.Write;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private Write w = new Write();
    private final static Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) throws Exception {
        new Server().run(6379);
    }

    private void run(int port) throws IOException {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            while (true){
                Socket socket = serverSocket.accept();
                pool.execute( () -> {
                    try {
                        logger.info("{} 已连接。", socket.getInetAddress().getHostName());
                        InputStream is = socket.getInputStream();
                        OutputStream out = socket.getOutputStream();
                        Command command = null;
                        while (true) {
                            try {
                                command = Protocol.readCommand(is);
                                command.run(out);
                            } catch (Exception e) {
                                w.writeError(out, "不识别的命令");
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
