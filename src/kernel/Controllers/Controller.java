package kernel.Controllers;

import kernel.Vindow.View;
import kernel.Vindow.ViewSize;

import java.util.HashMap;
import java.util.Objects;

public class Controller {
    public HashMap<String, Integer> data;
    public String complexity;
    private View view;

    public Controller() {
        data = new HashMap<>();
        new ViewSize(this);
    }
    public void restartGame(){
        view.setVisible(false);
        data = new HashMap<>();
        new ViewSize(this);
    }

    public void CreatGame() {
        this.view = new View();
        view.add(new GameController(this));
        view.setVisible(true);
    }

    public void setSizeWorld(String complexity) {
        if (Objects.equals(complexity, "lite")) {
            data.put("BOARD_WIDTH", 10);
            data.put("BOARD_HEIGHT", 15);
            data.put("fallingSpeed", 400);
        } else if (Objects.equals(complexity, "norm")) {
            data.put("BOARD_WIDTH", 15);
            data.put("BOARD_HEIGHT", 25);
            data.put("fallingSpeed", 200);
        } else if (Objects.equals(complexity, "hard")) {
            data.put("BOARD_WIDTH", 25);
            data.put("BOARD_HEIGHT", 40);
            data.put("fallingSpeed", 50);
        }
        this.complexity = complexity;
    }
}
