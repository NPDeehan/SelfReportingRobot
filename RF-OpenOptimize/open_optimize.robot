*** Settings ***
Library    CamundaLibrary    ${CAMUNDA_HOST}
Library    Collections
Library    SeleniumLibrary

*** Variables ***
${CAMUNDA_HOST}    http://localhost:8080
${existing_topic}    Demo

*** Test Cases ***
Process workload
    ${variables}    fetch workload   topic=${existing_topic}
    
    # do some processing
    Open Browser  http://localhost:8090/#/
    
    # create result and return workload to Camunda
    ${my_result}    Create Dictionary    lastname=Deehan
    complete task   ${my_result}