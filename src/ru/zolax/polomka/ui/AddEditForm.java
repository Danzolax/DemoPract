package ru.zolax.polomka.ui;

import ru.zolax.polomka.managers.ClientManager;
import ru.zolax.polomka.models.Client;
import ru.zolax.polomka.util.BaseForm;
import ru.zolax.polomka.util.DialogUtil;

import javax.swing.*;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AddEditForm extends BaseForm {
    private JTextField surnameField;
    private JTextField firstNameField;
    private JTextField patronomycField;
    private JTextField birthdayField;
    private JTextField emailField;
    private JTextField phoneField;
    private JComboBox genderBox;
    private JTextField pathField;
    private JButton backButton;
    private JButton addOrEditButton;
    private JLabel idLabel;
    private JPanel mainPanel;
    private SimpleDateFormat formatter;

    private boolean addForm = false;
    private boolean editForm = false;

    private Client client;
    public AddEditForm(Client client){
        if (client == null){
            addForm = true;
        } else{
            editForm = true;
            this.client = client;
        }
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        setContentPane(mainPanel);
        initButtons();
        initUI();
        initBoxes();
        setVisible(true);
    }

    private void initBoxes() {
        genderBox.addItem("м");
        genderBox.addItem("ж");
        if(editForm){
            if(client.getGenderCode() == 'м'){
                genderBox.setSelectedIndex(0);
            } else{
                genderBox.setSelectedIndex(1);
            }
        }
    }

    private boolean isValidFields(){
        String firstName = firstNameField.getText();
        String lastName = surnameField.getText();
        String patronomyc = patronomycField.getText();
        String birthday = birthdayField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String path = pathField.getText();
        StringBuilder out = new StringBuilder("Ошибки:\n");
        boolean flag = true;

        if (!firstName.matches("[a-zA-Zа-яА-Я\\-\s]*")){
            flag = false;
            out.append("Неправильный формат имени(только буквы - и пробел)\n");
        }
        if (!lastName.matches("[a-zA-Zа-яА-Я\\-\s]*")){
            flag = false;
            out.append("Неправильный формат фамилии(только буквы - и пробел)\n");
        }
        if (!patronomyc.matches("[a-zA-Zа-яА-Я\\-\s]*")){
            flag = false;
            out.append("Неправильный формат отчества(только буквы - и пробел)\n");
        }
        if (lastName.length() > 50 || firstName.length() > 50 || patronomyc.length() > 50){
            flag = false;
            out.append("Слишком длинное ФИО (больше 50 символов)\n");
        }
        if (!email.matches("[a-zA-Z@.\\-_0-9]*")){
            flag = false;
            out.append("Неверный формат email\n");
        }
        if (!phone.matches("[0-9()+\\-\s]*")){
            flag = false;
            out.append("Неверный формат телефона\n");
        }
        try{
            formatter.parse(birthday);
        } catch (ParseException exception) {
            exception.printStackTrace();
            flag = false;
            out.append("Неверный формат даты\n");
        }
        URL url = AddEditForm.class.getClassLoader().getResource(path);
        if (url == null){
            flag = false;
            out.append("Неверный путь к фото\n");
        }
        if (!flag){
            DialogUtil.showErr(out.toString());
        }
        return flag;
    }

    private void initUI() {
        if(editForm){
            idLabel.setVisible(true);
            idLabel.setText("ID: " + client.getId());
            firstNameField.setText(client.getFirstName());
            surnameField.setText(client.getLastName());
            patronomycField.setText(client.getPatronymic());
            birthdayField.setText(formatter.format(client.getBirthday()));
            emailField.setText(client.getEmail());
            phoneField.setText(client.getPhone());
            pathField.setText(client.getPhotoPath());

        } else{
            idLabel.setVisible(false);
        }
    }

    private void initButtons() {
        if (editForm){
            addOrEditButton.setText("Редактировать");
            addOrEditButton.addActionListener(e->{
                if (isValidFields()){
                    client.setFirstName(firstNameField.getText());
                    client.setLastName(surnameField.getText());
                    client.setPatronymic(patronomycField.getText());
                    try {
                        client.setBirthday(new Date(formatter.parse(birthdayField.getText()).getTime()));
                    } catch (ParseException exception) {
                        exception.printStackTrace();
                    }
                    client.setPhone(phoneField.getText());
                    client.setEmail(emailField.getText());
                    client.setPhotoPath(pathField.getText());
                    if (genderBox.getSelectedIndex() == 0){
                        client.setGenderCode('м');
                    } else{
                        client.setGenderCode('ж');
                    }
                    try {
                        ClientManager.update(client);
                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                    }
                    dispose();
                    new ClientTableForm();
                }
            });
        } else{
            addOrEditButton.setText("Добавить");
            addOrEditButton.addActionListener(e->{
                    if (isValidFields()){
                        try {
                            client = new Client(
                                    firstNameField.getText(),
                                    surnameField.getText(),
                                    patronomycField.getText(),
                                    new Date(formatter.parse(birthdayField.getText()).getTime()),
                                    new Timestamp(new java.util.Date().getTime()),
                                    emailField.getText(),
                                    phoneField.getText(),
                                    ' ',
                                    pathField.getText()
                            );
                            if (genderBox.getSelectedIndex() == 0){
                                client.setGenderCode('м');
                            } else{
                                client.setGenderCode('ж');
                            }
                            try {
                                ClientManager.insert(client);
                            } catch (SQLException sqlException) {
                                sqlException.printStackTrace();
                            }
                            dispose();
                            new ClientTableForm();
                        } catch (ParseException exception) {
                            exception.printStackTrace();
                        }

            }});
        }
        backButton.addActionListener(e->{
            dispose();
            new ClientTableForm();
        });

    }

    @Override
    public int getWidth() {
        return 500;
    }

    @Override
    public int getHeight() {
        return 500;
    }
}
