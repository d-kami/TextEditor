package text.edit.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import text.edit.info.ViewInfo;
import text.edit.info.ConstName;

public class HorizontalScrollBar implements ScrollBar{
    private static final int BAR_HEIGHT = 10;
    
    public void draw(Canvas canvas, ViewInfo viewInfo, Paint paint){
        int width = viewInfo.getInt(ConstName.EDITOR_WIDTH);
        int height = viewInfo.getInt(ConstName.EDITOR_HEIGHT);
        int rulerWidth = viewInfo.getInt(ConstName.ROW_RULER_WIDTH);
        
        int startY = height - HorizontalScrollBar.BAR_HEIGHT;
        int hsWidth = width -  HorizontalScrollBar.BAR_HEIGHT;
        
        Rect borderRect = new Rect(rulerWidth, startY, hsWidth, height);
        Rect barRect = HorizontalScrollBar.calcBarRect(viewInfo,borderRect);
        
        int srcColor = paint.getColor();
        Paint.Style srcStyle = paint.getStyle();
        
        HorizontalScrollBar.drawBorder(canvas, borderRect, paint);
        HorizontalScrollBar.drawBar(canvas, barRect, paint);
        
        paint.setColor(srcColor);
        paint.setStyle(srcStyle);
    }
    
    private static void drawBorder(Canvas canvas, Rect rect, Paint paint){
        paint.setColor(0xFFFFFFFF);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        
        canvas.drawRect(rect, paint);
        
        paint.setColor(0xFF000000);
        paint.setStyle(Paint.Style.STROKE);
        
        canvas.drawRect(rect, paint);
    }
    
    private static void drawBar(Canvas canvas, Rect rect, Paint paint){
        paint.setColor(0xFF0000FF);
        paint.setStyle(Paint.Style.FILL);
        
        canvas.drawRect(rect, paint);
    }
    
    private static Rect calcBarRect(ViewInfo viewInfo, Rect borderRect){
        int maxWidth = viewInfo.getInt(ConstName.EDITOR_WIDTH) + viewInfo.getInt(ConstName.SCROLL_MAX_X);
        
        float per = (float)borderRect.width() / maxWidth;
        int barWidth = (int)Math.ceil((viewInfo.getInt(ConstName.EDITOR_WIDTH) * per));
        int startX = (int)Math.ceil((viewInfo.getInt(ConstName.SCROLL_X) * per) + borderRect.left);
        
        Rect rect = new Rect(startX, borderRect.top, startX + barWidth, borderRect.bottom);
        
        return rect;
    }
}
