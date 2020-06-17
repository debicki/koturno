<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<fmt:setBundle basename="lang"/>

<html lang="pl_PL">
<head>
    <title><fmt:message key="head.title.ping"/></title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="theme-color" content="blue">
    <link rel="stylesheet" href="/koturno.main.css"/>
    <link rel="stylesheet" href="/bootstrap.min.css"/>
</head>
<body>

<jsp:include page="fragments/title-bar.jsp"/>

<div class="container-fluid">
    <div class="row min-vh-100">

        <jsp:include page="fragments/left-menu.jsp"/>

        <div class="col-10 koturno-dark text-light">

            <div class="row mt-4">
                <div class="col-4 pb-1 koturno-style">
                <h1><span class="badge text-light koturno-darkest border border-secondary">
                    <fmt:message key="head.title.ping"/>
                </span></h1>
                </div>
                <div class="col-8"></div>
            </div>

            <div class="row mt-4 ml-2">
                ${result}
            </div>
        </div>
    </div>
</div>

<jsp:include page="fragments/footer-bar.jsp"/>

<script src="/jquery-3.3.1.slim.min.js"></script>
<script src="/popper.min.js"></script>
<script src="/bootstrap.min.js"></script>
</body>
</html>
