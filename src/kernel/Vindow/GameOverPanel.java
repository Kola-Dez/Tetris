package kernel.Vindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import kernel.Controllers.DatabaseController;
import kernel.Controllers.GameController;
import kernel.Database.Connect;

public class GameOverPanel extends JPanel {

    private final GameController gameController;

    public GameOverPanel(GameController gameController) {
        this.gameController = gameController;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);
        JLabel gameOverLabel = new JLabel("Игра окончена");
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 36));
        gameOverLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(gameOverLabel, BorderLayout.NORTH);

        DefaultTableModel model;
        if (testConnect()) {
            // Создаем модель таблицы с заголовками столбцов
            model = new DefaultTableModel();
            model.setColumnIdentifiers(new String[]{"Имя", "Линии", "Время"});
            DatabaseController databaseController = new DatabaseController(gameController.complexity);

            databaseController.put(
                    gameController.getNamePlayer(),
                    gameController.getLinesRemoved(),
                    gameController.getTime()
            );

            ArrayList<HashMap<String, String>> data = databaseController.getForTable();

            // Добавляем отсортированные данные в модель таблицы
            for (HashMap<String, String> record : data) {
                String[] rowData = new String[]{record.get("name"), record.get("line"), record.get("time")};
                model.addRow(rowData);
            }
            databaseController.connect.closeConnection();
        } else {
            // Данные для таблицы при отсутствии подключения
            String[] columnNames = {"Имя", "Линии", "Время"};
            Object[][] data = {
                    {"нет", "подключения", "к бд"}
            };
            // Создаем модель таблицы на основе данных и заголовков
            model = new DefaultTableModel(data, columnNames);
        }

        // Создаем таблицу на основе модели данных
        JTable table = new JTable(model);
        table.setGridColor(Color.BLACK);
        table.setFont(new Font("Arial", Font.BOLD, 24));
        table.setRowHeight(30); // Устанавливаем высоту строк

        // Добавляем таблицу в скроллируемую панель, чтобы была возможность прокрутки, если строк много
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = getPanel();

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel getPanel() {
        JButton exitButton = new JButton("exit");
        exitButton.addActionListener(e -> gameController.exitGame());
        exitButton.setFont(new Font("Arial", Font.PLAIN, 18));
        exitButton.setHorizontalAlignment(SwingConstants.CENTER);

        JButton restartButton = new JButton("restart");
        restartButton.addActionListener(e -> gameController.restartGame());
        restartButton.setFont(new Font("Arial", Font.PLAIN, 18));
        restartButton.setHorizontalAlignment(SwingConstants.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());

        buttonPanel.add(exitButton);
        buttonPanel.add(restartButton);
        return buttonPanel;
    }

    private boolean testConnect() {
        Connect connect = new Connect();
        if (connect.getConnection() != null) {
            connect.closeConnection();
            return true;
        } else {
            return false;
        }
    }
}
