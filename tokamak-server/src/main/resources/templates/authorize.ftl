<!doctype html>

<html>

  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link href="https://fonts.googleapis.com/css?family=Gruppo|Khand|Unica+One|Lato|Roboto|Raleway" rel="stylesheet"> 
    <title>Tokamak Sign In</title>
    <link rel="stylesheet" href="../tokamak.css"/>
  </head>
  
  <body>
    <form id="login-form" action="../oauth/authorize" method="post">
      <div id="login-title">Authorize Appliation</div>
      <p>Client "${authorizationRequest.clientId}" would like permission to access your account.</p>
      
      <input name="user_oauth_approval" value="true" type="hidden" />
      <input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      
      <#list authorizationRequest.scope as scope>
          <input type="hidden" name="scope.${scope}" value="true" />
      </#list>
      
      <input type="submit" name="authorize" value="Authorize" /> 
    </form>
  </body>
  
</html>
