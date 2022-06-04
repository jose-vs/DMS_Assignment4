<%-- 
    Document   : index.jsp
    Author     : Andrew Ensor
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
   <head>
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
      <title>Mobile Blog App - Server</title>
   </head>
   <body>
      <h1>Mobile Blog App - Server</h1>
      For testing the Server API
      <p>
         <a href='<%= response.encodeURL("test-resbeans.html") %>'>
            Test Server via NetBeans RESTful Web Service tester
         </a>
      </p>
   </body>
</html>
