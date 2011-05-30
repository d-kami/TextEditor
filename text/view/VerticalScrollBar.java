package text.edit.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import text.edit.info.ViewInfo;
import text.edit.info.ConstName;

public class VerticalScrollBar implements ScrollBar{
    private static final int BAR_WIDTH = 10;
    
    public void draw(Canvas canvas, ViewInfo viewInfo, Paint paint){
        int width = viewInfo.getInt(ConstName.EDITOR_WIDTH);
        int height = viewInfo.getInt(ConstName.EDITOR_HEIGHT);
        
        int startX = width - BAR_WIDTH;
        
        Rect borderRect = new Rect(startX, 0, width, height);
        Rect barRect = VerticalScrollBar.calcBarRect(viewInfo,borderRect);
        
        int srcColor = paint.getColor();
        Paint.Style srcStyle = paint.getStyle();
        
        VerticalScrollBar.drawBorder(canvas, borderRect, paint);
        VerticalScrollBar.drawBar(canvas, barRect, paint);
        
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
        int maxHeight = viewInfo.getInt(ConstName.EDITOR_HEIGHT) + viewInfo.getInt(ConstName.SCROLL_MAX_Y);
        
        float per = (float)borderRect.height() / maxHeight;
        int barHeight = (int)Math.ceil((viewInfo.getInt(ConstName.EDITOR_HEIGHT) * per));
        int startY = (int)Math.ceil((viewInfo.getInt(ConstName.SCROLL_Y) * per) + borderRect.top);
        
        Rect rect = new Rect(borderRect.left, startY, borderRect.right,  startY + barHeight);
        
        return rect;
    }
}
