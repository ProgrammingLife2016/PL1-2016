Feature: Static Files
    As a web client
    I want to get static files from the server
    So that a web page can be displayed to the client

    Scenario: GET request for an existing file
        Given the server is started
          And the file /statictestfile is available on the server
         When the client requests /static/statictestfile
         Then the client receives the contents of /statictestfile
          And the HTTP status code is 200

    Scenario: GET request for a non-existing file
        Given the server is started
         When the client requests /static/nonexistingstaticfile
         Then the HTTP status code is 404