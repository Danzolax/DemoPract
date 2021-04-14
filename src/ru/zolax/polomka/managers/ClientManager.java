package ru.zolax.polomka.managers;

import ru.zolax.polomka.Application;
import ru.zolax.polomka.models.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
CREATE TABLE IF NOT EXISTS `Client` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `FirstName` VARCHAR(50) CHARACTER SET 'utf8mb4' NOT NULL,
  `LastName` VARCHAR(50) CHARACTER SET 'utf8mb4' NOT NULL,
  `Patronymic` VARCHAR(50) CHARACTER SET 'utf8mb4' NULL,
  `Birthday` DATE NULL,
  `RegistrationDate` DATETIME(6) NOT NULL,
  `Email` VARCHAR(255) CHARACTER SET 'utf8mb4' NULL,
  `Phone` VARCHAR(20) CHARACTER SET 'utf8mb4' NOT NULL,
  `GenderCode` CHAR(1) CHARACTER SET 'utf8mb4' NOT NULL,
  `PhotoPath` VARCHAR(1000) CHARACTER SET 'utf8mb4' NULL,
  PRIMARY KEY (`ID`),
  CONSTRAINT `FK_Client_Gender`
    FOREIGN KEY (`GenderCode`)
    REFERENCES `Gender` (`Code`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

* */

public class ClientManager {
    public static List<Client> selectAll() throws SQLException {
        try(Connection c = Application.getConnection()) {
            String sql = "SELECT * FROM client";
            PreparedStatement statement = c.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            List<Client> clients = new ArrayList<>();
            while (result.next()){
                clients.add(new Client(
                        result.getInt("ID"),
                        result.getString("FirstName"),
                        result.getString("LastName"),
                        result.getString("Patronymic"),
                        result.getDate("Birthday"),
                        result.getTimestamp("RegistrationDate"),
                        result.getString("Email"),
                        result.getString("Phone"),
                        result.getString("GenderCode").charAt(0),
                        result.getString("PhotoPath"),
                        getComeCountByID(result.getInt("ID")),
                        getLastComeByID(result.getInt("ID"))
                ));
            }
            return clients;
        }
    }

    public static int getComeCountByID(int ID) throws SQLException{
        try(Connection c = Application.getConnection()) {
            String sql = "SELECT count(clientservice.ID) from clientservice inner join client on client.ID = clientservice.ClientID where client.ID = ?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1,ID);
            ResultSet result = statement.executeQuery();
            if (result.next()){
                return result.getInt(1);
            }
            return 0;
        }
    }

    public static Timestamp getLastComeByID(int ID) throws SQLException{
        try(Connection c = Application.getConnection()) {
            String sql = "SELECT max(clientservice.StartTime) from clientservice inner join client on client.ID = clientservice.ClientID where client.ID = ?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1,ID);
            ResultSet result = statement.executeQuery();
            if (result.next()){
                return result.getTimestamp(1);
            }
            return null;
        }
    }


    public static void deleteByID(int ID) throws SQLException{
        try(Connection c = Application.getConnection()) {
            String sql = "DELETE FROM client where ID=?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setInt(1,ID);
            statement.executeUpdate();
        }
    }

    public static void update(Client client) throws SQLException{
        try(Connection c = Application.getConnection()) {
            String sql = "UPDATE client SET FirstName=?,LastName=?,Patronymic=?,Birthday=?,RegistrationDate=?, Email=?,Phone=?, GenderCode=?,PhotoPath=? where ID=?";
            PreparedStatement statement = c.prepareStatement(sql);
            statement.setString(1,client.getFirstName());
            statement.setString(2,client.getLastName());
            statement.setString(3,client.getPatronymic());
            statement.setDate(4,client.getBirthday());
            statement.setTimestamp(5,client.getRegistrationDate());
            statement.setString(6,client.getEmail());
            statement.setString(7,client.getPhone());
            statement.setString(8,String.valueOf(client.getGenderCode()));
            statement.setString(9,client.getPhotoPath());
            statement.setInt(10,client.getId());
            statement.executeUpdate();

        }
    }
    public static void insert(Client client) throws SQLException{
        try(Connection c = Application.getConnection()) {
            String sql = "insert into client (FirstName,LastName,Patronymic,Birthday,RegistrationDate,Email,Phone,GenderCode,PhotoPath) values (?,?,?,?,?,?,?,?,?)";
            PreparedStatement statement = c.prepareStatement(sql,PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1,client.getFirstName());
            statement.setString(2,client.getLastName());
            statement.setString(3,client.getPatronymic());
            statement.setDate(4,client.getBirthday());
            statement.setTimestamp(5,client.getRegistrationDate());
            statement.setString(6,client.getEmail());
            statement.setString(7,client.getPhone());
            statement.setString(8,String.valueOf(client.getGenderCode()));
            statement.setString(9,client.getPhotoPath());
            statement.executeUpdate();
            ResultSet result = statement.getGeneratedKeys();
            if (result.next()){
                client.setId(result.getInt(1));
            }
        }
    }


}
