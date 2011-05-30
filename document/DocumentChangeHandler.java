package text.edit.document;

public interface DocumentChangeHandler {
    //ドキュメントが変更されたら、変更位置と変更後のテキストを渡す
    void documentChanged(int row, int column, String text);
    //ドキュメントが変更されたら、変更された行番号を渡す
    void changeLine(int[] lines);
}
