package ru.zolax.polomka;

import ru.zolax.polomka.managers.ClientManager;
import ru.zolax.polomka.ui.ClientTableForm;
import ru.zolax.polomka.util.BaseForm;
import ru.zolax.polomka.util.DialogUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Application {
    private Application instance;

    private Application()  {
        instance = this;
        initUI();
        initDB();
        new ClientTableForm();
    }

    private void initDB() {
        try(Connection c = getConnection()) {

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            DialogUtil.showErr("Не робит");
            System.exit(-1);
        }
    }

    private void initUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //FontUtil.changeAllFonts(new FontUIResource("Tw Cen MT", Font.TRUETYPE_FONT,12));
        BaseForm.setAppTitle("Сервисный центр ООО \"Поломка\" ");
        URL url = Application.class.getClassLoader().getResource("service_logo.png");
        if (url != null){
            try {
                BaseForm.setAppIcon(ImageIO.read(url));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            DialogUtil.showWarning("Иконка не найдена");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost/demo2?serverTimeZone=Europe/Moscow&characterEncoding=utf8",
                "root",
                "161200");
    }

    public static void main(String[] args)  {
        new Application();
    }
}
