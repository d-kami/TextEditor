package text.edit.document;

import java.util.List;
import java.util.ArrayList;

public class TextDocument {
    //テキスト
    private final List<StringBuilder> textList;
    //イベントハンドラ
    private final List<DocumentChangeHandler> handlers;
    
    public TextDocument(){
        textList = new ArrayList<StringBuilder>();
        textList.add(new StringBuilder());
        
        handlers = new ArrayList<DocumentChangeHandler>();
    }
    
    public void setText(String text){
        for(char c : text.toCharArray()){
            append(c);
        }
        
        int[] lines = new int[getLineCount()];
        
        for(int i = 0; i < lines.length; i++){
            lines[i] = i;
        }
        
        fireDocumentChanged(0, 0, text);
        fireChangeLine(lines);
    }
    
    public String getText(){
        StringBuilder text = new StringBuilder(textList.size() * 10);
        
        for(StringBuilder line : textList){
            text.append(line).append('\n');
        }
        
        return text.substring(0, text.length() - 1);
    }

    public String getLine(int index){
        return textList.get(index).toString();
    }
    
    public int getLineCount(){
        return textList.size();
    }
    
    public void append(char c){
        if(c == '\n'){
            textList.add(new StringBuilder());
            return;
        }
        
        textList.get(textList.size() - 1).append(c);
    }
    
    public void addDocumentChangeHandler(DocumentChangeHandler handler){
        handlers.add(handler);
    }
    
    public void fireDocumentChanged(int row, int column, String text){
        for(DocumentChangeHandler handler : handlers){
            handler.documentChanged(row, column, text);
        }
    }
    
    public void fireChangeLine(int[] lines){
        for(DocumentChangeHandler handler : handlers){
            handler.changeLine(lines);
        }
    }
}
