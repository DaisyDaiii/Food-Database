package project.posts;

import java.util.HashMap;
import java.util.Map;

public class Personalizations {
//    private Map<String, String> toEmail = new HashMap<String, String>();
//    private Map<String, String> from = new HashMap<String, String>();
//    private String subject;
//    private Map<String, String> con = new HashMap<String, String>();
//    private Map[] content = new Map[1];
    private Map[] to = new Map[1];

    public Personalizations(String target){
//        this.from.put("email", "ydai8556@uni.sydney.edu.au");
//        this.toEmail.put("email", target);

        Map<String, String> toEmail = new HashMap<String, String>();
        toEmail.put("email", target);
        this.to[0] = toEmail;

//        Map<String, String> con = new HashMap<String, String>();
//        con.put("type", "text/plain");
//        con.put("value", content);
//        this.content[0] = con;
//
//        this.subject ="Report: your selected ingredients";
    }
}
