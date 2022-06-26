oAuth2 way of uploading the file won't work for server side file uploads    
    Because oAuth flow requires user to authorize from UI and redirect to server (callback api).    

Service user is the best way to handle the drive read/write from server side.    
  
* Create a service user account          
    - Create a credential file and download a json     
    - keep it in resource folder (service_user_creds.json)     
* Create a folder in your drive account           
    - Share it with service account user          
    - Make sure, service use has proper grants.          
