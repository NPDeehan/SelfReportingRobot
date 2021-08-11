*** Settings ***
Library    CamundaLibrary    ${CAMUNDA_HOST}
Library    Collections
Library    SeleniumLibrary

*** Variables ***
${OPTIMIZE_HOST}    http://localhost:8090
${CAMUNDA_HOST}    http://localhost:8080
${optimizeUsername}        demo
${optimizePassword}        demo
${creatNewReport}        http://localhost:8090/#/report/new/edit
${processDef}        Review Invoice
${view}        rawData
${groupBy}        Review Invoice
${distributedBy}        Review Invoice


*** Test Cases ***
Do Optimize Login
    Open Browser  ${OPTIMIZE_HOST}
    Sleep       1s
    Input Username    ${optimizeUsername}
    Input Password    ${optimizePassword}
    Click Login

Create Report
    Sleep       1s
    Create New Report
    Name Report     PondReport
    Select Definition   ${processDef}
    Select View   ${view}
    Save Report

*** Keywords ***
Input Username
    [Arguments]    ${username}
    Input Text     username    ${username}

Input Password
    [Arguments]    ${password}
    Input Text     password    ${password}

Click Login
    Click Button    Log in

Create New Report
    Go To   ${creatNewReport}

Name Report
    [Arguments]    ${reportName}
    Input Text     name    ${reportName}

Save Report
    Click Button    Save

Select Definition
    [Arguments]    ${definitionName}
    Click Button   Add
    Click Element    xpath: //span[contains(text()[normalize-space()],'${definitionName}')]/preceding-sibling::input
    Click Element    xpath: //div[@class="Modal__actions"]//button[contains(text(),"Add")]

Select View
    [Arguments]    ${viewName}
    Click Element    xpath: //div[@class="controlSections"]//span[contains(text(),'View')]//following-sibling::div
    Click Element    xpath: //div[@class="controlSections"]//span[contains(text(),'View')]//following-sibling::div//div[@value='${viewName}']
