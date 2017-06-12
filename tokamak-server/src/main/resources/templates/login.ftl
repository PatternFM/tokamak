<!doctype html>

<html>

  <head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link href="https://fonts.googleapis.com/css?family=Gruppo|Khand|Unica+One|Lato|Roboto|Raleway" rel="stylesheet"> 
    <title>Sign In to Tokamak</title>
    <link rel="stylesheet" href="tokamak.css"/>
  </head>
  
  <body>
    <form id="login-form" action="login" method="post">
      <div id="login-title" style="margin-bottom: 40px;">Sign In to Tokamak</div>
      <input id="username" class="login-textfield" type="text" name="username" placeholder="username" />
      <input id="password" class="login-textfield" type="password" name="password" placeholder="password" />
      <input type="hidden" id="csrf_token" name="${_csrf.parameterName}" value="${_csrf.token}"/>
      <br/>
      <input type="submit" name="login" value="Sign In" />
      
    <#if RequestParameters['error']??>
      <div class="alert alert-danger">
        There was a problem logging in. Please try again.
      </div>
    </#if>      
    </form>
  </body>
  
</html>