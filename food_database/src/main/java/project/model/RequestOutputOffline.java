package project.model;

/**
 * RequestOutputOffline implements RequestOutput interface
 * The class overrides all the methods that in interface
 * The class will return the dummy result default
 */
public class RequestOutputOffline implements RequestOutput {
    /**
     * send email which contains the information of added foods
     * @param target string of output receiver email address
     * @param number The number of meals of current mode
     * @return string of whether the email has been sent successfully
     */
    @Override
    public String sendEmail(String target, int number) {
        return "Send Successfully"+"\n"+"The validation of email is checked by API, thus dummy will not show the email error";
    }
}
