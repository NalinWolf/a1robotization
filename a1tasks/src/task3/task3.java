package task3;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class task3 {

    public static void main(String[] args) {
        processLoginsCsv("logins.csv");
        processPostingsCsv("postings.csv");
        // �������� REST API endpoint ��� ��������� ������ �� �������������
    }

    private static void processLoginsCsv(String loginsCsvPath) {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(loginsCsvPath)).withSkipLines(1).build()) {
            DatabaseHandler dbh = new DatabaseHandler();
            String insertQuery = "INSERT INTO logins (Application, AppAccountName, IsActive, JobTitle, Department) VALUES (?, ?, ?, ?, ?)";

            List<String[]> records = reader.readAll();
            for (String[] record : records) {
                try (PreparedStatement preparedStatement = dbh.getDbConnection().prepareStatement(insertQuery)) {
                    preparedStatement.setString(1, record[0]); // ������ ������� - Application
                    preparedStatement.setString(2, record[1]); // ������ ������� - AppAccountName
                    preparedStatement.setBoolean(3, Boolean.parseBoolean(record[2])); // ������ ������� - IsActive
                    preparedStatement.setString(4, record[3]); // ��������� ������� - JobTitle
                    preparedStatement.setString(5, record[4]); // ����� ������� - Department
                    preparedStatement.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processPostingsCsv(String postingsCsvPath) {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(postingsCsvPath)).withSkipLines(1).build()) {
            DatabaseHandler dbh = new DatabaseHandler();
            String insertQuery = "INSERT INTO postings (Mat.Doc., Item, Doc.Date, " +
                    "Pstng Date, MaterialDescription, Quantity, BUn, Amount LC, " +
                    "Crcy, User Name, IsAuthorizedDelivery) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            List<String[]> records = reader.readAll();
            for (String[] record : records) {

                if (record.length >= 11) { // ���������, ���������� �� ��������� � ������� record
                    boolean isAuthorizedDelivery = isUserAuthorized(record[9]); // ������������, ��� ������������ ������� - User Name

                    try (PreparedStatement preparedStatement = dbh.getDbConnection().prepareStatement(insertQuery)) {
                        preparedStatement.setString(1, record[0]); // ������������, ��� ������ ������� - Mat.Doc
                        preparedStatement.setString(2, record[1]); // ������ ������� - Item
                        preparedStatement.setString(3, record[2]); // ������ ������� - Doc Date
                        preparedStatement.setString(4, record[3]); // ��������� ������� - Pstng Date
                        preparedStatement.setString(5, record[4]); // ����� ������� - Material Description
                        preparedStatement.setString(6, record[5]); // ������ ������� - Quantity
                        preparedStatement.setString(7, record[6]); // ������� ������� - BUn
                        preparedStatement.setString(8, record[7]); // ������� ������� - Amount LC
                        preparedStatement.setString(9, record[8]); // ������� ������� - Crcy
                        preparedStatement.setString(10, record[9]); // ������� ������� - User Name
                        preparedStatement.setBoolean(11, isAuthorizedDelivery); // ������������ ������� - Authorized Delivery
                        preparedStatement.executeUpdate();
                    }
                } else {
                    System.out.println("������������ ������ � ������ ��� ���������.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static boolean isUserAuthorized(String userName) {
        try (Connection connection = DatabaseHandler.getDbConnection();
             PreparedStatement preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM logins WHERE AppAccountName = ? AND IsActive = True")) {

            preparedStatement.setString(1, userName);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0; // ���� ������������ ������ � �������, ���������� true
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false; // ���� ��������� ������ ��� ������������ �� ������, ���������� false
    }

}
