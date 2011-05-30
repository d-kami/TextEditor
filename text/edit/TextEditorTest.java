package text.edit;

import android.app.Activity;
import android.os.Bundle;

import text.edit.view.EditorView;

public class TextEditorTest extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        EditorView editor = new EditorView(this);

        StringBuilder text = new StringBuilder();
        text.append("import java.util.List;\n\n");
        text.append("public class Main{\n");
        text.append("    public static void main(String[] args){\n");
        text.append("    }\n");
        text.append("}");
        
        editor.setText(text.toString());
        
        setContentView(editor);
    }
}