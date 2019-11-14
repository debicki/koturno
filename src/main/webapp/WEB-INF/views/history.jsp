<%--
  Created by IntelliJ IDEA.
  User: sacull
  Date: 10.11.2019
  Time: 10:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="pl_PL">
<head>
    <title>Historia</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link rel="stylesheet" href="/koturno.main.css"/>
    <link rel="stylesheet" href="/bootstrap.min.css"/>
</head>
<body>
<jsp:include page="fragments/main-menu.jsp"/>

<div class="container">

    <div class="row">
        <div class="col-12 text-center koturno-style">
            <a href=/history?filter=all class="btn btn-primary">Wszystkie</a>
            <a href=/history?filter=onlyOffline class="btn btn-primary">Bez niestabilnych</a>
            <a href=/history?filter=noIgnored class="btn btn-primary">Bez ignorowanych</a>
        </div>
    </div>

    <br>

    <div class="row">
        <div class="col-12" style="padding-bottom: 20px">
            <c:if test="${activeInaccessibilityList.size() == 0 && inactiveInaccessibilityList.size() == 0}">
                <p class="h1 text-center koturno-style">Brak wpisów w historii</p>
            </c:if>
            <c:if test="${!(activeInaccessibilityList.size() == 0 && inactiveInaccessibilityList.size() == 0)}">
                <table class="table table-hover table-bordered text-center koturno-style">
                    <thead>
                    <tr class="thead-dark">
                        <th>Lp.</th>
                        <th>Status</th>
                        <th>Nazwa</th>
                        <th>Adres</th>
                        <th colspan="2">Początek</th>
                        <th colspan="2">Koniec</th>
                        <th colspan="2">Akcje</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${activeInaccessibilityList}" var="activeInstability" varStatus="activeInstabilityStatus">
                        <tr>
                            <td>${activeInstabilityStatus.count}</td>
                            <td>Aktywny</td>
                            <td>${activeInstability.host.name}</td>
                            <c:if test="${activeInstability.isOfflineStatus()}">
                                <td class="table-danger">
                                    <a href=/host?id=${activeInstability.host.id}&action=info>
                                            ${activeInstability.host.address}
                                    </a>
                                </td>
                            </c:if>
                            <c:if test="${!activeInstability.isOfflineStatus()}">
                                <td class="table-warning">
                                    <a href=/host?id=${activeInstability.host.id}&action=info>
                                            ${activeInstability.host.address}
                                    </a>
                                </td>
                            </c:if>
                            <td>${activeInstability.dayOfBegin}</td>
                            <td>${activeInstability.hourOfBegin}</td>
                            <td colspan="2">TRWA</td>
                            <td>
                                <a href=/inaccessibility?id=${activeInstability.id}&action=info>
                                    zobacz
                                </a>
                            </td>
                            <td>
                                <a href=/inaccessibility?id=${activeInstability.id}&action=remove>
                                    usuń
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:forEach items="${inactiveInaccessibilityList}" var="inactiveInstability" varStatus="inactiveInstabilityStatus">
                        <tr>
                            <td>${inactiveInstabilityStatus.count + activeInaccessibilityList.size()}</td>
                            <td>Archiwalny</td>
                            <td>${inactiveInstability.host.name}</td>
                            <td class="table-secondary">
                                <a href=/host?id=${inactiveInstability.host.id}&action=info>
                                        ${inactiveInstability.host.address}
                                </a>
                            </td>
                            <td>${inactiveInstability.dayOfBegin}</td>
                            <td>${inactiveInstability.hourOfBegin}</td>
                            <c:if test="${inactiveInstability.start == inactiveInstability.end}">
                                <td colspan="2">ZIGNOROWANY</td>
                            </c:if>
                            <c:if test="${inactiveInstability.start != inactiveInstability.end}">
                                <td>${inactiveInstability.dayOfEnd}</td>
                                <td>${inactiveInstability.hourOfEnd}</td>
                            </c:if>
                            <td>
                                <a href=/inaccessibility?id=${inactiveInstability.id}&action=info>
                                    zobacz
                                </a>
                            </td>
                            <td>
                                <a href=/inaccessibility?id=${inactiveInstability.id}&action=remove>
                                    usuń
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

<script src="/jquery-3.3.1.slim.min.js"></script>
<script src="/popper.min.js"></script>
<script src="/bootstrap.min.js"></script>
</body>
</html>
