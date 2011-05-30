package text.edit.view;

import android.graphics.Canvas;
import android.graphics.Paint;

public class DefaultCaret implements Caret{
    private int row;
    private int column;
    private int color;
    private int width;
    private int startRow;
    private int startColumn;
    private int endRow;
    private int endColumn;
    
    private boolean isSelected;
    
    public DefaultCaret(){
        row = 0;
        column = 0;
        color = 0;
        width = 2;
    }
    
    public int getColumn(){
        return column;
    }
    
    public int getRow(){
        return row;
    }
    
    public void move(int column, int row){
        this.column = column;
        this.row = row;
        isSelected = false;
    }
    
    public void select(int startRow, int startColumn, int endRow, int endColumn){
        this.startColumn = startColumn;
        this.startRow = startRow;
        this.endRow = endRow;
        this.endColumn = endColumn;
    }
    
    public boolean inSelected(int  column, int row){
        if(startRow < endRow || (startRow == endRow && startColumn < endColumn)){
            if(startRow <= row && row <= endRow && startColumn <= column && column <= endColumn ){
                return true;
            }
        }else{
            if(endRow <= row && row <= startRow && endColumn < column && column < startColumn ){
                return true;
            }
        }
        
        return false;
    }
    
    public boolean isSelected(){
        return isSelected = true;
    }
    
    public void setColor(int color){
        this.color = color;
    }
    
    public void draw(Canvas canvas, int x, int y, int fontSize, Paint paint){
        int currentColor = paint.getColor();
        paint.setColor(color);
        canvas.drawRect(x, y - fontSize, x + width, y, paint);
        paint.setColor(currentColor);
    }
}
