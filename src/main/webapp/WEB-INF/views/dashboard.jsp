<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html lang="pl_PL">
<head>
    <title>Podgląd</title>
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
        <div class="col-12 pb-3">
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
                            <td class="align-middle">${instabilityHostStatus.count}</td>
                            <td class="align-middle">${instabilityHost.host.name}</td>
                            <c:if test="${instabilityHost.isOfflineStatus()}">
                                <td class="table-danger align-middle">
                                    <a href=/host?id=${instabilityHost.host.id}&action=info>
                                            ${instabilityHost.host.address}
                                    </a>
                                </td>
                            </c:if>
                            <c:if test="${!instabilityHost.isOfflineStatus()}">
                                <td class="table-warning align-middle">
                                    <a href=/host?id=${instabilityHost.host.id}&action=info>
                                            ${instabilityHost.host.address}
                                    </a>
                                </td>
                            </c:if>
                            <td class="align-middle">${instabilityHost.hourOfBegin}</td>
                            <td class="align-middle">${instabilityHost.host.description}</td>
                            <c:if test="${!instabilityHost.isOfflineStatus()}">
                                <td colspan="2">
                                    <a href=/inaccessibility?id=${instabilityHost.id}&action=info
                                       class="btn btn-primary btn-sm">
                                        zobacz
                                    </a>
                                </td>
                                <td>
                                    <a href="#" class="btn btn-primary btn-sm"
                                       onclick="window.open('/ping?address=${instabilityHost.host.address}','_blank');return false">
                                        ping
                                    </a>
                                </td>
                            </c:if>
                            <c:if test="${instabilityHost.isOfflineStatus()}">
                                <td>
                                    <a href=/inaccessibility?id=${instabilityHost.id}&action=info
                                       class="btn btn-primary btn-sm">
                                        zobacz
                                    </a>
                                </td>
                                <td>
                                    <a href=/inaccessibility?id=${instabilityHost.id}&action=ignore
                                       class="btn btn-primary btn-sm">
                                        ignoruj
                                    </a>
                                </td>
                                <td>
                                    <a href="#" class="btn btn-primary btn-sm"
                                       onclick="window.open('/ping?address=${instabilityHost.host.address}','_blank');return false">
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
</div>

<jsp:include page="fragments/footer.jsp"/>

<script src="/koturno.reloader.js"></script>
<script src="/jquery-3.3.1.slim.min.js"></script>
<script src="/popper.min.js"></script>
<script src="/bootstrap.min.js"></script>
</body>
</html>
