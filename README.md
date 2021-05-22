# Gmail Archiver

The purpose of this application is to save Google Account space by downloading and deleting archived emails.

* connect to a Gmail account
* parse all archived emails (labeled not as inbox)
* download them 
  * filesystem (local) as eml files
    
* delete them from Gmail (explicit flag)

## Implementation details
Camel integration and the Google mail component consists the core the application

## References
* https://developers.google.com/gmail/api/quickstart/js
* https://medium.com/automationmaster/getting-google-oauth-access-token-using-google-apis-18b2ba11a11a
* https://camel.apache.org/components/latest/google-mail-component.html#_api_parameters_7_apis

## How to obtain a token (until automatic solution)
* Follow the steps in the quickstart guide above
* Open Network in the browser
* Sign out & Authorize
* The access token can be found in the response of https://accounts.google.com/o/oauth2/iframerpc 
