<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<fmt:setBundle basename="lang"/>

<html lang="pl_PL">
<head>
    <title><fmt:message key="head.title.users"/></title>
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

        <div class="modal" id="newUserModal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content koturno-style border border-light rounded">
                    <div class="modal-header koturno-dark text-light">
                        <h5 class="modal-title"><fmt:message key="modal.title.add-user"/></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true" class="text-light">×</span>
                        </button>
                    </div>
                    <div class="modal-body koturno-dark text-light">
                        <form method="post" action="/users">
                            <div class="form-group">
                                <label for="username"><fmt:message key="modal.body.username"/></label>
                                <fmt:message key="form.placeholder.provide-username" var="provideUsername"/>
                                <input type="text" required name="username" id="username" class="form-control koturno-dark text-light"
                                       placeholder="${provideUsername}" autofocus="autofocus" autocomplete="off"/>
                            </div>
                            <div class="form-group">
                                <label for="password"><fmt:message key="body.label.password"/></label>
                                <fmt:message key="form.placeholder.provide-password" var="providePassword"/>
                                <input type="password" required name="password" id="password" class="form-control koturno-dark text-light"
                                       placeholder="${providePassword}"/>
                            </div>
                            <div class="form-group">
                                <label for="password2"><fmt:message key="body.label.repeat-password"/></label>
                                <fmt:message key="form.placeholder.repeat-password" var="repeatPassword"/>
                                <input type="password" required name="password2" id="password2" class="form-control koturno-dark text-light"
                                       placeholder="${repeatPassword}"/>
                            </div>
                            <div class="form-row">
                                <div class="form-group col-6">
                                    <label for="role"><fmt:message key="modal.body.role"/></label>
                                    <select name="role" id="role" class="form-control koturno-dark text-light">
                                        <option value="ROLE_USER" selected><fmt:message key="modal.body.user"/></option>
                                        <option value="ROLE_MOD"><fmt:message key="modal.body.mod"/></option>
                                        <option value="ROLE_ADMIN"><fmt:message key="modal.body.admin"/></option>
                                    </select>
                                </div>
                                <div class="form-group col-6">
                                    <label for="activity"><fmt:message key="modal.body.activity"/></label>
                                    <select name="activity" id="activity" class="form-control koturno-dark text-light">
                                        <option value="true" selected><fmt:message key="modal.body.active"/></option>
                                        <option value="false"><fmt:message key="modal.body.inactive"/></option>
                                    </select>
                                </div>
                            </div>
                            <button class="btn btn-outline-success" type="submit"><fmt:message key="modal.button.add"/></button>
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

        <div class="modal" id="removeUserModal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content koturno-style border border-light rounded">
                    <div class="modal-header koturno-dark text-light">
                        <h5 class="modal-title"><fmt:message key="modal.title.remove-user"/></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true" class="text-light">×</span>
                        </button>
                    </div>
                    <div class="modal-body koturno-dark text-light">
                        <form method="get" action="/users">
                            <input type="hidden" id="user-to-remove" name="username" value=""/>
                            <input type="hidden" name="action" value="remove"/>
                            <div id="question" class="koturno-style">
                                <fmt:message key="modal.body.remove-user-question"/> ${host.address}?
                            </div>
                            <br>
                            <button class="btn btn-outline-danger koturno-style" type="submit"><fmt:message key="modal.button.remove"/></button>
                            <button type="button" class="btn btn-outline-light koturno-style" data-dismiss="modal">
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
                    <fmt:message key="head.title.users"/>
                </span></h1>
                </div>
                <div class="col-8"></div>
            </div>

            <div class="row mt-4">
                <div class="col-1">
                </div>
                <div class="col-10" style="min-height: 5rem">
                    <c:if test="${error.equals('user-added')}">
                        <div class="alert koturno-alert-success alert-dismissible fade show" role="alert">
                            <strong>
                                <fmt:message key="messages.title.success"/>
                            </strong> <fmt:message key="messages.information.user-added"/>
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    </c:if>
                    <c:if test="${error.equals('user-removed')}">
                        <div class="alert koturno-alert-success alert-dismissible fade show" role="alert">
                            <strong>
                                <fmt:message key="messages.title.success"/>
                            </strong> <fmt:message key="messages.information.user-removed"/>
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    </c:if>
                    <c:if test="${error.equals('user-enabled')}">
                        <div class="alert koturno-alert-success alert-dismissible fade show" role="alert">
                            <strong>
                                <fmt:message key="messages.title.success"/>
                            </strong> <fmt:message key="messages.information.user-enabled"/>
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    </c:if>
                    <c:if test="${error.equals('user-disabled')}">
                        <div class="alert koturno-alert-success alert-dismissible fade show" role="alert">
                            <strong>
                                <fmt:message key="messages.title.success"/>
                            </strong> <fmt:message key="messages.information.user-disabled"/>
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    </c:if>
                    <c:if test="${error.equals('user-exists')}">
                        <div class="alert koturno-alert-danger alert-dismissible fade show" role="alert">
                            <strong>
                                <fmt:message key="messages.title.error"/>
                            </strong> <fmt:message key="messages.error.username-exists"/>
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    </c:if>
                    <c:if test="${error.equals('passwords-mismatch')}">
                        <div class="alert koturno-alert-danger alert-dismissible fade show" role="alert">
                            <strong>
                                <fmt:message key="messages.title.error"/>
                            </strong> <fmt:message key="messages.error.passwords-mismatch"/>
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    </c:if>
                    <c:if test="${error.equals('no-active-admin')}">
                        <div class="alert koturno-alert-danger alert-dismissible fade show" role="alert">
                            <strong>
                                <fmt:message key="messages.title.error"/>
                            </strong> <fmt:message key="messages.error.no-active-admin"/>
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
                    <a href="#newUserModal" data-toggle="modal" data-target="#newUserModal" class="btn btn-outline-light">
                        <fmt:message key="submenu.label.new-user"/>
                    </a>
                </div>
            </div>

            <div class="row mt-4">
                <div class="col-12 pb-3">
                    <table class="table table-dark table-sm table-bordered text-center text-light koturno-style koturno-dark">
                        <thead>
                        <tr class="koturno-darker">
                            <th><fmt:message key="table.head.number"/></th>
                            <th><fmt:message key="table.head.username"/></th>
                            <th><fmt:message key="table.head.status"/></th>
                            <th><fmt:message key="table.head.role"/></th>
                            <th><fmt:message key="table.head.actions"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${users}" var="user" varStatus="userStatus">
                            <tr>
                                <td class="align-middle">${userStatus.count}</td>
                                <td class="align-middle">${user.username}</td>
                                <c:if test="${user.isActive()}">
                                    <td class="align-middle"><fmt:message key="table.body.on"/></td>
                                </c:if>
                                <c:if test="${!user.isActive()}">
                                    <td class="align-middle"><fmt:message key="table.body.off"/></td>
                                </c:if>
                                <c:if test="${user.role.equals('ROLE_ADMIN')}">
                                    <td class="align-middle"><fmt:message key="table.body.admin"/></td>
                                </c:if>
                                <c:if test="${user.role.equals('ROLE_MOD')}">
                                    <td class="align-middle"><fmt:message key="table.body.mod"/></td>
                                </c:if>
                                <c:if test="${user.role.equals('ROLE_USER')}">
                                    <td class="align-middle"><fmt:message key="table.body.user"/></td>
                                </c:if>
                                <td>
                                    <div class="btn-group" role="group">
                                        <button id="actionButton" type="button" class="btn btn-outline-light btn-sm dropdown-toggle koturno-style" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                        <fmt:message key="table.button-label.user"/>
                                        </button>
                                        <div class="dropdown-menu koturno-dark border-light rounded" aria-labelledby="actionButton">
                                            <c:if test="${user.isActive()}">
                                                <a class="dropdown-item text-light" href="/users?username=${user.username}&action=disable">
                                                    <fmt:message key="table.button-label.off"/>
                                                </a>
                                            </c:if>
                                            <c:if test="${!user.isActive()}">
                                                <a class="dropdown-item text-light" href="/users?username=${user.username}&action=enable">
                                                    <fmt:message key="table.button-label.on"/>
                                                </a>
                                            </c:if>
                                            <a class="dropdown-item text-danger" href="#removeUserModal" data-toggle="modal" data-target="#removeUserModal" data-username="${user.username}">
                                                <fmt:message key="table.button-label.remove"/>
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
<script>
$('#removeUserModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget)
    var username = button.data('username')
    var modal = $(this)
    $('#user-to-remove').val(username)
    $('#question').text('<fmt:message key="modal.body.remove-user-question"/>' + ' ' + username + '?')
})
</script>
</body>
</html>
