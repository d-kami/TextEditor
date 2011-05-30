package text.edit.view;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.text.TextPaint;

import text.edit.info.ConstName;
import text.edit.info.ViewInfo;

public class RowRuler implements Ruler{
    private static final int DEFAULT_PLACE = 5;
    private int place;
    private int fontSize;
    private int width;
    
    public RowRuler(){
        this(RowRuler.DEFAULT_PLACE);
    }
    
    public RowRuler(int place){
        this.place = place;
    }
    
    public void setFontSize(int fontSize){
        this.fontSize = fontSize;
        width = (fontSize * (place + 1)) / 2;
    }
    
    public int getWidth(){
        return width;
    }
    
    public void drawRuler(Canvas canvas, ViewInfo viewInfo, TextPaint paint){
        int height = viewInfo.getInt(ConstName.EDITOR_HEIGHT);
        
        int srcColor = paint.getColor();
        Paint.Style srcStyle = paint.getStyle();
        
        drawBackground(canvas, width, height, paint);
        drawLineNumber(canvas, width, height, viewInfo, paint);
        
        paint.setColor(srcColor);
        paint.setStyle(srcStyle);
    }
    
    //背景描画
    private void drawBackground(Canvas canvas, int width, int height, TextPaint paint){
        int srcColor = paint.getColor();
        Paint.Style srcStyle = paint.getStyle();
        
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(0xFFFFCC00);
        canvas.drawRect(0, 0, width, height, paint);
        
        //このメソッドが呼ばれる前のスタイルと色に戻す
        paint.setStyle(srcStyle);
        paint.setColor(srcColor);
    }
    
    //行数描画
    private void drawLineNumber(Canvas canvas, int width, int height, ViewInfo viewInfo, TextPaint paint){
        int srcColor = paint.getColor();
        int scrollY = viewInfo.getInt(ConstName.SCROLL_Y);
        
        paint.setColor(Color.BLACK);
        int top = scrollY;
        int bottom = height + scrollY + fontSize;
        int row = (top / fontSize) - 1;
        
        int drawY = (fontSize - (scrollY % fontSize)) - fontSize;
        
        for(int y = top; y <= bottom; y += fontSize){
            String rowNumber = String.format("%0" + place + "d", ++row);
            int x = alignRight(rowNumber, width, paint);
            
            canvas.drawText(rowNumber, x, drawY, paint);
            drawY += fontSize;
        }
        
        //このメソッドが呼ばれる前の色に戻す
        paint.setColor(srcColor);
    }
    
    //渡された文字列が幅widthの領域の中で右揃えになるようなx座標を返す
    private int alignRight(String text, int width, TextPaint paint){
        return width - (int)paint.measureText(text, 0, text.length());
    }
}
