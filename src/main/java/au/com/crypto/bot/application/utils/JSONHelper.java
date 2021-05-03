package au.com.crypto.bot.application.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/*
 *  An abstract class for a logical organization of the following
 *  two functions.
 */
public class JSONHelper {


    public static Map<String, Object> toMap(JSONObject object){
        Map<String, Object> mapping = new HashMap<String, Object>();

        //These iterators are like gimped up shadows of what iterators should be.
        @SuppressWarnings("rawtypes")
        Iterator keys = object.keys();

        //Java's naming conventions are horrible. This will in fact get the
        //the first key, and won't skip anything.
        while (keys.hasNext()) {
            String key = (String) keys.next();
            Object item = object.opt(key);
            if (item instanceof JSONObject){

                //Recursive call if we come across a JSONObject inside.
                mapping.put(key, toMap((JSONObject) item));
            }
            else if (item instanceof JSONArray){
                mapping.put(key, toVector((JSONArray) item));
            }

            else{
                mapping.put(key, object.opt(key));
            }
        }
        return mapping;
    }

    /*
     * Description: Same as above except for JSONArrays.
     */
    public static Vector<Object> toVector(JSONArray object){
        Vector<Object> array = new Vector<Object>();
        int object_len = object.length();
        for (int i = 0; i != object_len; ++i){
            Object item = object.opt(i);
            if (item instanceof JSONArray){
                array.add(toVector((JSONArray) item));
            }
            else if(item instanceof JSONObject){
                array.add(toMap((JSONObject) item));
            }
            else{
                array.add(item);
            }
        }
        return array;
    }

    /**
     * unused as of now
     * @param obj
     * @return
     */
    public static String convertObjectToJson(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json = mapper.writeValueAsString(obj);
            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * unused as of now
     * @param json
     * @return
     */
    public static String jsonToHash(String json) {
        return DigestUtils
                .md5Hex(json.replaceAll("[/\\s+/\\r\\n\\t]", "").toUpperCase());
    }
}
