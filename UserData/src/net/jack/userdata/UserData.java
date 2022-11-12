package net.jack.userdata;

import net.jack.userdata.database.Database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;
import javax.swing.JOptionPane;

public class UserData {

    public static void main(String[] args) throws SQLException {

        Database database = new Database();

        try {
            database.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        UserDataHandler dataHandler = new UserDataHandler();
        System.out.println("Have you already registered? ");
        Scanner scan = new Scanner(System.in);
        if (scan.nextLine().equalsIgnoreCase("yes")) {
            System.out.println("All details have been emailed to you.");

            dataHandler.checkId(scan, database);
            dataHandler.checkEmail(scan, database);
            dataHandler.checkPassword(scan, database);

        } else {
            String userName = JOptionPane.showInputDialog("What is your first name?");
            dataHandler.setName(userName);
            String surname = JOptionPane.showInputDialog("What is your surname?");
            dataHandler.setSurname(surname);
            int userAge = Integer.parseInt(JOptionPane.showInputDialog("What is your age?"));
            dataHandler.setAge(userAge);
            String userID = dataHandler.getRandomNumber();
            String userPassword = dataHandler.generatePassword();
            String userEmail = dataHandler.getUserEmail();
            System.out.println("Okay " + dataHandler.getName() + " we are creating you a user I.D as we speak.");
            dataHandler.sendEmail(userName, userID, userEmail, userPassword);

            PreparedStatement ps = database.getConnection().prepareStatement("INSERT INTO users VALUES(?, ?, ?, ?, ?, ?);");
            ps.setString(1, userID);
            ps.setString(2, userName);
            ps.setString(3, surname);
            ps.setInt(4, userAge);
            ps.setString(5, userEmail);
            ps.setString(6, userPassword);
            ps.executeUpdate();


            database.disconnect();

        }

    }
}

