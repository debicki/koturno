<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="pl_PL">
<head>
    <title>Historia</title>
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
        <div class="col-12 text-center koturno-style">
            <c:if test="${filter.equals('all')}">
                <a href=/history?filter=all class="btn btn-secondary">Wszystkie</a>
            </c:if>
            <c:if test="${!filter.equals('all')}">
                <a href=/history?filter=all class="btn btn-outline-secondary">Wszystkie</a>
            </c:if>
            <c:if test="${filter.equals('only-offline')}">
                <a href=/history?filter=only-offline class="btn btn-secondary">Tylko hosty offline</a>
            </c:if>
            <c:if test="${!filter.equals('only-offline')}">
                <a href=/history?filter=only-offline class="btn btn-outline-secondary">Tylko hosty offline</a>
            </c:if>
            <c:if test="${filter.equals('no-ignored')}">
                <a href=/history?filter=no-ignored class="btn btn-secondary">Historia bez ignorowanych</a>
            </c:if>
            <c:if test="${!filter.equals('no-ignored')}">
                <a href=/history?filter=no-ignored class="btn btn-outline-secondary">Historia bez ignorowanych</a>
            </c:if>
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
                    <c:forEach items="${limitedInaccessibilityList}" var="inaccessibility" varStatus="inaccessibilityStatus">
                        <tr>
                            <td class="align-middle">${inaccessibilityStatus.count}</td>
                            <c:if test="${inaccessibility.isActive()}">
                                <td class="align-middle">Aktywny</td>
                            </c:if>
                            <c:if test="${!inaccessibility.isActive()}">
                                <td class="align-middle">Archiwalny</td>
                            </c:if>
                            <td class="align-middle">${inaccessibility.host.name}</td>
                            <c:if test="${inaccessibility.isOfflineStatus() && inaccessibility.isActive()}">
                                <td class="table-danger align-middle">
                                    <a href=/host?id=${inaccessibility.host.id}&action=info>
                                            ${inaccessibility.host.address}
                                    </a>
                                </td>
                            </c:if>
                            <c:if test="${!inaccessibility.isOfflineStatus() && inaccessibility.isActive()}">
                                <td class="table-warning align-middle">
                                    <a href=/host?id=${inaccessibility.host.id}&action=info>
                                            ${inaccessibility.host.address}
                                    </a>
                                </td>
                            </c:if>
                            <c:if test="${!inaccessibility.isActive()}">
                                <td class="table-secondary align-middle">
                                    <a href=/host?id=${inaccessibility.host.id}&action=info>
                                            ${inaccessibility.host.address}
                                    </a>
                                </td>
                            </c:if>
                            <td class="align-middle">${inaccessibility.dayOfBegin}</td>
                            <td class="align-middle">${inaccessibility.hourOfBegin}</td>
                            <c:if test="${inaccessibility.isActive()}">
                                <td colspan="2" class="align-middle">TRWA</td>
                            </c:if>
                            <c:if test="${!inaccessibility.isActive() && inaccessibility.start == inaccessibility.end}">
                                <td colspan="2" class="align-middle">ZIGNOROWANY</td>
                            </c:if>
                            <c:if test="${inaccessibility.start != inaccessibility.end}">
                                <td class="align-middle">${inaccessibility.dayOfEnd}</td>
                                <td class="align-middle">${inaccessibility.hourOfEnd}</td>
                            </c:if>
                            <td>
                                <a href=/inaccessibility?id=${inaccessibility.id}&action=info class="btn btn-primary btn-sm">
                                    zobacz
                                </a>
                            </td>
                            <td>
                                <a href=/inaccessibility?id=${inaccessibility.id}&action=remove class="btn btn-danger btn-sm">
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

    <nav>
        <ul class="pagination justify-content-center">
            <li class="page-item disabled"><a class="page-link" href="#">Wyświetl pierwsze</a></li>
            <c:if test="${limit == 100}">
                <li class="page-item active"><a class="page-link" href=/history?filter=${filter}&limit=100>100</a></li>
            </c:if>
            <c:if test="${limit != 100}">
                <li class="page-item"><a class="page-link" href=/history?filter=${filter}&limit=100>100</a></li>
            </c:if>
            <c:if test="${limit == 500}">
                <li class="page-item active"><a class="page-link" href=/history?filter=${filter}&limit=500>500</a></li>
            </c:if>
            <c:if test="${limit != 500}">
                <li class="page-item"><a class="page-link" href=/history?filter=${filter}&limit=500>500</a></li>
            </c:if>
            <c:if test="${limit == 1000}">
                <li class="page-item active"><a class="page-link" href=/history?filter=${filter}&limit=1000>1000</a></li>
            </c:if>
            <c:if test="${limit != 1000}">
                <li class="page-item"><a class="page-link" href=/history?filter=${filter}&limit=1000>1000</a></li>
            </c:if>
            <c:if test="${limit == 5000}">
                <li class="page-item active"><a class="page-link" href=/history?filter=${filter}&limit=5000>5000</a></li>
            </c:if>
            <c:if test="${limit != 5000}">
                <li class="page-item"><a class="page-link" href=/history?filter=${filter}&limit=5000>5000</a></li>
            </c:if>
            <li class="page-item disabled"><a class="page-link" href="#">hostów</a></li>
        </ul>
    </nav>

</div>

<script src="/jquery-3.3.1.slim.min.js"></script>
<script src="/popper.min.js"></script>
<script src="/bootstrap.min.js"></script>
</body>
</html>
