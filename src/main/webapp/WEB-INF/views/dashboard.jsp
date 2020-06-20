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

<jsp:include page="fragments/title-bar.jsp"/>

<div class="container-fluid">
    <div class="row min-vh-100">

        <jsp:include page="fragments/left-menu.jsp"/>

        <div class="col-10 koturno-dark text-light">

            <div class="row mt-4 ml-1 mr-1">
                <div class="col-3">
                    <div class="card text-center border-danger koturno-dark">
                        <div class="card-body text-danger koturno-style">
                            <h5 class="card-title"><fmt:message key="body.label.inaccessible"/></h5>
                            <p class="card-text display-4">${inaccessibleHostsNumber}</p>
                        </div>
                    </div>
                </div>

                <div class="col-3">
                    <div class="card text-center border-warning koturno-dark">
                        <div class="card-body text-warning koturno-style">
                            <h5 class="card-title"><fmt:message key="body.label.unstable"/></h5>
                            <p class="card-text display-4">${unstableHostsNumber}</p>
                        </div>
                    </div>
                </div>

                <div class="col-3">
                    <div class="card text-center border-success koturno-dark">
                        <div class="card-body text-success koturno-style">
                            <h5 class="card-title"><fmt:message key="body.label.online"/></h5>
                            <p class="card-text display-4">${onlineHostsNumber}</p>
                        </div>
                    </div>
                </div>

                <div class="col-3">
                    <div class="card text-center border-secondary koturno-dark">
                        <div class="card-body text-secondary koturno-style">
                            <h5 class="card-title"><fmt:message key="body.label.inactive"/></h5>
                            <p class="card-text display-4">${inactiveHostsNumber}</p>
                        </div>
                    </div>
                </div>
            </div>

            <div class="row mt-5">
                <div class="col-12 pb-3">
                    <c:if test="${instabilityHosts.size() == 0 && hostsNumber > 0}">
                        <p class="h1 text-center koturno-style">
                            <fmt:message key="messages.information.all-hosts-are-online"/>
                        </p>
                    </c:if>
                    <c:if test="${hostsNumber == 0}">
                        <p class="h1 text-center koturno-style">
                            <fmt:message key="messages.information.hosts-are-empty"/>
                        </p>
                    </c:if>
                    <c:if test="${instabilityHosts.size() > 0}">
                        <table class="table table-bordered table-dark table-sm text-center text-light koturno-style koturno-dark">
                            <thead>
                            <tr class="koturno-darker">
                                <th><fmt:message key="table.head.number"/></th>
                                <th><fmt:message key="table.head.name"/></th>
                                <th><fmt:message key="table.head.address"/></th>
                                <th><fmt:message key="table.head.last-seen"/></th>
                                <th><fmt:message key="table.head.description"/></th>
                                <th colspan="2"><fmt:message key="table.head.actions"/></th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${instabilityHosts}" var="instabilityHost" varStatus="instabilityHostStatus">
                                <tr>
                                    <c:if test="${instabilityHost.isOfflineStatus()}">
                                        <td class="align-middle">
                                            <div class="border-bottom border-top border-danger">${instabilityHostStatus.count}</div>
                                        </td>
                                    </c:if>
                                    <c:if test="${!instabilityHost.isOfflineStatus()}">
                                        <td class="align-middle">
                                            <div class="border-bottom border-top border-warning">${instabilityHostStatus.count}</div>
                                        </td>
                                    </c:if>
                                    <td class="align-middle">${instabilityHost.host.name}</td>
                                    <td class="align-middle">${instabilityHost.host.address}</td>
                                    <jsp:useBean id="today" class="java.util.Date" />
                                    <fmt:formatDate var="day" value="${today}" pattern="yyyy-MM-dd" />
                                    <c:if test="${instabilityHost.dayOfBegin.equals(day)}">
                                        <td class="align-middle">${instabilityHost.hourOfBegin}</td>
                                    </c:if>
                                    <c:if test="${!instabilityHost.dayOfBegin.equals(day)}">
                                        <td class="align-middle">${instabilityHost.dayOfBegin}</td>
                                    </c:if>
                                    <td class="align-middle">${instabilityHost.host.description}</td>
                                    <sec:authorize access="!isAuthenticated()">
                                        <td colspan="2"><fmt:message key="table.button-label.access-after-logging-in"/></td>
                                    </sec:authorize>
                                    <sec:authorize access="isAuthenticated()">
                                        <td>
                                            <div class="btn-group" role="group">
                                                <button id="hostButton" type="button" class="btn btn-outline-light btn-sm dropdown-toggle koturno-style" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                                <fmt:message key="table.button-label.host"/>
                                                </button>
                                                <div class="dropdown-menu koturno-dark border-light rounded" aria-labelledby="hostButton">
                                                    <a class="dropdown-item text-light" href="/host?id=${instabilityHost.host.id}&action=info">
                                                        <fmt:message key="table.button-label.details"/>
                                                    </a>
                                                    <a class="dropdown-item text-light" href="#" onclick="window.open('/ping?address=${instabilityHost.host.address}','_blank');return false">
                                                        <fmt:message key="table.button-label.ping"/>
                                                    </a>
                                                </div>
                                            </div>
                                        </td>
                                        <td>
                                            <div class="btn-group" role="group">
                                                <button id="inaccessibilityButton" type="button" class="btn btn-outline-light btn-sm dropdown-toggle koturno-style" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                                <fmt:message key="table.button-label.inaccessibility"/>
                                                </button>
                                                <div class="dropdown-menu koturno-dark border-light rounded" aria-labelledby="inaccessibilityButton">
                                                    <a class="dropdown-item text-light" href="/inaccessibility?id=${instabilityHost.id}&action=info">
                                                        <fmt:message key="table.button-label.details"/>
                                                    </a>
                                                    <c:if test="${instabilityHost.isOfflineStatus()}">
                                                        <a class="dropdown-item text-light" href="/inaccessibility?id=${instabilityHost.id}&action=ignore">
                                                            <fmt:message key="table.button-label.ignore"/>
                                                        </a>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </td>
                                    </sec:authorize>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="fragments/footer-bar.jsp"/>

<script src="/koturno.reloader.js"></script>
<script src="/jquery-3.3.1.slim.min.js"></script>
<script src="/popper.min.js"></script>
<script src="/bootstrap.min.js"></script>
</body>
</html>
