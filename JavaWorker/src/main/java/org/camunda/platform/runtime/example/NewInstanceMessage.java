package org.camunda.platform.runtime.example;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.stereotype.Component;



@Component
@ExternalTaskSubscription("Start-Instance-Message")  // create a subscription for this topic name
public class NewInstanceMessage implements ExternalTaskHandler {


    @Override
    public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {

        String messageName = "AutomateOptimize";
        String userName = externalTask.getVariable("username");
        String password = externalTask.getVariable("password");
        String processDef= externalTask.getVariable("processDef");
        Long requiredInstanceCount = externalTask.getVariable("requiredInstanceCount");


        Unirest.setTimeouts(0, 0);
        try {

            String body = "{\r\n  \"messageName\":\"" + messageName + "\"\r\n  ,\r\n " +
                    " \"processVariables\": {\r\n    \"username\": {\r\n      \"value\": \"" + userName + "\",\r\n      \"type\": \"String\"\r\n    },\r\n  " +
                    "   \"password\": {\r\n      \"value\": \""+ password +"\",\r\n      \"type\": \"String\"\r\n    " +
                    "}," +
                    "\r\n  " +
                    "   \"requiredInstanceCount\": {\r\n      \"value\": \""+requiredInstanceCount +"\",\r\n      " +
                    "\"type\": \"Long\"\r\n   " +
                    " " +
                    "}," +
                    "\r\n  " +
                    "   \"processDef\": {\r\n      \"value\": \""+processDef +"\",\r\n      \"type\": \"String\"\r\n   " +
                    " " +
                    "}\r\n  }\r\n}";

            System.out.println("Request body: "+ body);

            HttpResponse<String> response = Unirest.post("http://localhost:8080/engine-rest/message")
                    .header("Content-Type", "application/json")
                    .body(body)
                    .asString();

        } catch (UnirestException e) {
            e.printStackTrace();
        }

        externalTaskService.complete(externalTask);

    }
}
