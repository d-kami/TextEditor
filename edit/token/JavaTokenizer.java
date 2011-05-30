package text.edit.token;

import java.util.Set;
import java.util.HashSet;

public class JavaTokenizer extends TextTokenizer{
    private int index;

    private static final Set<Character> SEPARATOR_SET;
    
    static{
        SEPARATOR_SET = new HashSet<Character>();
        
        SEPARATOR_SET.add('.');
        SEPARATOR_SET.add('(');
        SEPARATOR_SET.add(')');
        SEPARATOR_SET.add('[');
        SEPARATOR_SET.add(']');
        SEPARATOR_SET.add('{');
        SEPARATOR_SET.add('}');
        SEPARATOR_SET.add(' ');
        SEPARATOR_SET.add('\t');
        SEPARATOR_SET.add('\n');
    }
    
    public JavaTokenizer(String text){
        super(text.toCharArray());
    }
    
    public JavaTokenizer(char[] text){
        super(text);
    }
    
    public String getNextToken(){
        String token = null;
        StringBuilder tokenBuilder = new StringBuilder();
        
        while(true){
            if(index >= text.length){
                break;
            }
            
            char tokenc = text[index];
            
            if(SEPARATOR_SET.contains(tokenc)){
                if(tokenBuilder.length() == 0){
                    //区切り文字だけの場合
                    tokenBuilder.append(tokenc);
                    index++;
                    break;
                }else{
                    break;
                }
            }else{
                //区切り文字じゃなければトークンの文字として追加
                tokenBuilder.append(tokenc);
            }
            
            index++;
        }
        
        //トークンがあればStringにして返す
        if(tokenBuilder.length() > 0){
            token = tokenBuilder.toString();
        }
        
        return token;
    }
}
