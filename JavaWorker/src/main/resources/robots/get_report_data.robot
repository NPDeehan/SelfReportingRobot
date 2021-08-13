*** Settings ***
Library    Collections
Library    SeleniumLibrary

*** Variables ***
${OPTIMIZE_HOST}    http://localhost:8090
${reportURL}    http://localhost:8090/external/#/share/report/0c8ddc87-021c-4fa2-8024-d0bf6c80ff35
${optimizeUsername}        demo
${optimizePassword}        demo

*** Test Cases ***
Open Report
    Open Browser  ${reportURL}
    Sleep       1s

Go To Optimize
    Click Element    xpath: //span[contains(text(),'Open')]
    Sleep   1s
    Switch Window   NEW

Do Optimize Login
    Sleep   2s
    Input Username    ${optimizeUsername}
    Input Password    ${optimizePassword}
    Click Login

#Close The Browser
    #Close Browser

*** Keywords ***
Input Username
    [Arguments]    ${username}
    Input Text     username    ${username}

Input Password
    [Arguments]    ${password}
    Input Text     password    ${password}

Click Login
    Click Button    Log in