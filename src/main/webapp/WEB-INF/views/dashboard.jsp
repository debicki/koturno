<%--
  Created by IntelliJ IDEA.
  User: sacull
  Date: 08.11.2019
  Time: 21:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="pl_PL">
<head>
    <title>Podgląd</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css" integrity="sha384-WskhaSGFgHYWDcbwN70/dfYBj47jz9qbsMId/iRN3ewGhXQFZCSftd1LZCfmhktB" crossorigin="anonymous">
</head>
<body>
<jsp:include page="fragments/main-menu.jsp"/>

<div class="container">

    <div class="row">
        <div class="col-12" style="padding-bottom: 20px">
            <c:if test="${instabilityHosts.size() == 0}">
                <p class="h1" style="color: black; font-variant: small-caps; text-align: center;">Wszystkie hosty są ONLINE</p>
            </c:if>
            <c:if test="${instabilityHosts.size() > 0}">
                <table class="table table-hover table-bordered" style="color: black; font-variant: small-caps; text-align: center;">
                    <thead>
                        <tr class="thead-dark">
                            <th>Lp.</th>
                            <th>Nazwa</th>
                            <th>Adres</th>
                            <th>Ostatnio online</th>
                            <th>Opis</th>
                            <th colspan="2">Akcje</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${instabilityHosts}" var="instabilityHost" varStatus="instabilityHostStatus">
                            <tr>
                                <td>${instabilityHostStatus.count}</td>
                                <td>${instabilityHost.host.name}</td>
                                <c:if test="${instabilityHost.isOfflineStatus()}">
                                    <td class="table-danger">
                                        <a href=/host?id=${instabilityHost.host.id}&action=info>
                                            ${instabilityHost.host.address}
                                        </a>
                                    </td>
                                </c:if>
                                <c:if test="${!instabilityHost.isOfflineStatus()}">
                                    <td class="table-warning">
                                        <a href=/host?id=${instabilityHost.host.id}&action=info>
                                            ${instabilityHost.host.address}
                                        </a>
                                    </td>
                                </c:if>
                                <td>${instabilityHost.hourOfBegin}</td>
                                <td>${instabilityHost.host.description}</td>
                                <td>
                                    <a href=/inaccessibility?id=${instabilityHost.id}&action=info>
                                        zobacz
                                    </a>
                                </td>
                                <td>
                                    <a href=/inaccessibility?id=${instabilityHost.id}&action=ignore>
                                        ignoruj
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </div>
    </div>

</div>

<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.1/js/bootstrap.min.js" integrity="sha384-smHYKdLADwkXOn1EmN1qk/HfnUcbVRZyYmZ4qpPea6sjB/pTJ0euyQp0Mk8ck+5T" crossorigin="anonymous"></script>
</body>
</html>
