package org.camunda.platform.runtime.example;


import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
@ExternalTaskSubscription("Create-Report") // create a subscription for this topic name
public class CreateReport  implements ExternalTaskHandler {

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

        String s = null;
        String fullLog = null;
        Boolean reportCreated;

        Map<String, Object> vars = new HashMap<String, Object>();
        // we could call an external service to create the loan documents here
        try {


            Process p = Runtime.getRuntime().exec(
                    "python -m " +
                            "robot " +
                            " --variable optimizeUsername:"+ externalTask.getVariable("username") +
                            " --variable optimizePassword:"+ externalTask.getVariable("password") +
                            " --variable processDef:\""+ externalTask.getVariable("processDef") + "\"" +
                            " --outputdir .\\target\\Robot\\" +
                            " .\\src\\main\\resources\\robots\\create_report.robot"
                            //" C:\\Users\\Niall\\OneDrive\\Documents\\GitHub\\SelfReportingRobot\\RF-OpenOptimize\\create_report.robot"
            );
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(p.getInputStream()));

            BufferedReader stdError = new BufferedReader(new
                    InputStreamReader(p.getErrorStream()));

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
                fullLog = fullLog + s;
            }
            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
                fullLog = fullLog + s;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }



        // complete the external task
        if(fullLog.contains("FAIL"))
        {
            vars.put("reportCreated", false);
            externalTaskService.handleFailure(externalTask, "Failed to Create Report", "Baadddd things happened", 0, 0);

        }else if (fullLog.contains("PASS")){
            reportCreated = true;
            int linkStart = fullLog.indexOf("OptLink");
            int linkEnd = fullLog.lastIndexOf("OptLink");
            String optLink = fullLog.substring(linkStart,linkEnd);
            optLink = optLink.replace("OptLink","");

            vars.put("OptLink", optLink);
            vars.put("reportCreated", reportCreated);
        }else {
            externalTaskService.handleFailure(externalTask, "Problem", "I dont know how to fix it... ", 0, 0);
        }
        externalTaskService.complete(externalTask, vars);


    }
}
