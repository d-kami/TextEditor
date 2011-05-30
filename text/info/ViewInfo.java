package text.edit.info;

import java.util.Map;
import java.util.HashMap;

public class ViewInfo {
    private Map<String, Object> infoMap;
    
    public ViewInfo(){
        infoMap = new HashMap<String, Object>();
    }
    
    public void put(String key, Object value){
        infoMap.put(key, value);
    }
    
    public Object get(String key){
        return infoMap.get(key);
    }
    
    public Integer getInt(String key){
        return (Integer)infoMap.get(key);
    }
    
    public boolean containsKey(String key){
        return infoMap.containsKey(key);
    }
}
