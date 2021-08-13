*** Settings ***
Library    Collections
Library    SeleniumLibrary

*** Variables ***
${OPTIMIZE_HOST}    http://localhost:8090
${reportURL}    http://localhost:8090/#/report/new/edit

*** Test Cases ***
Open Report
    Open Browser  ${reportURL}
    Sleep       1s

Go To Optimize
    Click Button    css=html.js-focus-visible body div#root div.Root-container div.Sharing.report div.header div.title-container a.Button.title-button.main
    Sleep   1s
    Click Element   Edit
    Click Element    Flow Nodes

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