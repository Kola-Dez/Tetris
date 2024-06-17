
package kernel.Objects;

import kernel.GameInfo.Definitions.Tetrominoe;

import java.util.Random;

public class Shape {

    private Tetrominoe pieceShape;
    private int[][] coords;
    private int[][][] coordsTable;

    public Shape() {
        initShape();
    }

    private void initShape() {
        coords = new int[4][2];
        coordsTable = new int[][][] {
                { { 0, 0 },   { 0, 0 },   { 0, 0 },   { 0, 0 } },  // NoShape
                { { 0, -1 },  { 0, 0 },   { -1, 0 },  { -1, 1 } }, // ZShape
                { { 0, -1 },  { 0, 0 },   { 1, 0 },   { 1, 1 } },  // SShape
                { { 0, -1 },  { 0, 0 },   { 0, 1 },   { 0, 2 } },  // LineShape
                { { -1, 0 },  { 0, 0 },   { 1, 0 },   { 0, 1 } },  // TShape
                { { 0, 0 },   { 1, 0 },   { 0, 1 },   { 1, 1 } },  // SquareShape
                { { -1, -1 }, { 0, -1 },  { 0, 0 },   { 0, 1 } },  // LShape
                { { 1, -1 },  { 0, -1 },  { 0, 0 },   { 0, 1 } }   // MirroredLShape
        };
        setShape(Tetrominoe.NoShape);
    }

    public void setShape(Tetrominoe shape) {
        for (int i = 0; i < 4; i++) {
            System.arraycopy(coordsTable[shape.ordinal()][i], 0, coords[i], 0, 2);
        }
        pieceShape = shape;
    }

    public void setX(int index, int x) { coords[index][0] = x; }

    public void setY(int index, int y) { coords[index][1] = y; }

    public int x(int index) { return coords[index][0]; }

    public int y(int index) { return coords[index][1]; }

    public Tetrominoe getShape()  { return pieceShape; }

    public void setRandomShape() {
        Random r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;
        Tetrominoe[] values = Tetrominoe.values();
        setShape(values[x]);
    }

    public int minX() {
        int m = coords[0][0];
        for (int i = 0; i < 4; i++) {
            m = Math.min(m, coords[i][0]);
        }
        return m;
    }

    public int minY() {
        int m = coords[0][1];
        for (int i = 0; i < 4; i++) {
            m = Math.min(m, coords[i][1]);
        }
        return m;
    }

    public Shape rotateLeft() {
        if (pieceShape == Tetrominoe.SquareShape) {
            return this;
        }
        Shape result = new Shape();
        result.pieceShape = pieceShape;
        for (int i = 0; i < 4; ++i) {
            result.setX(i, y(i));
            result.setY(i, -x(i));
        }
        return result;
    }

    public Shape rotateRight() {
        if (pieceShape == Tetrominoe.SquareShape) {
            return this;
        }
        Shape result = new Shape();
        result.pieceShape = pieceShape;
        for (int i = 0; i < 4; ++i) {
            result.setX(i, -y(i));
            result.setY(i, x(i));
        }
        return result;
    }
}
