/*
 * Copyright Camunda Services GmbH and/or licensed to Camunda Services GmbH
 * under one or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information regarding copyright
 * ownership. Camunda licenses this file to you under the Apache License,
 * Version 2.0; you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.platform.runtime.example;

import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@ExternalTaskSubscription("check-login") // create a subscription for this topic name
public class CheckLoginDetails implements ExternalTaskHandler {

  @Override
  public void execute(ExternalTask externalTask, ExternalTaskService externalTaskService) {
    // get variables
//    String customerId = externalTask.getVariable("customerId");
//    int creditScore = externalTask.getVariable("creditScore");
    String s = null;
    String fullLog = null;
    Boolean loginOK;

    Map<String, Object> vars = new HashMap<String, Object>();

    try {
      String command = "python -m " +
              "robot " +
              " --variable optimizeUsername:"+ externalTask.getVariable("username") +
              " --variable optimizePassword:"+ externalTask.getVariable("password") +
              " --outputdir .\\target\\Robot\\" +
              " .\\src\\main\\resources\\robots\\open_optimize.robot";

      System.out.println("Running: " + command);

      Process p = Runtime.getRuntime().exec(command);
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
      externalTaskService.handleBpmnError(externalTask, "LoginFailed");
    }else{
      vars.put("loginOK", true);
      externalTaskService.complete(externalTask, vars);
    }
    //

  }

}
