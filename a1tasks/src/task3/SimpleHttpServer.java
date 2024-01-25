package task3;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class SimpleHttpServer {
    public static void main(String[] args) {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
            server.createContext("/api", new ApiHandler());
            server.setExecutor(null); // Используем стандартный пул потоков
            server.start();
            System.out.println("Сервер запущен на порту 8000");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
