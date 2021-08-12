*** Settings ***
Library    Collections
Library    AutoRecorder
Library    SeleniumLibrary

*** Variables ***
${OPTIMIZE_HOST}    http://localhost:8090
${optimizeUsername}        demo
${optimizePassword}        d


*** Test Cases ***
Do Optimize Login

    # do some processing
    Open Browser  ${OPTIMIZE_HOST}  FireFox
    Sleep       3s
    Input Username    ${optimizeUsername}   
    Input Password    ${optimizePassword}
    Click Login
    Sleep   2s
    ${title} =  Get Title
    Log To Console  Title of the Page is ${title}
    Page Should Not Contain   Could not authenticate you
    

Close The Browser
    Close Browser

*** Keywords ***

Input Username
    [Arguments]    ${username}
    Input Text     username    ${username}

Input Password
    [Arguments]    ${password}
    Input Text     password    ${password}

Click Login
    Click Button    Log in