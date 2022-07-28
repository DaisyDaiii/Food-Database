package project.model;

import project.posts.Post;
import java.util.List;

/**
 * RequestOutputOnline implements RequestOutput interface
 * The class overrides all the methods that in interface
 * The class will return the results of output API
 */
public class RequestOutputOnline implements RequestOutput {
    private RequestInputOnline requestInputOnline;

    public RequestOutputOnline(RequestInputOnline online){
        this.requestInputOnline = online;
    }

    /**
     * @param number The number of meals of current mode
     * @return string of the email content
     */
    public String content(int number){
        String content = "";

        List<SelectedFood> added = requestInputOnline.getFoodList();
        if(added.size() == 0){
            content+="You didn't add any ingredient";
        }else{
            content+="Total calories: "+requestInputOnline.getCalories()*number+"\n";
            content+="\n";

            for(SelectedFood i: added){
                content+="Food: "+i.getFood().getLabel()+"\n"
                        +"Measure: "+i.getMeasure().getLabel()+"\n"
                        +"Quantity: "+i.getNum()*number+"\n";
                content+="\n";
            }
        }

        return content;
    }

    /**
     * send email which contains the information of added foods
     * @param target string of output receiver email address
     * @param number The number of meals of current mode
     * @return string of whether the email has been sent successfully
     */
    @Override
    public String sendEmail(String target, int number) {
        Post post =  requestInputOnline.getHttp().getPost(requestInputOnline.getHttp().emailRequest(System.getenv("SENDGRID_API_EMAIL"), target,
                System.getenv("SENDGRID_API_KEY"),
                content(number)));
        if(post != null && post.getErrors() != null){
            return "Error: "+post.getErrors()[0].getMessage();
        }
        return "Send Successfully";
    }
}
