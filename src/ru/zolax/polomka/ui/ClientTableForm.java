package ru.zolax.polomka.ui;

import ru.zolax.polomka.managers.ClientManager;
import ru.zolax.polomka.models.Client;
import ru.zolax.polomka.util.BaseForm;
import ru.zolax.polomka.util.CustomTableModel;
import ru.zolax.polomka.util.DialogUtil;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import java.awt.event.*;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ClientTableForm extends BaseForm {
    private JPanel mainPanel;
    private JTable table;
    private JButton sortSurnameButton;
    private JButton sortLastComeButton;
    private JButton sortCountComeButton;
    private JButton sortIdButton;
    private JLabel rowCount;
    private JButton filterBirthdayButton;
    private JComboBox genderBox;
    private JButton addClientButton;

    private CustomTableModel<Client> model;
    private List<Client> tableData;

    private boolean sortSurname = false;
    private boolean sortLastCome = false;
    private boolean sortCountCome = false;
    private boolean sortID = true;
    private boolean filterBirthday = false;

    public ClientTableForm() {
        setContentPane(mainPanel);
        initTable();
        initButtons();
        initBoxes();
        setVisible(true);
    }

    private void initBoxes() {
        genderBox.addItem("Фильтровать по полу");
        genderBox.addItem("Мужчина");
        genderBox.addItem("Женщина");
        genderBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    applyFilters();
                }
            }
        });
    }

    private void initButtons() {
        sortSurnameButton.addActionListener(e -> {
            model.sort(new Comparator<Client>() {
                @Override
                public int compare(Client o1, Client o2) {
                    if (sortSurname) {
                        return o2.getLastName().compareTo(o1.getLastName());
                    } else {
                        return o1.getLastName().compareTo(o2.getLastName());
                    }
                }
            });
            sortSurname = !sortSurname;
            sortLastCome = false;
            sortCountCome = false;
            sortID = false;
        });
        sortIdButton.addActionListener(e -> {
            model.sort(new Comparator<Client>() {
                @Override
                public int compare(Client o1, Client o2) {
                    if (sortID) {
                        return Integer.compare(o2.getId(), o1.getId());
                    } else {
                        return Integer.compare(o1.getId(), o2.getId());
                    }
                }
            });
            sortSurname = false;
            sortLastCome = false;
            sortCountCome = false;
            sortID = !sortID;
        });
        sortCountComeButton.addActionListener(e -> {
            model.sort(new Comparator<Client>() {
                @Override
                public int compare(Client o1, Client o2) {
                    if (sortCountCome) {
                        return Integer.compare(o2.getComeCount(), o1.getComeCount());
                    } else {
                        return Integer.compare(o1.getComeCount(), o2.getComeCount());
                    }
                }
            });
            sortSurname = false;
            sortLastCome = false;
            sortCountCome = !sortCountCome;
            sortID = false;
        });

        sortLastComeButton.addActionListener(e -> {
            model.sort(new Comparator<Client>() {
                @Override
                public int compare(Client o1, Client o2) {
                    if (o1.getLastComeCount() == null && o2.getLastComeCount() != null) {
                        return 1;
                    } else if (o2.getLastComeCount() == null && o1.getLastComeCount() != null) {
                        return -1;
                    } else if (o2.getLastComeCount() == null && o1.getLastComeCount() == null) {
                        return 0;
                    }
                    if (sortLastCome) {
                        return o2.getLastComeCount().compareTo(o1.getLastComeCount());
                    } else {

                        return o1.getLastComeCount().compareTo(o2.getLastComeCount());
                    }
                }
            });
            sortSurname = false;
            sortLastCome = !sortLastCome;
            sortCountCome = false;
            sortID = false;
        });

        filterBirthdayButton.addActionListener(e -> {
            filterBirthday = !filterBirthday;
            applyFilters();
        });
        addClientButton.addActionListener(e->{
            dispose();
            new AddEditForm(null);
        });
    }

    private void applyFilters() {
        try {
            List<Client> clients = ClientManager.selectAll();
            int allRowsCount = clients.size();
            if (filterBirthday) {
                clients.removeIf(elem -> !elem.getBirthday().toLocalDate().getMonth().equals(LocalDate.now().getMonth()));
            }
            if (genderBox.getSelectedIndex() == 1) {
                clients.removeIf(elem -> elem.getGenderCode() == 'ж');
            }
            if (genderBox.getSelectedIndex() == 2) {
                clients.removeIf(elem -> elem.getGenderCode() == 'м');
            }
            model.setList(clients);
            model.fireTableDataChanged();
            sortSurname = false;
            sortLastCome = false;
            sortCountCome = false;
            sortID = true;
            rowCount.setText("Записей " + clients.size() + " / " + allRowsCount);
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    private void initTable() {
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(50);
        try {
            tableData = ClientManager.selectAll();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        model = new CustomTableModel<>(
                Client.class,
                tableData,
                new String[]{"ID", "Имя", "Фамилия", "Отчество", "Дата рождения", "Дата регистрации", "Почта", "Телефон", "Пол", "Путь", "Фото", "Кол-во посещений", "Дата последнего посещения"});

        table.setModel(model);
        table.removeColumn(table.getColumn("Путь"));
        rowCount.setText("Записей: " + model.getRowCount() + " / " + model.getRowCount());

        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int row = table.getSelectedRow();
                if (e.getKeyCode() == KeyEvent.VK_DELETE && row != -1) {
                    if (DialogUtil.showConfirm("Вы точно хотите удалить клиента")) {
                        try {
                            ClientManager.deleteByID(model.getList().get(row).getId());
                            model.getList().remove(row);
                            model.fireTableDataChanged();
                            applyFilters();
                        } catch (SQLException sqlException) {
                            sqlException.printStackTrace();
                            DialogUtil.showErr("К клиенту привязана услуга, невозможно удалить\n" +
                                    "(" + sqlException + ")");
                        }

                    }


                }
            }
        });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (e.getClickCount() == 2 && row != -1){
                    new AddEditForm(model.getList().get(row));
                    dispose();
                }
            }
        });
    }

    @Override
    public int getWidth() {
        return 1200;
    }

    @Override
    public int getHeight() {
        return 500;
    }
}
