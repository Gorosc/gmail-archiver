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
* https://developers.google.com/workspace/guides/create-credentials
* https://camel.apache.org/components/latest/google-mail-component.html#_api_parameters_7_apis
