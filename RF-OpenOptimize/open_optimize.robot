*** Settings ***
Library    CamundaLibrary    ${CAMUNDA_HOST}
Library    Collections
Library    SeleniumLibrary

*** Variables ***
${OPTIMIZE_HOST}    http://localhost:8090
${CAMUNDA_HOST}    http://localhost:8080
${optimizeUsername}        demo
${optimizePassword}        demo
${existing_topic}    login


*** Test Cases ***
Do Optimize Login
    ${variables}    fetch workload   topic=${existing_topic}
    ${recent_task}    Get fetch response
    log    Recent task:\t${recent_task}

    Pass Execution If    not ${recent_task}    No workload fetched from Camunda

    # do some processing
    Open Browser  ${OPTIMIZE_HOST}
    Sleep       3s
    Input Username    ${optimizeUsername}
    Input Password    ${optimizePassword}
    Click Login

    ${my_result}    Create Dictionary    lastname=Deehan
    complete task   ${my_result}

*** Keywords ***

Input Username
    [Arguments]    ${username}
    Input Text     username    ${username}

Input Password
    [Arguments]    ${password}
    Input Text     password    ${password}

Click Login
    Click Button    Log in