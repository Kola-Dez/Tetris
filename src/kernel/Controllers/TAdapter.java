package kernel.Controllers;

import kernel.Objects.Shape;
import kernel.GameInfo.Definitions.Tetrominoe;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TAdapter extends KeyAdapter {
    private final GameController gameController;

    public TAdapter(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Shape currentShape = gameController.getCurrentShape();
        int currentXPos = gameController.getCurrentXPos();
        int currentYPos = gameController.getCurrentYPos();

        if (currentShape.getShape() == Tetrominoe.NoShape) {
            return;  // Если текущая фигура пустая, игнорируем ввод
        }

        int keyCode = e.getKeyCode();

        // Обработка клавиш
        switch (keyCode) {
            case KeyEvent.VK_SHIFT:
                gameController.togglePause();  // Пауза игры
                break;
            case KeyEvent.VK_A:
                gameController.tryMove(currentShape, currentXPos - 1, currentYPos);  // Движение влево
                break;
            case KeyEvent.VK_D:
                gameController.tryMove(currentShape, currentXPos + 1, currentYPos); // Движение вправо
                break;
            case KeyEvent.VK_Q:
                gameController.tryMove(currentShape.rotateRight(), currentXPos, currentYPos); // Поворот вправо
                break;
            case KeyEvent.VK_W:
                gameController.tryMove(currentShape.rotateLeft(), currentXPos, currentYPos); // Поворот влево
                break;
            case KeyEvent.VK_SPACE:
                gameController.dropDownCurrentPiece();  // Быстрое падение
                break;
            case KeyEvent.VK_S:
                gameController.moveCurrentPieceOneLineDown();   // Сдвиг на одну линию вниз
                break;
        }
    }
}
