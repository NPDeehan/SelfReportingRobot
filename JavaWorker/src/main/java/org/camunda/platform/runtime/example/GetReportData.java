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
@ExternalTaskSubscription("Get-Report-Data")  // create a subscription for this topic name
public class GetReportData implements ExternalTaskHandler {

    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {


        String s = null;
        String fullLog = null;
        boolean reportDataRetrieved;

        Map<String, Object> vars = new HashMap<String, Object>();
        // we could call an external service to create the loan documents here
        try {


            Process p = Runtime.getRuntime().exec(
                    "python -m " +
                            "robot " +
                            " --variable optimizeUsername:"+ externalTask.getVariable("username") +
                            " --variable optimizePassword:"+ externalTask.getVariable("password") +
                            " --variable reportURL:\""+ externalTask.getVariable("OptLink") + "\"" +
                            " --outputdir .\\target\\Robot\\" +
                            " .\\src\\main\\resources\\robots\\get_report_data.robot"

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
        assert fullLog != null;
        if(fullLog.contains("FAIL"))
        {
            vars.put("reportDataRetrieved", false);
            externalTaskService.handleFailure(externalTask, "Failed to Get Data from report", "Baadddd things happened", 0, 0);
            externalTaskService.complete(externalTask, vars);
        }else if (fullLog.contains("PASS")){
            reportDataRetrieved = true;
            int linkStart = fullLog.indexOf("OptResult");
            int linkEnd = fullLog.lastIndexOf("OptResult");
            String numberOfInstances = fullLog.substring(linkStart,linkEnd);
            numberOfInstances = numberOfInstances.replace("OptResult","");

            vars.put("numberOfInstances", Long.valueOf(numberOfInstances));
            vars.put("reportDataRetrieved", reportDataRetrieved);
            externalTaskService.complete(externalTask, vars);
        }else {
            externalTaskService.handleFailure(externalTask, "Problem", "I dont know how to fix it... ", 0, 0);
        }


    }
}

