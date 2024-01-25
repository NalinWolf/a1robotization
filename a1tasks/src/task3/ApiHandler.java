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
            sendResponse(exchange, 405, "����� �� ��������������");
        }
    }

    private void handleGetRequest(HttpExchange exchange) throws IOException {
        String requestUri = exchange.getRequestURI().toString();
        String[] uriParts = requestUri.split("/");
        if (uriParts.length != 3 || !uriParts[1].equals("postings")) {
            sendResponse(exchange, 400, "������������ ������");
            return;
        }

        String date = uriParts[2]; // �������� ���� �� �������
        String data = fetchDataFromDatabase(date);
        sendResponse(exchange, 200, data);
    }

    private String fetchDataFromDatabase(String date) {
        String data = "";
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/a1", "localhost", "nalin94541955546A");

            // SQL-������ ��� ������� ������ �� ����
            String query = "SELECT * FROM postings WHERE `Pstng Date` = ?";

            // ���������� SQL-�������
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, date);

            // ���������� �������
            ResultSet resultSet = statement.executeQuery();

            // ��������� ����������� �������
            while (resultSet.next()) {
                String materialDescription = resultSet.getString("Pstng Date");
                data += materialDescription + "\n";
            }

            // �������� ��������
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
