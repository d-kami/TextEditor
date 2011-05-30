package text.edit.view;

import java.util.Map;
import java.util.HashMap;

import android.view.View;
import android.view.MotionEvent;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Canvas;
import android.text.TextPaint;

import text.edit.info.ViewInfo;
import text.edit.info.ConstName;
import text.edit.document.TextDocument;
import text.edit.document.DocumentChangeHandler;
import text.edit.token.TextTokenizer;
import text.edit.token.JavaTokenizer;

public class EditorView extends View implements DocumentChangeHandler{
    //このエディタのフォントサイズ
    private int fontSize;
    
    //このエディタのドキュメント
    private TextDocument document;
    
    //キーワード色分け用カラーマップ
    private Map<String, Integer> colorMap;
    
    //エディタやキャレット、スクロールバーの情報など
    private ViewInfo viewInfo;
    
    //このエディタのキャレット
    private Caret caret;
    
    //このエディタの行番号表示ビュー
    private Ruler rowRuler;
    
    //垂直方向のスクロールバー
    private ScrollBar verticalScroll;
    
    //水平方向のスクロールバー
    private ScrollBar horizontalScroll;
    
    //タッチイベントの起こったx座標
    private int touchX;
    //タッチイベントの起こったy座標
    private int touchY;
    
    //水平方向スクロールの限界位置
    private int maxScrollX;
    //垂直方向スクロールの限界位置
    private int maxScrollY;
    
    //テキスト描画に使うPaint
    private TextPaint paint;
    
    //スクロールのノイズ(ずれ)を無視する閾値
    private static final int IGNORE_SCROLL = 20;
    
    public EditorView(Context context){
        super(context);
        
        fontSize = 16;
        document = new TextDocument();    
        document.addDocumentChangeHandler(this);
        
        initColorMap();
        initViewInfo();
        initRuler();
        initCaret();
        initScroll();
        setBackgroundColor(Color.WHITE);
    }
    
    //カラーマップの初期化
    private void initColorMap(){
        colorMap = new HashMap<String, Integer>();
        
        //キーワードと色の関連付け
        colorMap.put("import", Color.BLUE);
        colorMap.put("public", Color.BLUE);
        colorMap.put("class", Color.BLUE);
        colorMap.put("static", Color.RED);
        colorMap.put("String", 0xFF008000);
        colorMap.put("void", 0xFF008000);
    }
    
    //エディタの情報の初期化
    private void initViewInfo(){
        viewInfo = new ViewInfo();
        viewInfo.put(ConstName.EDITOR_FONT_SIZE, fontSize);
        
        viewInfo.put(ConstName.SCROLL_X, 0);
        viewInfo.put(ConstName.SCROLL_Y, 0);
        
        viewInfo.put(ConstName.CARET_ROW, 0);
        viewInfo.put(ConstName.CARET_COLUMN, 0);
    }
    
    //ルーラの初期化
    private void initRuler(){
        rowRuler = new RowRuler();
        rowRuler.setFontSize(fontSize);
        viewInfo.put(ConstName.ROW_RULER_WIDTH, rowRuler.getWidth());
    }
    
    
    //キャレットの初期化
    private void initCaret(){
        caret = new DefaultCaret();
        caret.setColor(0xFF000000);
    }
    
    //スクロール位置やスクロールバーの初期化
    private void initScroll(){
        maxScrollX =0;
        maxScrollY = 0;
        viewInfo.put(ConstName.SCROLL_MAX_X, maxScrollX);
        viewInfo.put(ConstName.SCROLL_MAX_Y, maxScrollY);
        
        horizontalScroll = new HorizontalScrollBar();
        verticalScroll = new VerticalScrollBar();
        
        paint = new TextPaint();
        paint.setTextSize(fontSize);
    }
    
    //テキストを設定する
    public void setText(String text){
        document.setText(text);
        invalidate();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //押された場所を保存する
                touchX = (int)event.getX();
                touchY = (int)event.getY();
                break;
                
            case MotionEvent.ACTION_MOVE:
                int currentX = (int)event.getX();
                int currentY = (int)event.getY();

                //タッチイベントによるスクロール処理
                touchScroll(currentX, currentY);
                
                //現在押されてる場所
                touchX = currentX;
                touchY = currentY;
                invalidate();
                
                break;
        }
        
        return true;
    }
    
    //タッチイベントによるスクロール
    private void touchScroll(int currentX, int currentY){
        int scrollX = viewInfo.getInt(ConstName.SCROLL_X);
        int scrollY = viewInfo.getInt(ConstName.SCROLL_Y);
        
        int dx = (currentX - touchX);
        int dy = (currentY - touchY);
        int adx = Math.abs(dx);
        int ady = Math.abs(dy);
        
        //ブレ対応
        if(adx < ady && adx < EditorView.IGNORE_SCROLL){
            dx = 0;
        }else if(ady < adx && ady < EditorView.IGNORE_SCROLL){
            dy = 0;
        }else if(adx == ady && adx < EditorView.IGNORE_SCROLL){
            //両方向に僅かなスクロールの場合は水平方向をブレとみなす
            dx = 0;
        }
        
        scrollX -= dx;
        scrollY -= dy;
        
        //スクロール位置が0以下にならないようにする
        scrollX = scrollX < 0 ? 0 : scrollX;
        scrollY = scrollY < 0 ? 0 : scrollY;
        
        //スクロール位置が上限を超えないようにする
        scrollX = scrollX < maxScrollX ? scrollX : maxScrollX;
        scrollY = scrollY < maxScrollY ? scrollY : maxScrollY;
        
        //スクロール位置の設定
        viewInfo.put(ConstName.SCROLL_X, scrollX);
        viewInfo.put(ConstName.SCROLL_Y, scrollY);
    }
    
    //ドキュメントが変更されたら呼ばれる
    public void documentChanged(int row, int column, String text){
        int rowCount = document.getLineCount();
        
        //垂直方向のスクロール限界位置を設定する
        maxScrollY = rowCount * fontSize;
        viewInfo.put(ConstName.SCROLL_MAX_Y, maxScrollY);
    }
    
    //ドキュメントが変更されたとき変更された行番号を引数にして呼ばれる
    public void changeLine(int[] lines){
        int start = lines[0];
        int end = lines[lines.length - 1];
        
        for(int row = start; row < end; row++){
            String line = document.getLine(row);
            int lineWidth = (int)paint.measureText(line);

            if(lineWidth > maxScrollX){
                //水平方向のスクロール限界位置
                maxScrollX = lineWidth;
            }
        }
        
        viewInfo.put(ConstName.SCROLL_MAX_X, maxScrollX);
    }
    
    @Override
    protected void onDraw(Canvas canvas){
        viewInfo.put(ConstName.EDITOR_WIDTH, getWidth());
        viewInfo.put(ConstName.EDITOR_HEIGHT, getHeight());
        paint.setTextSize(fontSize);

        drawText(canvas, document.getText());
        rowRuler.drawRuler(canvas, viewInfo, paint);
        horizontalScroll.draw(canvas, viewInfo, paint);
        verticalScroll.draw(canvas, viewInfo, paint);
    }
    
    //テキストを描画する
    private void drawText(Canvas canvas, String text){
        String token;
        TextTokenizer tokenizer = new JavaTokenizer(text.toCharArray());
        DrawPoint point = new DrawPoint();
        
        int startX = rowRuler.getWidth() - viewInfo.getInt(ConstName.SCROLL_X);
        int drawX = startX;
        //描画位置は垂直スクロール位置を引いた場所
        int drawY = fontSize - viewInfo.getInt(ConstName.SCROLL_Y);
        
        //初期位置を設定
        point.drawMove(drawX, drawY);
        
        while((token = tokenizer.getNextToken()) != null){
            if(token.equals("\n")){
                //改行コードが来たら次の行に移動
                point.drawMove(startX, point.getDrawY() + fontSize);
                point.cellMove(point.getColumn(), point.getRow() + 1);
            }else{
                drawString(canvas, token, point);
            }
        }        
    }
    
    //文字列を描画して次の文字列を描画する位置を返す
    private void drawString(Canvas canvas, String text, DrawPoint point){
        int srcColor = paint.getColor();
        
        //渡された文字列がキーワードに登録されてるなら
        //関連付けられた色で描画する
        if(colorMap.containsKey(text)){
            paint.setColor(colorMap.get(text));
        }else{
            paint.setColor(Color.BLACK);
        }
        
        for(char c : text.toCharArray()){
            //一文字ずつ描画する
            drawCharacter(canvas, c, point);
        }
        
        //paintの元の色に戻しておく
        paint.setColor(srcColor);
    }
    
    //一文字描画して次の文字の描画位置を返す
    private void drawCharacter(Canvas canvas, char c, DrawPoint point){
        String drawChar = Character.toString(c);

        //描画位置の取得
        int drawX = point.getDrawX();
        int drawY = point.getDrawY();
        
        //テキストを描画する場合、この文字を描画する
        if(isDrawCharacter(drawX, drawY)){
            canvas.drawText(drawChar, 0, 1, drawX, drawY, paint);
        }
        
        //キャレットの位置ならキャレットを描画する
        if(point.getColumn() == caret.getColumn() &&point.getRow() == caret.getRow()){
            caret.draw(canvas, drawX, drawY, (int)paint.getTextSize(), paint);
        }
        
        point.next(drawChar, paint);
    }
    
    //この文字の位置が描画範囲内かどうか返す
    public boolean isDrawCharacter(int drawX, int drawY){
        int right = getWidth();
        int bottom = getHeight();
        
        if(0 <= drawY && drawY <= bottom){
             if(0 <= drawX && drawX <= right){
                 //描画範囲内
                 return true;
             }
        }
        
        //描画範囲外
        return false;
    }
}
