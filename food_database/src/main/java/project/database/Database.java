package project.database;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Database will connect the sql database and do the executions
 * Create, remove, insert, select and update is executable
 */
public class Database {
    private static final String dbName = "project.db";
    private static final String dbURL = "jdbc:sqlite:" + dbName;

    /**
     * create the database
     * @return boolean - whether the database is created
     */
    public boolean createDB() {
        File dbFile = new File(dbName);
        if (dbFile.exists()) {
            System.out.println("Database already created");
            return true;
        }
        try (Connection ignored = DriverManager.getConnection(dbURL)) {
            // If we get here that means no exception raised from getConnection - meaning it worked
            System.out.println("A new database has been created.");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        return false;
    }

    /**
     * remove the database
     * @return boolean - whether the database is deleted
     */
    public boolean removeDB() {
        File dbFile = new File(dbName);
        if (dbFile.exists()) {
            boolean result = dbFile.delete();
            if (!result) {
                System.out.println("Couldn't delete existing db file");
                System.exit(-1);
            } else {
                System.out.println("Removed existing DB file.");
                return true;
            }
        } else {
            System.out.println("No existing DB file.");
        }
        return false;
    }

    /**
     * create tables
     * @return boolean - whether the table is created
     */
    public boolean setupDB() {
        String createNutrientsTableSQL =
                """
                CREATE TABLE IF NOT EXISTS nutrients (
                    id integer primary key AUTOINCREMENT,
                    foodID text,
                    num double,
                    measureID text,
                    calories int,
                    totalWeight double
                );
                """;

        String createNurInfoTableSQL =
                """
                CREATE TABLE IF NOT EXISTS nurInfo (
                    nurID int,
                    name text NOT NULL,
                    label text,
                    quantity double,
                    unit text,
                    primary key(nurID, name)
                );
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             Statement statement = conn.createStatement()) {
            statement.execute(createNutrientsTableSQL);
            statement.execute(createNurInfoTableSQL);

            System.out.println("Created tables");
            return true;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        return false;
    }

    /**
     *
     * @param foodID the added food
     * @param num the added num
     * @param measureID the added measure
     * @param calories the added calories
     * @param totalWeight the added weight
     * @return boolean -whether the add execution is successful
     */
    public boolean addNutrients(String foodID, double num, String measureID, int calories, double totalWeight) {
        String addSingleStudentWithParametersSQL =
                """
                        INSERT INTO nutrients(foodID, num, measureID, calories, totalWeight) VALUES
                            (?, ?, ?, ?, ?)
                        """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(addSingleStudentWithParametersSQL)) {
            preparedStatement.setString(1, foodID);
            preparedStatement.setDouble(2, num);
            preparedStatement.setString(3, measureID);
            preparedStatement.setInt(4, calories);
            preparedStatement.setDouble(5, totalWeight);
            preparedStatement.executeUpdate();

            System.out.println(foodID);
            System.out.println(num);
            System.out.println(measureID);
            System.out.println(calories);
            System.out.println(totalWeight);

            System.out.println("Added nutrients data");
        } catch (SQLException e) {
            System.out.println("already exist");
            return false;
        }
        return true;
    }

    /**
     *
     * @param foodID the inserted food
     * @param num the inserted number
     * @param measureID the inserted measure
     * @return int - the selected food's id from database
     */
    public int getNurID(String foodID, double num, String measureID) {
        String statusSQL =
                """
                SELECT id
                FROM nutrients
                WHERE foodID=? and num=? and measureID=?
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(statusSQL)) {
            preparedStatement.setString(1, foodID);
            preparedStatement.setDouble(2, num);
            preparedStatement.setString(3, measureID);
            ResultSet results = preparedStatement.executeQuery();

            while (results.next()) {
                System.out.println(results.getInt("id"));
                return results.getInt("id");
            }

            System.out.println("Finished user query");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return -1;
    }

    /**
     *
     * @param nurID the added nutrient
     * @param name the added nutrient name
     * @param label the added nutrient label
     * @param quantity the added nutrient quantity
     * @param unit the added nutrient unit
     * @return boolean -whether the add execution is successful
     */
    public boolean addNurInfo(int nurID, String name, String label, double quantity, String unit) {
        String addSingleStudentWithParametersSQL =
                """
                INSERT INTO nurInfo(nurID, name, label, quantity, unit) VALUES
                    (?,?,?,?,?)
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(addSingleStudentWithParametersSQL)) {
            preparedStatement.setInt(1, nurID);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, label);
            preparedStatement.setDouble(4, quantity);
            preparedStatement.setString(5, unit);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            System.out.println("total already exist");
            return false;
        }
        return true;
    }

    /**
     *
     * @param id the inserted id
     * @return List<String> - the selected food's nutrient information from database
     */
    public List<String> getTotal(int id){
        String statusSQL =
                """
                SELECT name, label, quantity, unit
                FROM nurInfo
                WHERE nurID=?
                """;

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement preparedStatement = conn.prepareStatement(statusSQL)) {
            preparedStatement.setInt(1, id);
            ResultSet results = preparedStatement.executeQuery();

            List<String> list = new ArrayList<>();
            while (results.next()) {
                String i=results.getString("name")+" "+results.getDouble("quantity")+" "+results.getString("unit");
                list.add(i);
            }

            return list;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return null;
    }
}
