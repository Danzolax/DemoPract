package ru.zolax.polomka.models;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;

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
public class Client {
    private int id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private Date birthday;
    private Timestamp registrationDate;
    private String email;
    private String phone;
    private char genderCode;
    private String photoPath;
    private ImageIcon image;
    private int comeCount;
    private Timestamp lastComeCount = null;

    public Client(int id, String firstName, String lastName, String patronymic, Date birthday, Timestamp registrationDate, String email, String phone, char genderCode, String photoPath,int comeCount, Timestamp lastComeCount) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.birthday = birthday;
        this.registrationDate = registrationDate;
        this.email = email;
        this.phone = phone;
        this.genderCode = genderCode;
        this.photoPath = photoPath;
        this.comeCount = comeCount;
        this.lastComeCount = lastComeCount;
        initImage();
    }

    public Client(String firstName, String lastName, String patronymic, Date birthday, Timestamp registrationDate, String email, String phone, char genderCode, String photoPath) {
        this.id = -1;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.birthday = birthday;
        this.registrationDate = registrationDate;
        this.email = email;
        this.phone = phone;
        this.genderCode = genderCode;
        this.photoPath = photoPath;
        initImage();

    }

    private void initImage() {
        URL url = Client.class.getClassLoader().getResource(photoPath);
        if (url !=null){
            try {
                this.image = new ImageIcon(ImageIO.read(url));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            this.image = null;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Timestamp getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Timestamp registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public char getGenderCode() {
        return genderCode;
    }

    public void setGenderCode(char genderCode) {
        this.genderCode = genderCode;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
        initImage();
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", birthday=" + birthday +
                ", registrationDate=" + registrationDate +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", genderCode=" + genderCode +
                ", photoPath='" + photoPath + '\'' +
                '}';
    }

    public int getComeCount() {
        return comeCount;
    }

    public void setComeCount(int comeCount) {
        this.comeCount = comeCount;
    }

    public Timestamp getLastComeCount() {
        return lastComeCount;
    }

    public void setLastComeCount(Timestamp lastComeCount) {
        this.lastComeCount = lastComeCount;
    }
}
