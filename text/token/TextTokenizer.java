package text.edit.token;

public abstract class TextTokenizer {
    protected final char[] text;
    protected int index;
    
    protected TextTokenizer(char[] text){
        this.text = text;
        index = 0;
    }
    
    public abstract String getNextToken();
}
