import com.mysql.cj.jdbc.Driver;
import java.sql.*;

public class Main {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // Подключение для создания базы данных
        String url = "jdbc:mysql://localhost:3306/";
        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println("Connection successful");

        // Создание базы данных
        try (Connection connection = DriverManager.getConnection(url, "root", "admin");
             Statement statement = connection.createStatement()) {

            // SQL запрос на создание базы данных
            String sql = "CREATE DATABASE IF NOT EXISTS Store";
            statement.executeUpdate(sql);

            System.out.println("База данных успешно создана или уже существует!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Подключение к базе данных для создания таблиц
        String dbUrl = "jdbc:mysql://localhost:3306/Store";

        // Создание таблицы магазинов
        String createStoresTableSQL = "CREATE TABLE IF NOT EXISTS Stores (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(50), " +
                "location VARCHAR(100))";

        // Создание таблицы консультантов
        String createConsultantsTableSQL = "CREATE TABLE IF NOT EXISTS Consultants (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(50), " +
                "email VARCHAR(50), " +
                "phone VARCHAR(20), " +
                "store_id INT, " +
                "FOREIGN KEY (store_id) REFERENCES Stores(id) ON DELETE CASCADE)";

        // Создание таблицы кассиров
        String createCashiersTableSQL = "CREATE TABLE IF NOT EXISTS Cashiers (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(50), " +
                "email VARCHAR(50), " +
                "store_id INT, " +
                "FOREIGN KEY (store_id) REFERENCES Stores(id) ON DELETE CASCADE)";

        // Создание таблиц
        try (Connection connection = DriverManager.getConnection(dbUrl, "root", "admin");
             Statement statement = connection.createStatement()) {

            // Создаем таблицы
            statement.executeUpdate(createStoresTableSQL);
            statement.executeUpdate(createConsultantsTableSQL);
            statement.executeUpdate(createCashiersTableSQL);

            System.out.println("Таблицы 'Stores', 'Consultants' и 'Cashiers' успешно созданы!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        String insertStoreSQL = "INSERT INTO Stores (name, location) VALUES (?, ?)";
        try (Connection connection = DriverManager.getConnection(dbUrl, "root", "admin");
             PreparedStatement preparedStatement = connection.prepareStatement(insertStoreSQL)) {

            String[] storeNames = {"Пятерочка", "Магазин", "Перекресток"};
            String[] storeLocations = {"Пушкинская", "Ленинская", "Терешковой"};
            for (int i = 0; i < storeNames.length; i++) {
                preparedStatement.setString(1, storeNames[i]);
                preparedStatement.setString(2, storeLocations[i]);
                preparedStatement.executeUpdate();
            }
            System.out.println("Данные магазинов успешно добавлены!");
        }

        // Вставка данных в таблицу консультантов (Consultants)
        String insertConsultantSQL = "INSERT INTO Consultants (name, email, phone, store_id) VALUES (?, ?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(dbUrl, "root", "admin");
             PreparedStatement preparedStatement = connection.prepareStatement(insertConsultantSQL)) {

            String[] consultantNames = {"Вася", "Петя", "Маша"};
            String[] consultantEmails = {"Вася@yandex.ru", "Петя@yandex.ru", "Маша@yandex.ru"};
            String[] consultantPhones = {"1234567890", "0987654321", "1122334455"};
            for (int i = 0; i < consultantNames.length; i++) {
                preparedStatement.setString(1, consultantNames[i]);
                preparedStatement.setString(2, consultantEmails[i]);
                preparedStatement.setString(3, consultantPhones[i]);
                preparedStatement.setInt(4, i + 1); // Привязка консультанта к магазину
                preparedStatement.executeUpdate();
            }
            System.out.println("Данные консультантов успешно добавлены!");
        }

        // Вставка данных в таблицу кассиров (Cashiers)
        String insertCashierSQL = "INSERT INTO Cashiers (name, email, store_id) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(dbUrl, "root", "admin");
             PreparedStatement preparedStatement = connection.prepareStatement(insertCashierSQL)) {

            String[] cashierNames = {"Даша", "Владимир", "Армен"};
            String[] cashierEmails = {"Даша@yandex.ru", "Владимир@yandex.ru", "Армен@yandex.ru"};
            for (int i = 0; i < cashierNames.length; i++) {
                preparedStatement.setString(1, cashierNames[i]);
                preparedStatement.setString(2, cashierEmails[i]);
                preparedStatement.setInt(3, i + 1); // Привязка кассира к магазину
                preparedStatement.executeUpdate();
            }
            System.out.println("Данные кассиров успешно добавлены!");

        } catch (Exception e) {
            e.printStackTrace();
        }
        String selectStoresSQL = "SELECT * FROM Stores";
        try (Connection connection = DriverManager.getConnection(dbUrl, "root", "admin");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectStoresSQL)) {

            System.out.println("Магазины:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String location = resultSet.getString("location");
                System.out.println("ID: " + id + ", Name: " + name + ", Location: " + location);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Вывод данных о консультантах
        String selectConsultantsSQL = "SELECT * FROM Consultants";
        try (Connection connection = DriverManager.getConnection(dbUrl, "root", "admin");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectConsultantsSQL)) {

            System.out.println("\nКонсультанты:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                int storeId = resultSet.getInt("store_id");
                System.out.println("ID: " + id + ", Name: " + name + ", Email: " + email +
                        ", Phone: " + phone + ", Store ID: " + storeId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Вывод данных о кассирах
        String selectCashiersSQL = "SELECT * FROM Cashiers";

        try (Connection connection = DriverManager.getConnection(dbUrl, "root", "admin");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(selectCashiersSQL)) {

            System.out.println("\nКассиры:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                int storeId = resultSet.getInt("store_id");
                System.out.println("ID: " + id + ", Name: " + name + ", Email: " + email + ", Store ID: " + storeId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String select1StoreSQL =
                "SELECT c.id, c.name, c.email, c.phone, 'Consultant' AS role " +
                        "FROM Consultants c WHERE c.store_id = 1 " +
                        "UNION " +
                        "SELECT ca.id, ca.name, ca.email, NULL AS phone, 'Cashier' AS role " +
                        "FROM Cashiers ca WHERE ca.store_id = 1";

        try (Connection connection = DriverManager.getConnection(dbUrl, "root", "admin");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(select1StoreSQL)) {

            System.out.println("\nСотрудники первого магазина:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                String phone = resultSet.getString("phone");
                String role = resultSet.getString("role");

                // Вывод данных сотрудника
                System.out.println("ID: " + id + ", Name: " + name + ", Email: " + email +
                        (phone != null ? ", Phone: " + phone : "") + ", Role: " + role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
