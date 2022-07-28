package project.posts;

import java.util.HashMap;
import java.util.Map;

public class EmailBody {
    private Personalizations[] personalizations = new Personalizations[1];

    private String subject;
    private Map[] content = new Map[1];
    private Map<String, String> from = new HashMap<String, String>();

    public EmailBody(Personalizations personalizations, String content, String from){
        this.personalizations[0] = personalizations;

        this.from.put("email", from);
        Map<String, String> con = new HashMap<String, String>();
        con.put("type", "text/plain");
        con.put("value", content);
        this.content[0] = con;

        this.subject ="Report: your selected ingredients";
    }
}
