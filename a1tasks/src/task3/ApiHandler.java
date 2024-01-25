package task3;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.*;

public class ApiHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String requestMethod = exchange.getRequestMethod();
        if (requestMethod.equalsIgnoreCase("GET")) {
            handleGetRequest(exchange);
        } else {
            sendResponse(exchange, 405, "Метод не поддерживается");
        }
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        String requestUri = exchange.getRequestURI().toString();
        String[] uriParts = requestUri.split("/");
        if (uriParts.length != 3 || !uriParts[1].equals("postings")) {
            sendResponse(exchange, 400, "Некорректный запрос");
            return;
        }

        String date = uriParts[2]; // Получаем дату из запроса
        String data = fetchDataFromDatabase(date);
        sendResponse(exchange, 200, data);
    }

    private String fetchDataFromDatabase(String date) {
        String data = "";
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/a1", "localhost", "nalin94541955546A");

            // SQL-запрос для выборки данных по дате
            String query = "SELECT * FROM postings WHERE `Pstng Date` = ?";

            // Подготовка SQL-запроса
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, date);

            // Выполнение запроса
            ResultSet resultSet = statement.executeQuery();

            // Обработка результатов запроса
            while (resultSet.next()) {
                String materialDescription = resultSet.getString("Pstng Date");
                data += materialDescription + "\n";
            }

            // Закрытие ресурсов
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }


    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }
}
