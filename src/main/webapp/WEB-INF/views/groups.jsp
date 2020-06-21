<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<fmt:setBundle basename="lang"/>

<html lang="pl_PL">
<head>
    <title><fmt:message key="head.title.groups"/></title>
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

        <div class="modal" id="newGroupModal" tabindex="-1" role="dialog">
                <div class="modal-dialog" role="document">
                    <div class="modal-content koturno-style border border-light rounded">
                        <div class="modal-header koturno-dark text-light">
                            <h5 class="modal-title"><fmt:message key="modal.title.create-group"/></h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                                <span aria-hidden="true" class="text-light">×</span>
                            </button>
                        </div>
                        <div class="modal-body koturno-dark text-light">
                            <form method="post" action="/groups">
                                <div class="form-group">
                                    <label for="name"><fmt:message key="modal.body.name"/></label>
                                    <fmt:message key="form.placeholder.provide-group-name" var="provideGroupName"/>
                                    <input type="text" required name="name" id="name" class="form-control koturno-dark text-light"
                                           placeholder="${provideGroupName}" autofocus="autofocus"/>
                                </div>
                                <div class="form-group">
                                    <label for="description"><fmt:message key="modal.body.description"/></label>
                                    <fmt:message key="form.placeholder.provide-group-description"
                                                 var="provideGroupDescription"/>
                                    <input type="text" name="description" id="description" class="form-control koturno-dark text-light"
                                           placeholder="${provideGroupDescription}"/>
                                </div>
                                <button class="btn btn-outline-success" type="submit"><fmt:message key="modal.button.create"/></button>
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
                    <fmt:message key="head.title.groups"/>
                </span></h1>
                </div>
                <div class="col-8"></div>
            </div>

            <div class="row mt-4">
                <div class="col-1">
                </div>
                <div class="col-10" style="min-height: 5rem">
                    <c:if test="${error.equals('group-created')}">
                        <div class="alert koturno-alert-success alert-dismissible fade show" role="alert">
                            <strong>
                                <fmt:message key="messages.title.success"/>
                            </strong> <fmt:message key="messages.information.group-created"/>
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    </c:if>
                    <c:if test="${error.equals('group-exists')}">
                        <div class="alert koturno-alert-danger alert-dismissible fade show" role="alert">
                            <strong>
                                <fmt:message key="messages.title.error"/>
                            </strong> <fmt:message key="messages.error.group-exists"/>
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    </c:if>
                    <c:if test="${error.equals('10')}">
                        <div class="alert koturno-alert-success alert-dismissible fade show" role="alert">
                            <strong>
                                <fmt:message key="messages.title.success"/>
                            </strong> <fmt:message key="messages.information.group-removed"/>
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    </c:if>
                    <c:if test="${error.equals('11')}">
                        <div class="alert koturno-alert-danger alert-dismissible fade show" role="alert">
                            <strong>
                                <fmt:message key="messages.title.error"/>
                            </strong> <fmt:message key="messages.error.group-not-empty"/>
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    </c:if>
                    <c:if test="${error.equals('12')}">
                        <div class="alert koturno-alert-danger alert-dismissible fade show" role="alert">
                            <strong>
                                <fmt:message key="messages.title.error"/>
                            </strong> <fmt:message key="messages.error.default-group-is-not-removable"/>
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    </c:if>
                </div>
                <div class="col-3">
                </div>
            </div>

            <div class="row">
            <sec:authorize access="hasRole('EDITOR') || hasRole('ADMIN')">
                <div class="col-12 koturno-style">
                    <a href="#newGroupModal" data-toggle="modal" data-target="#newGroupModal" class="btn btn-outline-light">
                        <fmt:message key="submenu.label.new-group"/>
                    </a>
                </div>
            </sec:authorize>
            </div>

            <div class="row mt-4">
                <div class="col-12 pb-3">
                    <table class="table table-dark table-sm table-bordered text-center text-light koturno-style koturno-dark">
                        <thead>
                        <tr class="koturno-darker">
                            <th><fmt:message key="table.head.number"/></th>
                            <th><fmt:message key="table.head.name"/></th>
                            <th><fmt:message key="table.head.description"/></th>
                            <th><fmt:message key="table.head.hosts-count"/></th>
                            <th><fmt:message key="table.head.actions"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${groups}" var="group" varStatus="groupStatus">
                            <tr>
                                <td class="align-middle">${groupStatus.count}</td>
                                <td class="align-middle">${group.name}</td>
                                <td class="align-middle">${group.description}</td>
                                <td class="align-middle">${groupMembersCounter.get(group.name)}</td>
                                <td>
                                    <div class="btn-group" role="group">
                                        <button id="actionButton" type="button" class="btn btn-outline-light btn-sm dropdown-toggle koturno-style" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                        <fmt:message key="table.button-label.group"/>
                                        </button>
                                        <div class="dropdown-menu koturno-dark border-light rounded" aria-labelledby="actionButton">
                                            <a class="dropdown-item text-light" href="/group?id=${group.id}&action=info">
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
        </div>
    </div>
</div>

<jsp:include page="fragments/footer-bar.jsp"/>

<script src="/jquery-3.3.1.slim.min.js"></script>
<script src="/popper.min.js"></script>
<script src="/bootstrap.min.js"></script>
</body>
</html>
