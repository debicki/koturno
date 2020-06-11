<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<fmt:setBundle basename="lang"/>

<html lang="pl_PL">
<head>
    <title><fmt:message key="head.title.login"/></title>
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
                <div class="col-1"></div>
                <div class="col-6" style="min-height: 5rem">
                    <c:if test="${param['error'] != null}">
                        <div class="alert koturno-alert-danger alert-dismissible fade show" role="alert">
                            <strong>
                                <fmt:message key="messages.title.error"/>
                            </strong> <fmt:message key="messages.error.incorrect-credentials"/>
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    </c:if>
                </div>
                <div class="col-5"></div>
            </div>

            <div class="row mb-4">
                <div class="col-1"></div>
                <div class="col-6 pb-1 koturno-style border border-secondary rounded koturno-darkest text-white koturno-title-display">
                    <fmt:message key="body.label.login"/>
                </div>
                <div class="col-5"></div>
            </div>

            <div class="row">
                <div class="col-1"></div>
                <div class="col-6">
                    <form method="post" action="/login" class="koturno-style">
                        <div class="form-group">
                            <label for="username"><fmt:message key="body.label.username"/></label>
                            <fmt:message key="form.placeholder.provide-username" var="provideUsername"/>
                            <input type="text" required name="username" id="username" class="form-control koturno-dark text-light"
                                   placeholder="${provideUsername}" autofocus="autofocus" autocomplete="off"/>
                        </div>
                        <div class="form-group">
                            <label for="password"><fmt:message key="body.label.password"/></label>
                            <fmt:message key="form.placeholder.provide-password" var="providePassword"/>
                            <input type="password" required name="password" id="password" class="form-control koturno-dark text-light"
                                   placeholder="${providePassword}"/>
                        </div>
                        <button class="btn btn-outline-light" type="submit"><fmt:message key="security.button.login"/></button>
                        <button class="btn btn-outline-secondary" type="reset"><fmt:message key="security.button.clear"/></button>
                        <sec:csrfInput/>
                    </form>
                </div>
                <div class="col-5"></div>
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
