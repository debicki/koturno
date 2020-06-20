<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<fmt:setBundle basename="lang"/>

<html lang="pl_PL">
<head>
    <title><fmt:message key="head.title.inaccessibility-details"/></title>
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

        <div class="modal" id="editInaccessibilityModal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content koturno-style border border-light rounded">
                    <div class="modal-header koturno-dark text-light">
                        <h5 class="modal-title"><fmt:message key="modal.title.edit-description"/></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true" class="text-light">×</span>
                        </button>
                    </div>
                    <div class="modal-body koturno-dark text-light">
                        <form method="post" action="/inaccessibility">
                            <input type="hidden" name="id" value="${inaccessibility.id}"/>
                            <div class="form-group">
                                <label for="description"><fmt:message key="modal.body.description"/></label>
                                <c:if test="${!inaccessibility.description.equals('')}">
                                    <input type="text" name="description" id="description" class="form-control koturno-dark text-light"
                                           value="${inaccessibility.description}"/>
                                </c:if>
                                <c:if test="${inaccessibility.description.equals('')}">
                                    <fmt:message key="form.placeholder.provide-inaccessibility-description"
                                                 var="provideInaccessibilityDescription"/>
                                    <input type="text" name="description" id="description" class="form-control koturno-dark text-light"
                                           placeholder="${provideInaccessibilityDescription}"/>
                                </c:if>
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

        <div class="col-10 koturno-dark text-light">

            <div class="row mt-4">
                <div class="col-4 pb-1 koturno-style">
                <h1><span class="badge text-light koturno-darkest border border-secondary">
                    <fmt:message key="head.title.inaccessibility-details"/>
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
                            </strong> <fmt:message key="messages.information.description-saved"/>
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
                    <div class="h1">${inaccessibility.host.name}</div>
                    <div class="h2">
                        <a class="text-decoration-none text-primary" href=/host?id=${inaccessibility.host.id}&action=info>
                            ${inaccessibility.host.address}
                        </a>
                    </div>
                    <div class="lead">
                        <strong><fmt:message key="body.label.start-date"/>: ${inaccessibility.dayOfBegin}</strong>
                    </div>
                    <div class="lead">
                        <strong><fmt:message key="body.label.start-time"/>: ${inaccessibility.hourOfBegin}</strong>
                    </div>
                    <c:if test="${(inaccessibility.start == inaccessibility.end) && inaccessibility.isActive()}">
                        <div class="lead">
                            <strong><fmt:message key="body.label.end-date"/>: <fmt:message key="table.body.continues"/></strong>
                        </div>
                        <div class="lead">
                            <strong><fmt:message key="body.label.end-time"/>: <fmt:message key="table.body.continues"/></strong>
                        </div>
                    </c:if>
                    <c:if test="${(inaccessibility.start == inaccessibility.end) && !inaccessibility.isActive()}">
                        <div class="lead">
                            <strong><fmt:message key="body.label.end-date"/>: <fmt:message key="table.body.ignored"/></strong>
                        </div>
                        <div class="lead">
                            <strong><fmt:message key="body.label.end-time"/>: <fmt:message key="table.body.ignored"/></strong>
                        </div>
                    </c:if>
                    <c:if test="${inaccessibility.start != inaccessibility.end}">
                        <div class="lead">
                            <strong><fmt:message key="body.label.end-date"/>: ${inaccessibility.dayOfEnd}</strong>
                        </div>
                        <div class="lead">
                            <strong><fmt:message key="body.label.end-time"/>: ${inaccessibility.hourOfEnd}</strong>
                        </div>
                    </c:if>
                    <div class="lead">
                        <strong><fmt:message key="body.label.description"/>: ${inaccessibility.description}</strong>
                    </div>
                </div>
            </div>

            <div class="row mt-4">
                <div class="col-12 koturno-style">
                    <a href="#editInaccessibilityModal" data-toggle="modal" data-target="#editInaccessibilityModal"
                       class="btn btn-outline-light">
                        <fmt:message key="submenu.label.edit-description"/>
                    </a>
                </div>
            </div>

        </div>
    </div>
</div>

<jsp:include page="fragments/footer-bar.jsp"/>

<script src="/jquery-3.3.1.slim.min.js"></script>
<script src="/popper.min.js"></script>
<script src="/bootstrap.min.js"></script>
</body>
</html>

