package text.edit.view;

import android.text.TextPaint;

public class DrawPoint {
    //現在の描画位置のx座標とy座標
    private int drawX;
    private int drawY;
    
    //現在の位置(行と列)
    private int column;
    private int row;
    
    //描画位置を移動する
    public void drawMove(int drawX, int drawY){
        this.drawX = drawX;
        this.drawY = drawY;
    }
    
    //行と列を移動する
    public void cellMove(int column, int row){
        this.column = column;
        this.row = row;
    }
    
    //次の文字の位置に移動する
    public void next(String drawChar, TextPaint paint){
        int charWidth = (int)paint.measureText(drawChar, 0, 1);
        drawX += charWidth;
        column++;
    }
    
    public int getDrawX(){
        return drawX;
    }
    
    public int getDrawY(){
        return drawY;
    }
    
    public int getRow(){
        return row;
    }
    
    public int getColumn(){
        return column;
    }
}
