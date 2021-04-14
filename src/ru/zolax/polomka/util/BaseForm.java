package ru.zolax.polomka.util;

import javax.swing.*;
import java.awt.*;

public abstract class BaseForm extends JFrame {
    private static String appTitle = "Заголовок";
    private static Image appIcon = null;


    public static void setAppTitle(String appTitle) {
        BaseForm.appTitle = appTitle;
    }

    public static void setAppIcon(Image appIcon) {
        BaseForm.appIcon = appIcon;
    }

    public abstract int getWidth();
    public abstract int getHeight();

    public BaseForm(){
        setTitle(appTitle);
        if (appIcon != null){
            setIconImage(appIcon);
        }
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(getWidth(),getHeight()));
        setLocation(Toolkit.getDefaultToolkit().getScreenSize().width / 2 - getWidth() / 2,
                Toolkit.getDefaultToolkit().getScreenSize().height / 2 - getHeight() / 2);
        pack();
    }
}
