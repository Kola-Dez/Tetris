package kernel.GameInfo;

import java.awt.Color;
import java.util.Random;

public class GetInfo {

    // Метод для генерации случайного цвета
    public static Color randColor() {
        Random random = new Random();
        // Генерируем случайные значения для RGB
        int r = random.nextInt(200) + 56;
        int g = random.nextInt(200) + 56;
        int b = random.nextInt(200) + 56;
        // Возвращаем новый объект цвета на основе сгенерированных значений
        return new Color(r, g, b);
    }
}
