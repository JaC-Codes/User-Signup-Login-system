package net.jack.userdata;


import net.jack.userdata.database.Database;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Random;
import java.util.Scanner;

public class UserDataHandler {

    String name;
    String surname;
    int age;

    public String getName() {
        return this.name;
    }

    public String getSurname() {
        return this.surname;
    }

    public int getAge() {
        return this.age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getRandomNumber() {
        Random random = new Random();
        int number = random.nextInt(999999);
        return String.format("%06d", number);
    }

    public String getUserEmail() {
        return name + "@JaCCodes.co.uk";
    }

    public String generatePassword() {
        String password = new Random().ints(15, 33, 122).collect
                (StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
        return password;

    }

    public void checkId(Scanner scan, Database database) throws SQLException {
        System.out.println("Please enter your ID number");
        for (int i = 1; i <= 3; i++) {
            String idGuess = scan.nextLine();
            PreparedStatement ps = database.getConnection().prepareStatement("SELECT * FROM users WHERE IdNumber = ?");
            ps.setString(1, idGuess);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("Id Number found.");
                break;
            } else {
                System.out.println("We can't match your id number in our records.");
                System.out.println("You have " + (3 - i) + " more attempts.");
            }
            if (i == 3) {
                System.exit(0);
            }
        }
    }


    public void checkEmail(Scanner scan, Database database) throws SQLException {
        System.out.println("Please enter your email address given to you upon registering.");

        for (int i = 1; i <= 3; i++) {
            String userEmail = scan.nextLine();
            PreparedStatement ps1 = database.getConnection().prepareStatement("SELECT * FROM users WHERE EmailAddress = ?");
            ps1.setString(1, userEmail);
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                System.out.println("Please enter your password");
                break;
            } else {
                System.out.println("Incorrect email!");
                System.out.println("You have " + (3 - i) + " more attempts.");
            }
            if (i == 3) {
                System.exit(0);
            }
        }
    }

    public void checkPassword(Scanner scan, Database database) throws SQLException {

        for (int i = 1; i <= 3; i++) {
            String userPassword = scan.nextLine();
            PreparedStatement ps2 = database.getConnection().prepareStatement("SELECT * FROM users WHERE UserPassword = ?");
            ps2.setString(1, userPassword);
            ResultSet rs2 = ps2.executeQuery();
            scan.nextLine();
            if (rs2.next()) {
                System.out.println("Success! You will now be redirected to the home portal.");
                break;
            } else {
                System.out.println("Incorrect password!");
                System.out.println("You have " + (3 - i) + " more attempts.");
            }
            if (i == 3) {
                System.exit(0);
            }
        }
    }

    public void sendEmail(String userName, String userID, String userEmail, String userPassword) {
        String personalEmail = JOptionPane.showInputDialog("What is your personal email address?");
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", "smtp.office365.com");
        properties.put("mail.smtp.port", 587);
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.transport.protocol", "smtp");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("", "");
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(""));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(personalEmail));
            message.setSubject("Welcome to JaC Codes");
            message.setText("Hello, " + userName + "\nYour identification number is " + userID + "\nYour company email address is " + userEmail +
                    "\nThe password to access the email account is " + userPassword);
            Transport.send(message);
            System.out.println("An E-Mail with the disclosed details have been sent to you.");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}






