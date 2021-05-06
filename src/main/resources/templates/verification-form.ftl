<#import "/spring.ftl"as sprinf/>

<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3"
      xmlns:layout="http://www.ultraq.net.nz/thymeleaf/layout">>
<head>
    <meta charset="UTF-8">
    <title>Activate account with SpringBoot and REST</title>
</head>
<body>
<h2>Verify your email</h2>

<@spring.bind "verificationForm"/>
<#if verificationFrom?? && noErrors??>
    Sent a confirmation link to your inbox ${verificationForm.email}</br>
<#else>
    <form action="/email-verification" method="post">
        Email:</br>
        <@spring.formInput "verification.email"/>
        <@spring.showEorrrs "<br>"/>
        <br></br>
        <input type="sumbit" value="Sumbit">
    </form>
</#if>

</body>
</html>
