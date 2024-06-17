
package kernel.Vindow;

import kernel.GameInfo.Definitions;

import javax.swing.*;

public class View extends JFrame {

    public View() {
        setTitle(Definitions.gameName);
        setSize(Definitions.widthWindow, Definitions.heightWindow);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
    }
}