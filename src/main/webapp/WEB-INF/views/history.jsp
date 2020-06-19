<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<fmt:setBundle basename="lang"/>

<html lang="pl_PL">
<head>
    <title><fmt:message key="head.title.history"/></title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="theme-color" content="blue">
    <link rel="stylesheet" href="/bootstrap.min.css"/>
    <link rel="stylesheet" href="/koturno.main.css"/>
</head>
<body>

<jsp:include page="fragments/title-bar.jsp"/>

<div class="container-fluid">

    <div class="row min-vh-100">

        <jsp:include page="fragments/left-menu.jsp"/>

        <div class="col-10 koturno-dark text-light">

            <div class="row mt-4">
                <div class="col-4 pb-1 koturno-style">
                <h1><span class="badge text-light koturno-darkest border border-secondary">
                    <fmt:message key="head.title.history"/>
                </span></h1>
                </div>
                <div class="col-8"></div>
            </div>

            <div class="row mt-4">
                <div class="col-6">
                    <form method="get" action="/history" class="koturno-style">
                        <div class="form-row">
                            <input type="hidden" name="limit" value="${limit}">
                            <input type="hidden" name="page" value="${page}">
                        </div>
                        <div class="form-row">
                            <div class="col-9">
                                <label for="range">
                                    <fmt:message key="body.filter.inaccessibility-longer-than"/>
                                    <span id="range-value"></span>
                                    <fmt:message key="body.filter.minutes"/>
                                </label>
                                <input type="range" id="range" min="0" max="120" name="range"
                                       class="form-control-range custom-range" value="${range}">
                            </div>
                            <div class="col-3">
                                <button class="btn btn-outline-light mt-3" type="submit">
                                    <fmt:message key="modal.button.submit"/>
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
                <div class="col-6"></div>
            </div>

            <div class="row mt-4">
                <div class="col-12 pb-3">
                    <c:if test="${limitedInaccessibilityList == null || limitedInaccessibilityList.size() == 0}">
                        <p class="h1 koturno-style"><fmt:message key="messages.information.history-is-empty"/></p>
                    </c:if>
                    <c:if test="${limitedInaccessibilityList.size() > 0}">
                        <table class="table table-dark table-sm table-bordered text-center text-light koturno-style koturno-dark">
                            <thead>
                            <tr class="koturno-darker">
                                <th><fmt:message key="table.head.number"/></th>
                                <th><fmt:message key="table.head.status"/></th>
                                <th><fmt:message key="table.head.name"/></th>
                                <th><fmt:message key="table.head.address"/></th>
                                <th colspan="2"><fmt:message key="table.head.begin"/></th>
                                <th colspan="2"><fmt:message key="table.head.end"/></th>
                                <th colspan="2"><fmt:message key="table.head.actions"/></th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${limitedInaccessibilityList}" var="inaccessibility"
                                       varStatus="inaccessibilityStatus">
                                <tr>
                                    <td class="align-middle">${inaccessibilityStatus.count + (page - 1) * limit}</td>
                                    <c:if test="${inaccessibility.isActive()}">
                                        <td class="align-middle"><fmt:message key="table.body.active"/></td>
                                    </c:if>
                                    <c:if test="${!inaccessibility.isActive()}">
                                        <td class="align-middle"><fmt:message key="table.body.archival"/></td>
                                    </c:if>
                                    <td class="align-middle">${inaccessibility.host.name}</td>
                                    <c:if test="${inaccessibility.isOfflineStatus() && inaccessibility.isActive()}">
                                        <td class="bg-danger text-light align-middle">
                                            <a href=/host?id=${inaccessibility.host.id}&action=info
                                                class="text-decoration-none text-light">
                                                    ${inaccessibility.host.address}
                                            </a>
                                        </td>
                                    </c:if>
                                    <c:if test="${!inaccessibility.isOfflineStatus() && inaccessibility.isActive()}">
                                        <td class="bg-warning text-light align-middle">
                                            <a href=/host?id=${inaccessibility.host.id}&action=info
                                                class="text-decoration-none text-light">
                                                    ${inaccessibility.host.address}
                                            </a>
                                        </td>
                                    </c:if>
                                    <c:if test="${!inaccessibility.isActive()}">
                                        <td class="bg-secondary text-light align-middle">
                                            <a href=/host?id=${inaccessibility.host.id}&action=info
                                                class="text-decoration-none text-light">
                                                    ${inaccessibility.host.address}
                                            </a>
                                        </td>
                                    </c:if>
                                    <td class="align-middle">${inaccessibility.dayOfBegin}</td>
                                    <td class="align-middle">${inaccessibility.hourOfBegin}</td>
                                    <c:if test="${inaccessibility.isActive()}">
                                        <td colspan="2" class="align-middle"><fmt:message key="table.body.continues"/></td>
                                    </c:if>
                                    <c:if test="${!inaccessibility.isActive() && inaccessibility.start == inaccessibility.end}">
                                        <td colspan="2" class="align-middle"><fmt:message key="table.body.ignored"/></td>
                                    </c:if>
                                    <c:if test="${!inaccessibility.isActive() && inaccessibility.start != inaccessibility.end}">
                                        <td class="align-middle">${inaccessibility.dayOfEnd}</td>
                                        <td class="align-middle">${inaccessibility.hourOfEnd}</td>
                                    </c:if>
                                    <td>
                                        <a href=/inaccessibility?id=${inaccessibility.id}&action=info
                                           class="btn btn-outline-light btn-sm">
                                            <fmt:message key="table.button-label.see"/>
                                        </a>
                                    </td>
                                    <td>
                                        <a href=/inaccessibility?id=${inaccessibility.id}&action=remove
                                           class="btn btn-outline-danger btn-sm">
                                            <fmt:message key="table.button-label.remove"/>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </c:if>
                </div>
            </div>

            <c:if test="${limitedInaccessibilityList.size() > 0}">
                <nav>
                    <ul class="pagination justify-content-center">
                        <li class="page-item disabled">
                            <a class="page-link koturno-dark" href="#"><fmt:message key="body.filter.positions-per-page"/>:</a>
                        </li>
                        <c:if test="${limit == 25}">
                            <li class="page-item">
                                <a class="page-link bg-light text-secondary text-decoration-none"
                                    href=/history?limit=25&range=${range}>25</a>
                            </li>
                        </c:if>
                        <c:if test="${limit != 25}">
                            <li class="page-item">
                                <a class="page-link koturno-dark text-light text-decoration-none"
                                    href=/history?limit=25&range=${range}>25</a>
                            </li>
                        </c:if>
                        <c:if test="${limit == 50}">
                            <li class="page-item">
                                <a class="page-link bg-light text-secondary text-decoration-none"
                                    href=/history?limit=50&range=${range}>50</a>
                            </li>
                        </c:if>
                        <c:if test="${limit != 50}">
                            <li class="page-item">
                                <a class="page-link koturno-dark text-light text-decoration-none"
                                    href=/history?limit=50&range=${range}>50</a>
                            </li>
                        </c:if>
                        <c:if test="${limit == 75}">
                            <li class="page-item">
                                <a class="page-link bg-light text-secondary text-decoration-none"
                                    href=/history?limit=75&range=${range}>75</a>
                            </li>
                        </c:if>
                        <c:if test="${limit != 75}">
                            <li class="page-item">
                                <a class="page-link koturno-dark text-light text-decoration-none"
                                    href=/history?limit=75&range=${range}>75</a>
                            </li>
                        </c:if>
                        <c:if test="${limit == 100}">
                            <li class="page-item">
                                <a class="page-link bg-light text-secondary text-decoration-none"
                                    href=/history?limit=100&range=${range}>100</a>
                            </li>
                        </c:if>
                        <c:if test="${limit != 100}">
                            <li class="page-item">
                                <a class="page-link koturno-dark text-light text-decoration-none"
                                     href=/history?limit=100&range=${range}>100</a>
                            </li>
                        </c:if>
                    </ul>
                </nav>

                <nav>
                    <ul class="pagination justify-content-center">
                        <c:if test="${page > 1}">
                            <li class="page-item">
                                <a class="page-link koturno-dark text-light text-decoration-none"
                                    href="/history?limit=${limit}&page=${page - 1}&range=${range}">
                                    <span>&laquo;</span>
                                </a>
                            </li>
                        </c:if>
                        <c:if test="${page <= 1}">
                            <li class="page-item disabled"><a class="page-link koturno-dark text-secondary" href="#"><span>&laquo;</span></a></li>
                        </c:if>
                        <li class="page-item disabled">
                            <a class="page-link koturno-dark text-secondary" href="#">
                                <fmt:message key="body.filter.page"/> ${page}/${numberOfPages}
                            </a>
                        </li>
                        <c:if test="${page < numberOfPages}">
                            <li class="page-item">
                                <a class="page-link koturno-dark text-light text-decoration-none"
                                    href="/history?limit=${limit}&page=${page + 1}&range=${range}">
                                    <span>&raquo;</span>
                                </a>
                            </li>
                        </c:if>
                        <c:if test="${page >= numberOfPages}">
                            <li class="page-item disabled"><a class="page-link koturno-dark text-secondary" href="#"><span>&raquo;</span></a></li>
                        </c:if>
                    </ul>
                </nav>
            </c:if>
        </div>
    </div>
</div>

<jsp:include page="fragments/footer-bar.jsp"/>

<script>
    var slider = document.getElementById("range");
    var output = document.getElementById("range-value");
    output.innerHTML = slider.value;
    slider.oninput = function () {
        output.innerHTML = this.value;
    }
</script>
<script src="/jquery-3.3.1.slim.min.js"></script>
<script src="/popper.min.js"></script>
<script src="/bootstrap.min.js"></script>
</body>
</html>
