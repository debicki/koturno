<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<fmt:setBundle basename="lang"/>

<html lang="pl_PL">
<head>
    <title><fmt:message key="head.title.dashboard"/></title>
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
                <p class="h1 text-center koturno-style">
                    <fmt:message key="messages.information.all-hosts-are-online"/>
                </p>
            </c:if>
            <c:if test="${instabilityHosts.size() > 0}">
                <table class="table table-hover table-bordered text-center koturno-style">
                    <thead>
                    <tr class="thead-dark">
                        <th><fmt:message key="table.head.number"/></th>
                        <th><fmt:message key="table.head.name"/></th>
                        <th><fmt:message key="table.head.address"/></th>
                        <th><fmt:message key="table.head.last-seen"/></th>
                        <th><fmt:message key="table.head.description"/></th>
                        <th colspan="3"><fmt:message key="table.head.actions"/></th>
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
                                        <fmt:message key="table.button-label.see"/>
                                    </a>
                                </td>
                                <td>
                                    <a href="#" class="btn btn-primary btn-sm"
                                       onclick="window.open('/ping?address=${instabilityHost.host.address}','_blank');return false">
                                        <fmt:message key="table.button-label.ping"/>
                                    </a>
                                </td>
                            </c:if>
                            <c:if test="${instabilityHost.isOfflineStatus()}">
                                <td>
                                    <a href=/inaccessibility?id=${instabilityHost.id}&action=info
                                       class="btn btn-primary btn-sm">
                                        <fmt:message key="table.button-label.see"/>
                                    </a>
                                </td>
                                <td>
                                    <a href=/inaccessibility?id=${instabilityHost.id}&action=ignore
                                       class="btn btn-primary btn-sm">
                                        <fmt:message key="table.button-label.ignore"/>
                                    </a>
                                </td>
                                <td>
                                    <a href="#" class="btn btn-primary btn-sm"
                                       onclick="window.open('/ping?address=${instabilityHost.host.address}','_blank');return false">
                                        <fmt:message key="table.button-label.ping"/>
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
