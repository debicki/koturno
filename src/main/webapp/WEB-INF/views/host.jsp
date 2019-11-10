<%--
  Created by IntelliJ IDEA.
  User: sacull
  Date: 10.11.2019
  Time: 12:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="pl_PL">
<head>
    <title>Szczegóły hosta</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" integrity="sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB" crossorigin="anonymous">
</head>
<body>
<jsp:include page="fragments/main-menu.jsp"/>

<div class="container">

    <div class="row">
        <div class="col-12 text-center" style="color: black; font-variant: small-caps;">
            <div class="h1">${host.name}</div>
            <div class="h2">${host.address}</div>
            <div class="lead"><strong>Grupa: ${host.hostGroup.name}</strong></div>
            <div class="lead"><strong>Dodany dnia: ${host.dayWhenCreated}</strong></div>
            <c:if test="${host.isActive()}">
                <div class="lead"><strong>Status: aktywny</strong></div>
            </c:if>
            <c:if test="${!host.isActive()}">
                <div class="lead"><strong>Status: nieaktywny</strong></div>
            </c:if>
            <div class="lead"><strong>Opis: ${host.description}</strong></div>
        </div>
    </div>
    <div class="row">
        <div class="col-12 text-center">

        </div>
    </div>
    <div class="row">
        <div class="col-12" style="padding-bottom: 20px">
            <table class="table table-hover table-bordered" style="color: black; font-variant: small-caps; text-align: center;">
                <thead>
                <tr class="thead-dark">
                    <th>Lp.</th>
                    <th colspan="2">Początek</th>
                    <th colspan="2">Koniec</th>
                    <th>Opis</th>
                    <th>Akcje</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${inaccessibilityList}" var="inaccessibility" varStatus="inaccessibilityStatus">
                    <tr>
                        <td>${inaccessibilityStatus.count}</td>
                        <td>${inaccessibility.dayOfBegin}</td>
                        <td>${inaccessibility.hourOfBegin}</td>
                        <td>${inaccessibility.dayOfEnd}</td>
                        <td>${inaccessibility.hourOfEnd}</td>
                        <td>${inaccessibility.description}</td>
                        <td>
                            <a href=/inaccessibility?id=${inaccessibility.id}&action=info>
                                zobacz
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

</div>

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js" integrity="sha384-smHYKdLADwkXOn1EmN1qk/HfnUcbVRZyYmZ4qpPea6sjB/pTJ0euyQp0Mk8ck+5T" crossorigin="anonymous"></script>
</body>
</html>
