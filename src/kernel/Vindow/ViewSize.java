package kernel.Vindow;

import kernel.Controllers.Controller;
import kernel.GameInfo.Definitions;

import javax.swing.*;
import java.awt.*;

public class ViewSize extends JFrame {

    public ViewSize(Controller c) {
        setTitle(Definitions.gameName);
        setSize(Definitions.widthWindow, Definitions.heightWindow);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Центрируем окно по центру экрана
        setResizable(false);
        setLayout(new BorderLayout());
        setBackground(Color.BLACK);

        JLabel enterComplexityLabel = new JLabel("Введите сложность игры: lite, normal, hard");
        enterComplexityLabel.setFont(new Font("Arial", Font.BOLD, 20));
        enterComplexityLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(enterComplexityLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton playButtonLite = createButton("Lite", c);
        JButton playButtonNormal = createButton("Norm", c);
        JButton playButtonHard = createButton("Hard", c);

        buttonPanel.add(playButtonLite);
        buttonPanel.add(playButtonNormal);
        buttonPanel.add(playButtonHard);

        add(buttonPanel, BorderLayout.SOUTH);

        pack(); // Подгоняем размеры окна
        setVisible(true); // Показываем окно
    }

    private JButton createButton(String label, Controller c) {
        JButton button = new JButton(label);
        button.addActionListener(e -> {
            c.setSizeWorld(label.toLowerCase());
            setVisible(false);
            c.CreatGame();
        });
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        return button;
    }
}
