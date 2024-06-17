package kernel.Controllers;

import kernel.GameInfo.Definitions;
import kernel.GameInfo.GetInfo;
import kernel.Objects.Shape;
import kernel.GameInfo.Definitions.Tetrominoe;
import kernel.Vindow.GameOverPanel;
import kernel.Vindow.PressNamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Objects;

public class GameController extends JPanel {

    private Timer timer;
    private boolean isFallingFinished = false;
    private boolean isPaused = false;
    private int linesRemoved = 0;
    private int currentXPos = 0;
    private int currentYPos = 0;
    private Shape currentShape;
    private Tetrominoe[] board;
    private int fallingSpeed;
    private Shape nextShape;
    private String namePlayer = null;
    private long startTime;
    private final Controller controller;
    public int BOARD_WIDTH;
    public int BOARD_HEIGHT;
    public String complexity;

    public GameController(Controller controller) {
        this.controller = controller;
        this.BOARD_WIDTH = controller.data.get("BOARD_WIDTH");
        this.BOARD_HEIGHT = controller.data.get("BOARD_HEIGHT");
        this.fallingSpeed = controller.data.get("fallingSpeed");
        this.complexity = controller.complexity;
        setFocusable(true);
        addKeyListener(new TAdapter(this));
        generateNextShape();
        startGame();
    }

    /**
     * Запуск игры.
     */
    public void startGame() {
        currentShape = new Shape();
        board = new Tetrominoe[BOARD_WIDTH * BOARD_HEIGHT];
        clearBoard();
        spawnNewPiece();
        timer = new Timer(fallingSpeed, new GameCycle());
        timer.start();
        timerStart();
        generateNextShape();
    }

    /**
     * Переключение паузы в игре.
     */
    public void togglePause() {
        isPaused = !isPaused;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        renderGame(g);
        drawNextShape(g);
    }

    /**
     * Отрисовка игрового поля и текущей фигуры.
     * @param g Графический контекст для отрисовки.
     */
    private void renderGame(Graphics g) {
        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - BOARD_HEIGHT * getSquareHeight();

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Definitions.widthWindow, Definitions.heightWindow);

        // Отрисовка всех блоков на игровой доске
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                Tetrominoe shape = getShapeAt(j, BOARD_HEIGHT - i - 1);
                if (shape != Tetrominoe.NoShape) {
                    drawSquare(g, j * getSquareWidth(), boardTop + i * getSquareHeight(), shape);
                }
            }
        }

        // Отрисовка текущей фигуры
        if (currentShape.getShape() != Tetrominoe.NoShape) {
            for (int i = 0; i < 4; i++) {
                int x = currentXPos + currentShape.x(i);
                int y = currentYPos - currentShape.y(i);
                drawSquare(g, x * getSquareWidth(), boardTop + (BOARD_HEIGHT - y - 1) * getSquareHeight(), currentShape.getShape());
            }
        }
    }

    /**
     * Быстрый спуск текущей фигуры вниз.
     */
    public void dropDownCurrentPiece() {
        int newY = currentYPos;
        while (newY > 0) {
            if (!tryMove(currentShape, currentXPos, newY - 1)) {
                break;
            }
            newY--;
        }
        pieceDropped();
    }

    /**
     * Перемещение текущей фигуры на одну строку вниз.
     */
    public void moveCurrentPieceOneLineDown() {
        int dropLine = 1;
        if (!tryMove(currentShape, currentXPos, currentYPos - dropLine)) {
            pieceDropped();
        }
    }

    /**
     * Очистка игровой доски.
     */
    private void clearBoard() {
        for (int i = 0; i < BOARD_HEIGHT * BOARD_WIDTH; i++) {
            board[i] = Tetrominoe.NoShape;
        }
    }

    /**
     * Обработка падения фигуры на дно или на другую фигуру.
     */
    private void pieceDropped() {
        for (int i = 0; i < 4; i++) {
            int x = currentXPos + currentShape.x(i);
            int y = currentYPos - currentShape.y(i);
            board[(y * BOARD_WIDTH) + x] = currentShape.getShape();
        }
        removeFullLines();
        if (!isFallingFinished) {
            spawnNewPiece();
            generateNextShape();
            fallingSpeed += 300;
        }
    }

    /**
     * Попытка переместить фигуру в новое положение.
     * @param newPiece Новая фигура для перемещения.
     * @param newX Новая координата X.
     * @param newY Новая координата Y.
     * @return true, если перемещение возможно, иначе false.
     */
    public boolean tryMove(Shape newPiece, int newX, int newY) {
        for (int i = 0; i < 4; i++) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT) {
                return false;
            }
            if (getShapeAt(x, y) != Tetrominoe.NoShape) {
                return false;
            }
        }
        currentShape = newPiece;
        currentXPos = newX;
        currentYPos = newY;
        repaint();
        return true;
    }

    /**
     * Удаление заполненных линий на игровой доске.
     */
    private void removeFullLines() {
        int numFullLines = 0;
        for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {
            boolean lineIsFull = true;
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (getShapeAt(j, i) == Tetrominoe.NoShape) {
                    lineIsFull = false;
                    break;
                }
            }
            if (lineIsFull) {
                numFullLines++;
                for (int k = i; k < BOARD_HEIGHT - 1; k++) {
                    for (int j = 0; j < BOARD_WIDTH; j++) {
                        board[(k * BOARD_WIDTH) + j] = getShapeAt(j, k + 1);
                    }
                }
            }
        }
        if (numFullLines > 0) {
            linesRemoved += numFullLines;
            isFallingFinished = true;
            currentShape.setShape(Tetrominoe.NoShape);
            playLineClearSound();
            repaint();
        }
    }

    /**
     * Отрисовка квадрата с указанными координатами и цветом.
     * @param g Графический контекст для отрисовки.
     * @param x Координата X квадрата.
     * @param y Координата Y квадрата.
     * @param shape Фигура квадрата.
     */
    private void drawSquare(Graphics g, int x, int y, Tetrominoe shape) {
        Color[] colors = {
                new Color(0, 0, 0),
                new Color(204, 102, 102),
                new Color(102, 204, 102),
                new Color(102, 102, 204),
                new Color(204, 204, 102),
                new Color(204, 102, 204),
                new Color(102, 204, 204),
                new Color(218, 170, 0)
        };
        Color color = colors[shape.ordinal()];
        Color randColor = GetInfo.randColor();
        g.setColor(color);
        g.fillRect(x, y, getSquareWidth(), getSquareHeight());
        g.setColor(randColor);
        g.drawLine(x, y + getSquareHeight() - 1, x, y);
        g.drawLine(x, y, x + getSquareWidth() - 1, y);
        g.setColor(randColor);
        g.drawLine(x + 1, y + getSquareHeight() - 1, x + getSquareWidth() - 1, y + getSquareHeight() - 1);
        g.drawLine(x + getSquareWidth() - 1, y + getSquareHeight() - 1, x + getSquareWidth() - 1, y + 1);
    }

    /**
     * Получение ширины квадрата на игровой доске.
     * @return Ширина квадрата.
     */
    private int getSquareWidth() {
        return (int) getSize().getWidth() / BOARD_WIDTH;
    }

    /**
     * Получение высоты квадрата на игровой доске.
     * @return Высота квадрата.
     */
    private int getSquareHeight() {
        return (int) getSize().getHeight() / BOARD_HEIGHT;
    }

    /**
     * Получение фигуры по указанным координатам на игровой доске.
     * @param x Координата X.
     * @param y Координата Y.
     * @return Тип фигуры (Tetrominoe) на указанных координатах.
     */
    private Tetrominoe getShapeAt(int x, int y) {
        return board[(y * BOARD_WIDTH) + x];
    }

    /**
     * Получение количества удаленных линий.
     * @return Количество удаленных линий.
     */
    public int getLinesRemoved() {
        return linesRemoved;
    }

    /**
     * Получение текущей игровой фигуры.
     * @return Текущая игровая фигура.
     */
    public Shape getCurrentShape() {
        return currentShape;
    }

    /**
     * Получение текущей координаты X текущей игровой фигуры.
     * @return Текущая координата X текущей игровой фигуры.
     */
    public int getCurrentXPos() {
        return currentXPos;
    }

    /**
     * Получение текущей координаты Y текущей игровой фигуры.
     * @return Текущая координата Y текущей игровой фигуры.
     */
    public int getCurrentYPos() {
        return currentYPos;
    }

    /**
     * Выход из игры.
     */
    public void exitGame() {
        System.exit(1);
    }

    /**
     * Внутренний класс для цикла игры.
     */
    private class GameCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            gameLoop();
        }

        /**
         * Основной цикл игры.
         */
        private void gameLoop() {
            updateGame();
            repaint();
        }

        /**
         * Обновление состояния игры на каждой итерации.
         */
        private void updateGame() {
            if (isPaused) {
                return;
            }
            if (isFallingFinished) {
                isFallingFinished = false;
                spawnNewPiece();
                generateNextShape();
            } else {
                moveCurrentPieceOneLineDown();
            }
        }
    }

    /**
     * Создание новой игровой фигуры и ее спаун на игровой доске.
     */
    private void spawnNewPiece() {
        currentShape.setShape(nextShape.getShape());
        currentXPos = BOARD_WIDTH / 2;
        currentYPos = BOARD_HEIGHT - 1 + currentShape.minY();
        if (!tryMove(currentShape, currentXPos, currentYPos)) {
            currentShape.setShape(Tetrominoe.NoShape);
            timer.stop();
            PressNamePanel pressNamePanel = new PressNamePanel(this);
            getParent().add(pressNamePanel);
            setVisible(false);
        }
    }

    public void viewGameOverPanel() {
        GameOverPanel gameOverPanel = new GameOverPanel(this);
        getParent().add(gameOverPanel);
        gameOverPanel.setVisible(true);
    }

    /**
     * Генерация следующей случайной игровой фигуры.
     */
    private void generateNextShape() {
        nextShape = new Shape();
        nextShape.setRandomShape();
    }

    /**
     * Отрисовка следующей фигуры на боковой панели.
     * @param g Графический контекст для отрисовки.
     */
    private void drawNextShape(Graphics g) {
        g.setColor(Color.YELLOW);
        g.drawString(linesRemoved + " lines ", (int) (Definitions.widthWindow / 1.37), (int) (Definitions.heightWindow / 5.3));

        if (!Objects.equals(complexity, "lite")) {
            Dimension size = getSize();
            int nextShapeTop = (int) size.getHeight() / 8;

            g.setColor(Color.BLUE);
            g.drawLine((int) (Definitions.widthWindow / 1.4), Definitions.heightWindow / 5, Definitions.widthWindow, Definitions.heightWindow / 5);
            g.drawLine((int) (Definitions.widthWindow / 1.4), Definitions.heightWindow / 5, (int) (Definitions.widthWindow / 1.4), 0);

            // Отрисовка следующей фигуры
            for (int i = 0; i < 4; i++) {
                int x = nextShape.x(i);
                int y = nextShape.y(i);
                drawSquare(g, (BOARD_WIDTH + x) * getSquareWidth() - 100, nextShapeTop - 50 + y * getSquareHeight(), nextShape.getShape());
            }
        }
    }

    /**
     * Запуск таймера.
     */
    private void timerStart() {
        this.startTime = System.nanoTime();
    }

    /**
     * Получение текущего времени.
     * @return Текущее время в секундах.
     */
    public int getTime() {
        long endTime = System.nanoTime();
        double durationSeconds = (endTime - startTime) / 1e9;
        return (int) durationSeconds;
    }

    /**
     * Воспроизведение звукового эффекта при очистке линии (пока не реализовано).
     */
    private void playLineClearSound() {
        // звуковой эффект No Detect
    }

    /**
     * Получение имени игрока.
     * @return Имя игрока.
     */
    public String getNamePlayer() {
        return this.namePlayer;
    }

    public void restartGame() {
        controller.restartGame();
    }

    public void setNamePlayer(String namePlayer) {
        this.namePlayer = namePlayer;
    }
}
