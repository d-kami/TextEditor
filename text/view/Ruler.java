package text.edit.view;

import android.graphics.Canvas;
import android.text.TextPaint;

import text.edit.info.ViewInfo;

public interface Ruler {    
    int getWidth();
    void setFontSize(int fontSize);
    void drawRuler(Canvas canvas, ViewInfo viewInfo, TextPaint paint);
}
