package text.edit.view;

import android.graphics.Canvas;
import android.graphics.Paint;

import text.edit.info.ViewInfo;

public interface ScrollBar {
    void draw(Canvas canvas, ViewInfo viewInfo, Paint paint);
}
