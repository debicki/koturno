<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html lang="pl_PL">
<head>
    <title>Podgląd</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link rel="stylesheet" href="/koturno.main.css"/>
    <link rel="stylesheet" href="/bootstrap.min.css"/>
</head>
<body>
<jsp:include page="fragments/main-menu.jsp"/>

<div class="container">
<sec:authorize access="isAuthenticated()">
    <div class="row">
        <div class="col-12" style="padding-bottom: 20px">
            <c:if test="${instabilityHosts.size() == 0}">
                <p class="h1 text-center koturno-style">Wszystkie hosty są ONLINE</p>
            </c:if>
            <c:if test="${instabilityHosts.size() > 0}">
                <table class="table table-hover table-bordered text-center koturno-style">
                    <thead>
                        <tr class="thead-dark">
                            <th>Lp.</th>
                            <th>Nazwa</th>
                            <th>Adres</th>
                            <th>Ostatnio online</th>
                            <th>Opis</th>
                            <th colspan="3">Akcje</th>
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
                                <c:if test="${!instabilityHost.isOfflineStatus()}">
                                    <td colspan="2">
                                        <a href=/inaccessibility?id=${instabilityHost.id}&action=info>
                                            zobacz
                                        </a>
                                    </td>
                                    <td>
                                        <a href="#" onclick="window.open('/ping?address=${instabilityHost.host.address}','_blank');return false">
                                            ping
                                        </a>
                                    </td>
                                </c:if>
                                <c:if test="${instabilityHost.isOfflineStatus()}">
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
                                    <td>
                                        <a href="#" onclick="window.open('/ping?address=${instabilityHost.host.address}','_blank');return false">
                                            ping
                                        </a>
                                    </td>
                                </c:if>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </div>
    </div>
</sec:authorize>
</div>

<script src="/koturno.reloader.js"></script>
<script src="/jquery-3.3.1.slim.min.js"></script>
<script src="/popper.min.js"></script>
<script src="/bootstrap.min.js"></script>
</body>
</html>
