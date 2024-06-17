
package kernel.Vindow;

import kernel.Controllers.GameController;

import javax.swing.*;
        import java.awt.*;

public class PressNamePanel extends JPanel {

    private final GameController gameController;
    private JTextField nameField;

    public PressNamePanel(GameController gameController) {
        this.gameController = gameController;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel enterNameLabel = new JLabel("Введите имя");
        enterNameLabel.setFont(new Font("Arial", Font.BOLD, 36));
        enterNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterNameLabel, BorderLayout.NORTH);

        // Поле ввода имени
        nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 18));
        nameField.setHorizontalAlignment(SwingConstants.CENTER);
        add(nameField, BorderLayout.CENTER);

        JButton playButton = getjButton();
        add(playButton, BorderLayout.SOUTH);
    }

    private JButton getjButton() {
        JButton playButton = new JButton("К таблице лидеров");
        playButton.addActionListener(e -> {
            String playerName = nameField.getText().trim();
            if (!playerName.isEmpty()) {
                gameController.setNamePlayer(playerName);
                setVisible(false);
                gameController.viewGameOverPanel();
            } else {
                JOptionPane.showMessageDialog(this, "Введите имя игрока", "Ошибка", JOptionPane.ERROR_MESSAGE);
            }
        });
        playButton.setFont(new Font("Arial", Font.PLAIN, 18));
        playButton.setHorizontalAlignment(SwingConstants.CENTER);
        return playButton;
    }
}