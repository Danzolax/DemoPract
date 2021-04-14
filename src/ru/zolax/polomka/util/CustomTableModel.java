package ru.zolax.polomka.util;

import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;

public class CustomTableModel<T> extends AbstractTableModel {
    private Class<T> cls;
    private List<T> list;
    private String[] columnNames;

    public CustomTableModel(Class<T> cls, List<T> list, String[] columnNames) {
        this.cls = cls;
        this.list = list;
        this.columnNames = columnNames;
    }

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return cls.getDeclaredFields().length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Field field = cls.getDeclaredFields()[columnIndex];
        field.setAccessible(true);
        try {
            return field.get(list.get(rowIndex));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return cls.getDeclaredFields()[columnIndex].getType();
    }

    @Override
    public String getColumnName(int column) {
        return column >= columnNames.length ? "Title" : columnNames[column];
    }

    public void sort(Comparator comparator){
        list.sort(comparator);
        fireTableDataChanged();
    }

    public Class<T> getCls() {
        return cls;
    }

    public void setCls(Class<T> cls) {
        this.cls = cls;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }
}
