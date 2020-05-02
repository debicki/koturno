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

<jsp:include page="fragments/main-menu.jsp"/>

<div class="container">

    <div class="row">
        <div class="col-12 text-center koturno-style">
            <a href="#editInaccessibilityModal" data-toggle="modal" data-target="#editInaccessibilityModal"
               class="btn btn-primary">
                <fmt:message key="submenu.label.edit-description"/>
            </a>
        </div>
    </div>

    <br>

    <div class="row">
        <div class="col-12 text-center koturno-style">
            <div class="h1">${inaccessibility.host.name}</div>
            <div class="h2"><a href=/host?id=${inaccessibility.host.id}&action=info>${inaccessibility.host.address}</a>
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

    <br>

    <div class="modal" id="editInaccessibilityModal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content koturno-style">
                <div class="modal-header">
                    <h5 class="modal-title"><fmt:message key="modal.title.edit-description"/></h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form method="post" action="/inaccessibility">
                        <input type="hidden" name="id" value="${inaccessibility.id}"/>
                        <div class="form-group">
                            <label for="description"><fmt:message key="modal.body.description"/></label>
                            <c:if test="${!inaccessibility.description.equals('')}">
                                <input type="text" name="description" id="description" class="form-control"
                                       value="${inaccessibility.description}"/>
                            </c:if>
                            <c:if test="${inaccessibility.description.equals('')}">
                                <fmt:message key="form.placeholder.provide-inaccessibility-description"
                                             var="provideInaccessibilityDescription"/>
                                <input type="text" name="description" id="description" class="form-control"
                                       placeholder="${provideInaccessibilityDescription}"/>
                            </c:if>
                        </div>
                        <button class="btn btn-success" type="submit"><fmt:message key="modal.button.save"/></button>
                        <button class="btn btn-secondary" type="reset"><fmt:message key="modal.button.clear"/></button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">
                            <fmt:message key="modal.button.cancel"/>
                        </button>
                        <sec:csrfInput/>
                    </form>
                </div>
            </div>
        </div>
    </div>

</div>

<jsp:include page="fragments/footer.jsp"/>

<script src="/jquery-3.3.1.slim.min.js"></script>
<script src="/popper.min.js"></script>
<script src="/bootstrap.min.js"></script>
</body>
</html>

