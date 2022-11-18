package net.jack.userdata;

import net.jack.userdata.database.Database;
import net.jack.userdata.gui.GUI;

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
        int registered = JOptionPane.showConfirmDialog(null, "Are you already registered?", "JaCCodes", JOptionPane.YES_NO_CANCEL_OPTION);
        Scanner scan = new Scanner(System.in);

        if (registered == 2) {
            System.exit(0);
        }
        if (registered == -1 ) {
            System.exit(0);
        }
        if (registered == 0) {
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
            String userEmail = dataHandler.getUserEmail(userID);
            JOptionPane.showMessageDialog(null, "Okay " + dataHandler.getName() + " we are setting up your account now.", "JaCCodes", JOptionPane.PLAIN_MESSAGE);
            dataHandler.sendEmail(userName, userID, userEmail, userPassword);
            GUI gui = new GUI();
            gui.sendEmailGUI();


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

