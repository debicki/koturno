<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html lang="pl_PL">
<head>
    <title>Logowanie</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="theme-color" content="blue">
    <link rel="stylesheet" href="/koturno.main.css"/>
    <link rel="stylesheet" href="/bootstrap.min.css"/>
</head>
<body>
<jsp:include page="fragments/main-menu.jsp"/>

<div class="container">

    <div class="row">
        <div class="col-3"></div>
        <div class="col-6">
            <c:if test="${param['error'] != null}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    Błędne dane logowania!
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </c:if>
        </div>
        <div class="col-3"></div>
    </div>

    <div class="row mt-2 mb-4">
        <div class="col-3"></div>
        <div class="col-6 koturno-style text-center border border-secondary rounded bg-secondary text-white">
            <h2>Logowanie</h2>
        </div>
        <div class="col-3"></div>
    </div>

    <div class="row">
        <div class="col-3"></div>
        <div class="col-6 text-center">
            <form method="post" action="/login" class="koturno-style">
                <div class="form-group">
                    <label for="username">Nazwa użytkownika</label>
                    <input type="text" required name="username" id="username" class="form-control text-center"
                           placeholder="Podaj nazwę użytkownika" autofocus="autofocus" autocomplete="off"/>
                </div>
                <div class="form-group">
                    <label for="password">Hasło</label>
                    <input type="password" required name="password" id="password" class="form-control text-center"
                           placeholder="Podaj hasło"/>
                </div>
                <button class="btn btn-primary" type="submit">Zaloguj</button>
                <button class="btn btn-secondary" type="reset">Wyczyść dane</button>
                <sec:csrfInput/>
            </form>
        </div>
        <div class="col-3"></div>
    </div>

</div>

<script src="/jquery-3.3.1.slim.min.js"></script>
<script src="/popper.min.js"></script>
<script src="/bootstrap.min.js"></script>
</body>
</html>
