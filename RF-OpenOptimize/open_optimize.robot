*** Settings ***
Library    CamundaLibrary    ${CAMUNDA_HOST}
Library    Collections
Library    SeleniumLibrary

*** Variables ***
${OPTIMIZE_HOST}    http://localhost:8090
${CAMUNDA_HOST}    http://localhost:8080
${optimizeUsername}        demo
${optimizePassword}        demo


*** Test Cases ***
Do Optimize Login
    # do some processing
    Open Browser  ${OPTIMIZE_HOST}
    Sleep       3s
    Input Username    ${optimizeUsername}
    Input Password    ${optimizePassword}

*** Keywords ***



    Click Login
    # create result and return workload to Camunda
Input Username
    [Arguments]    ${username}
    Input Text     username    ${username}

Input Password
    [Arguments]    ${password}
    Input Text     password    ${password}

Click Login
    Click Button    Log in