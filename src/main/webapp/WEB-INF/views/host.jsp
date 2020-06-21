<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<fmt:setBundle basename="lang"/>

<html lang="pl_PL">
<head>
    <title><fmt:message key="head.title.host-details"/></title>
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

        <div class="modal" id="editHostModal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content koturno-style border border-light rounded">
                    <div class="modal-header koturno-dark text-light">
                        <h5 class="modal-title"><fmt:message key="modal.title.edit-host"/></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true" class="text-light">×</span>
                        </button>
                    </div>
                    <div class="modal-body koturno-dark text-light">
                        <form method="post" action="/host">
                            <input type="hidden" name="originAddress" value="${host.address}"/>
                            <div class="form-row">
                                <div class="form-group col-8">
                                    <label for="address"><fmt:message key="modal.body.address"/></label>
                                    <input type="text" required name="address" id="address" class="form-control koturno-dark text-light"
                                           value="${host.address}"/>
                                </div>
                                <div class="form-group col-4">
                                    <label for="activity"><fmt:message key="modal.body.activity"/></label>
                                    <select name="activity" id="activity" class="form-control koturno-dark text-light">
                                        <c:if test="${host.isActive()}">
                                            <option selected><fmt:message key="modal.body.active"/></option>
                                            <option><fmt:message key="modal.body.inactive"/></option>
                                        </c:if>
                                        <c:if test="${!host.isActive()}">
                                            <option selected><fmt:message key="modal.body.inactive"/></option>
                                            <option><fmt:message key="modal.body.active"/></option>
                                        </c:if>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="name"><fmt:message key="modal.body.name"/></label>
                                <c:if test="${!host.name.equals('')}">
                                    <input type="text" name="name" id="name" class="form-control koturno-dark text-light" value="${host.name}"/>
                                </c:if>
                                <c:if test="${host.name.equals('')}">
                                    <fmt:message key="form.placeholder.provide-host-name" var="provideHostName"/>
                                    <input type="text" name="name" id="name" class="form-control koturno-dark text-light"
                                           placeholder="${provideHostName}"/>
                                </c:if>
                            </div>
                            <div class="form-group">
                                <label for="description"><fmt:message key="modal.body.description"/></label>
                                <c:if test="${!host.description.equals('')}">
                                    <input type="text" name="description" id="description" class="form-control koturno-dark text-light"
                                           value="${host.description}"/>
                                </c:if>
                                <c:if test="${host.description.equals('')}">
                                    <fmt:message key="form.placeholder.provide-host-description"
                                                 var="provideHostDescription"/>
                                    <input type="text" name="description" id="description" class="form-control koturno-dark text-light"
                                           placeholder="${provideHostDescription}"/>
                                </c:if>
                            </div>
                            <div class="form-group">
                                <label for="hostGroupName"><fmt:message key="modal.body.group"/></label>
                                <select name="hostGroupName" id="hostGroupName" class="form-control koturno-dark text-light">
                                    <option selected>${host.hostGroup.name}</option>
                                    <c:forEach items="${hostGroupList}" var="hostGroup" varStatus="hostGroupStatus">
                                        <c:if test="${!hostGroup.name.equals(host.hostGroup.name)}">
                                            <option>${hostGroup.name}</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
                            </div>
                            <button class="btn btn-outline-success" type="submit"><fmt:message key="modal.button.save"/></button>
                            <button class="btn btn-outline-light" type="reset"><fmt:message key="modal.button.clear"/></button>
                            <button type="button" class="btn btn-outline-light" data-dismiss="modal">
                                <fmt:message key="modal.button.cancel"/>
                            </button>
                            <sec:csrfInput/>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <div class="modal" id="removeHostModal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content koturno-style border border-light rounded">
                    <div class="modal-header koturno-dark text-light">
                        <h5 class="modal-title"><fmt:message key="modal.title.remove-host"/></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true" class="text-light">×</span>
                        </button>
                    </div>
                    <div class="modal-body koturno-dark text-light">
                        <form method="get" action="/host">
                            <input type="hidden" name="id" value="${host.id}"/>
                            <input type="hidden" name="action" value="remove"/>
                            <div class="koturno-style">
                                <fmt:message key="modal.body.remove-host-question"/> ${host.address}?
                            </div>
                            <br>
                            <button class="btn btn-outline-danger" type="submit"><fmt:message key="modal.button.remove"/></button>
                            <button type="button" class="btn btn-outline-light" data-dismiss="modal">
                                <fmt:message key="modal.button.cancel"/>
                            </button>
                            <sec:csrfInput/>
                        </form>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-10 koturno-dark text-light">

            <div class="row mt-4">
                <div class="col-4 pb-1 koturno-style">
                <h1><span class="badge text-light koturno-darkest border border-secondary">
                    <fmt:message key="head.title.host-details"/>
                </span></h1>
                </div>
                <div class="col-8"></div>
            </div>

            <div class="row mt-4">
                <div class="col-1">
                </div>
                <div class="col-10" style="min-height: 5rem">
                    <c:if test="${error.equals('0')}">
                        <div class="alert koturno-alert-success alert-dismissible fade show" role="alert">
                            <strong>
                                <fmt:message key="messages.title.success"/>
                            </strong> <fmt:message key="messages.information.host-saved"/>
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    </c:if>
                    <c:if test="${error.equals('1')}">
                        <div class="alert koturno-alert-danger alert-dismissible fade show" role="alert">
                            <strong>
                                <fmt:message key="messages.title.error"/>
                            </strong> <fmt:message key="messages.error.host-exists"/>
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    </c:if>
                </div>
                <div class="col-1">
                </div>
            </div>

            <div class="row">
                <div class="col-12 koturno-style">
                    <div class="h1">${host.name}</div>
                    <div class="h2">${host.address}</div>
                    <div class="lead">
                        <strong>
                            <fmt:message key="body.label.group"/>:
                            <a class="text-decoration-none text-primary" href=/group?id=${host.hostGroup.id}&action=info> ${host.hostGroup.name}</a>
                        </strong>
                    </div>
                    <div class="lead"><strong><fmt:message key="body.label.added-at"/>: ${host.dayWhenCreated}</strong></div>
                    <c:if test="${host.isActive()}">
                        <div class="lead">
                            <strong><fmt:message key="body.label.status"/>: <fmt:message key="body.label.active"/></strong>
                        </div>
                    </c:if>
                    <c:if test="${!host.isActive()}">
                        <div class="lead">
                            <strong><fmt:message key="body.label.status"/>: <fmt:message key="body.label.inactive"/></strong>
                        </div>
                    </c:if>
                    <div class="lead"><strong><fmt:message key="body.label.description"/>: ${host.description}</strong></div>
                    <c:if test="${host.externalLink.length() > 0}">
                        <div class="lead">
                            <strong>
                                <fmt:message key="body.label.external-link"/>:
                                <a href="${host.externalLink}"> ${host.externalLink}</a>
                            </strong>
                        </div>
                    </c:if>
                </div>
            </div>

            <sec:authorize access="hasRole('EDITOR') || hasRole('ADMIN')">
                <div class="row mt-4">
                    <div class="col-12 koturno-style">
                        <a href="#editHostModal" data-toggle="modal" data-target="#editHostModal" class="btn btn-outline-light">
                            <fmt:message key="submenu.label.edit-host"/>
                        </a>
                        <a href="#removeHostModal" data-toggle="modal" data-target="#removeHostModal" class="btn btn-outline-danger">
                            <fmt:message key="submenu.label.remove-host"/>
                        </a>
                    </div>
                </div>
            </sec:authorize>

            <c:if test="${inaccessibilityList.size() == 0}">
                <div class="row mt-4">
                    <div class="col-12 pb-3">
                        <p class="h1 koturno-style">
                            <fmt:message key="messages.information.host-history-is-empty"/>
                        </p>
                    </div>
                </div>
            </c:if>
            <c:if test="${inaccessibilityList.size() > 0}">
                <div class="row mt-4">
                    <div class="col-12 pb-3">
                        <table class="table table-dark table-sm table-bordered text-center text-light koturno-style koturno-dark">
                            <thead>
                            <tr class="koturno-darker">
                                <th><fmt:message key="table.head.number"/></th>
                                <th colspan="2"><fmt:message key="table.head.begin"/></th>
                                <th colspan="2"><fmt:message key="table.head.end"/></th>
                                <th><fmt:message key="table.head.description"/></th>
                                <th><fmt:message key="table.head.actions"/></th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${inaccessibilityList}" var="inaccessibility" varStatus="inaccessibilityStatus">
                                <tr>
                                    <c:if test="${inaccessibility.isOfflineStatus()}">
                                        <td class="align-middle"><div class="border-bottom border-top border-danger">${inaccessibilityStatus.count}</div></td>
                                    </c:if>
                                    <c:if test="${!inaccessibility.isOfflineStatus()}">
                                        <td class="align-middle"><div class="border-bottom border-top border-warning">${inaccessibilityStatus.count}</div></td>
                                    </c:if>
                                    <td class="align-middle">${inaccessibility.dayOfBegin}</td>
                                    <td class="align-middle">${inaccessibility.hourOfBegin}</td>
                                    <c:if test="${inaccessibility.start == inaccessibility.end}">
                                        <td colspan="2" class="align-middle"><fmt:message key="table.body.continues"/></td>
                                    </c:if>
                                    <c:if test="${inaccessibility.start != inaccessibility.end}">
                                        <td class="align-middle">${inaccessibility.dayOfEnd}</td>
                                        <td class="align-middle">${inaccessibility.hourOfEnd}</td>
                                    </c:if>
                                    <td class="align-middle">${inaccessibility.description}</td>
                                    <td>
                                        <div class="btn-group" role="group">
                                            <button id="inaccessibilityButton" type="button" class="btn btn-outline-light btn-sm dropdown-toggle koturno-style" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                            <fmt:message key="table.button-label.inaccessibility"/>
                                            </button>
                                            <div class="dropdown-menu koturno-dark border-light rounded" aria-labelledby="inaccessibilityButton">
                                                <a class="dropdown-item text-light" href="/inaccessibility?id=${inaccessibility.id}&action=info">
                                                    <fmt:message key="table.button-label.details"/>
                                                </a>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </c:if>
        </div>
    </div>
</div>

<jsp:include page="fragments/footer-bar.jsp"/>

<script src="/jquery-3.3.1.slim.min.js"></script>
<script src="/popper.min.js"></script>
<script src="/bootstrap.min.js"></script>
</body>
</html>
