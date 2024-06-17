package kernel.GameInfo;

import java.awt.*;
import java.util.HashMap;
import java.util.Random;
public abstract class Definitions {
    public static String gameName = "Tetris";
    // Перечисление для хранения типов тетромино (фигур)
    public enum Tetrominoe {NoShape, ZShape, SShape, LineShape, TShape, SquareShape, LShape, MirroredLShape}
    // Размеры окна
    public static int widthWindow = 600;
    public static int heightWindow = 800;

}