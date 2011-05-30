package text.edit.view;

import android.graphics.Canvas;
import android.graphics.Paint;

public interface Caret {
    int getColumn();
    int getRow();
    void move(int column, int row);
    void select(int startRow, int startColumn, int endRow, int endColumn);
    boolean isSelected();
    boolean inSelected(int column, int row);
    void setColor(int color);
    void draw(Canvas canvas, int x, int y, int fontsize, Paint paint);
}

