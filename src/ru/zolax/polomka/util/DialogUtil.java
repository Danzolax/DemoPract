package ru.zolax.polomka.util;

import javax.swing.*;

public class DialogUtil {
    public static void showWarning(String text) {
        JOptionPane.showMessageDialog(null,text,"Предупреждение", JOptionPane.WARNING_MESSAGE);
    }
    public static void showErr(String text) {
        JOptionPane.showMessageDialog(null,text,"Предупреждение", JOptionPane.ERROR_MESSAGE);
    }
    public static void showInf(String text) {
        JOptionPane.showMessageDialog(null,text,"Предупреждение", JOptionPane.INFORMATION_MESSAGE);
    }
    public static boolean showConfirm(String text) {
        return JOptionPane.showOptionDialog(
                null,
                text,
                "Подтверждение",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"ДА","НЕТ"},
                "ДА"
        ) == JOptionPane.YES_OPTION;
    }
}
