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
${reportName}        PondReport
${view}        rawData
${groupBy}        TODO
${distributedBy}        TODO


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
    Name Report     ${reportName}
    Sleep       1s
    Select Definition   ${processDef}
    Sleep       1s
    Select View   ${view}
    Save Report

Log Share Link
    Enable Sharing
    Log Share Link

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

Enable Sharing
    Click Button    Share
    Click Element   xpath: //span[contains(text(),'sharing')]//preceding-sibling::span

Log Share Link
    ${shareLink}=   Get Text    xpath: //input[contains(concat(' ',normalize-space(@class),' '),' linkText ')]
    Log    ${shareLink}     WARN
