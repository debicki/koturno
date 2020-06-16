<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<fmt:setBundle basename="lang"/>

<html lang="pl_PL">
<head>
    <title><fmt:message key="head.title.group-details"/></title>
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

        <div class="modal" id="editGroupModal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content koturno-style border border-light rounded">
                    <div class="modal-header koturno-dark text-light">
                        <h5 class="modal-title"><fmt:message key="modal.title.edit-group"/></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true" class="text-light">×</span>
                        </button>
                    </div>
                    <div class="modal-body koturno-dark text-light">
                        <form method="post" action="/group">
                            <input type="hidden" name="originName" value="${group.name}"/>
                            <div class="form-group">
                                <label for="name"><fmt:message key="modal.body.name"/></label>
                                <input type="text" required name="name" id="name" class="form-control koturno-dark text-light"
                                       value="${group.name}"/>
                            </div>
                            <div class="form-group">
                                <label for="description"><fmt:message key="modal.body.description"/></label>
                                <c:if test="${!group.description.equals('')}">
                                    <input type="text" name="description" id="description" class="form-control koturno-dark text-light"
                                           value="${group.description}"/>
                                </c:if>
                                <c:if test="${group.description.equals('')}">
                                    <fmt:message key="form.placeholder.provide-group-description"
                                                 var="provideGroupDescription"/>
                                    <input type="text" name="description" id="description" class="form-control koturno-dark text-light"
                                           placeholder="${provideGroupDescription}"/>
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

        <div class="modal" id="removeGroupModal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content koturno-style border border-light rounded">
                    <div class="modal-header koturno-dark text-light">
                        <h5 class="modal-title"><fmt:message key="modal.title.remove-group"/></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true" class="text-light">×</span>
                        </button>
                    </div>
                    <div class="modal-body koturno-dark text-light">
                        <form method="get" action="/group">
                            <input type="hidden" name="id" value="${group.id}"/>
                            <input type="hidden" name="action" value="remove"/>
                            <div class="koturno-style">
                                <fmt:message key="modal.body.remove-group-question"/> ${group.name}?
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
                    <fmt:message key="head.title.group-details"/>
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
                            </strong> <fmt:message key="messages.information.group-saved"/>
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    </c:if>
                    <c:if test="${error.equals('2')}">
                        <div class="alert koturno-alert-danger alert-dismissible fade show" role="alert">
                            <strong>
                                <fmt:message key="messages.title.error"/>
                            </strong> <fmt:message key="messages.error.group-exists"/>
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    </c:if>
                    <c:if test="${error.equals('3')}">
                        <div class="alert koturno-alert-danger alert-dismissible fade show" role="alert">
                            <strong>
                                <fmt:message key="messages.title.error"/>
                            </strong> <fmt:message key="messages.error.default-group-is-not-editable"/>
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
                    <div class="lead"><strong><fmt:message key="body.label.name"/>: ${group.name}</strong></div>
                    <div class="lead"><strong><fmt:message key="body.label.description"/>: ${group.description}</strong></div>
                </div>
            </div>

            <div class="row mt-4">
                <div class="col-12 koturno-style">
                    <a href="#editGroupModal" data-toggle="modal" data-target="#editGroupModal" class="btn btn-outline-light">
                        <fmt:message key="submenu.label.edit-group"/>
                    </a>
                    <a href="#removeGroupModal" data-toggle="modal" data-target="#removeGroupModal" class="btn btn-outline-danger">
                        <fmt:message key="submenu.label.remove-group"/>
                    </a>
                </div>
            </div>

            <c:if test="${hosts.size() == 0}">
                <div class="row mt-4">
                    <div class="col-12 pb-3">
                        <p class="h1 koturno-style"><fmt:message key="messages.information.group-is-empty"/></p>
                    </div>
                </div>
            </c:if>
            <c:if test="${hosts.size() > 0}">
                <div class="row mt-4">
                    <div class="col-12 pb-3">
                        <table class="table table-dark table-sm table-bordered text-center text-light koturno-style koturno-dark">
                            <thead>
                            <tr class="koturno-darker">
                                <th><fmt:message key="table.head.number"/></th>
                                <th><fmt:message key="table.head.name"/></th>
                                <th><fmt:message key="table.head.address"/></th>
                                <th><fmt:message key="table.head.description"/></th>
                                <th><fmt:message key="table.head.actions"/></th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${hosts}" var="host" varStatus="hostStatus">
                                <tr>
                                    <td class="align-middle">${hostStatus.count}</td>
                                    <td class="align-middle">${host.name}</td>
                                    <c:if test="${offlineHosts.contains(host)}">
                                        <td class="bg-danger align-middle">${host.address}</td>
                                    </c:if>
                                    <c:if test="${unstableHosts.contains(host)}">
                                        <td class="bg-warning align-middle">${host.address}</td>
                                    </c:if>
                                    <c:if test="${host.isActive() && !offlineHosts.contains(host) && !unstableHosts.contains(host)}">
                                        <td class="bg-success align-middle">${host.address}</td>
                                    </c:if>
                                    <c:if test="${!host.isActive()}">
                                        <td class="bg-secondary align-middle">${host.address}</td>
                                    </c:if>
                                    <td>${host.description}</td>
                                    <td>
                                        <a href=/host?id=${host.id}&action=info class="btn btn-outline-light btn-sm">
                                            <fmt:message key="table.button-label.see"/>
                                        </a>
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

