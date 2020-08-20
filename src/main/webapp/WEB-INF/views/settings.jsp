<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<fmt:setBundle basename="lang"/>

<html lang="pl_PL">
<head>
    <title><fmt:message key="head.title.settings"/></title>
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

        <div class="modal" id="editUsernameModal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content koturno-style border border-light rounded">
                    <div class="modal-header koturno-dark text-light">
                        <h5 class="modal-title"><fmt:message key="modal.title.edit-username"/></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true" class="text-light">×</span>
                        </button>
                    </div>
                    <div class="modal-body koturno-dark text-light">
                        <form method="post" action="/settings/user/change-username">
                            <div class="form-group">
                                <label for="username"><fmt:message key="modal.body.new-username"/></label>
                                <fmt:message key="form.placeholder.provide-new-username" var="provideNewUsername"/>
                                <input type="text" required name="newUsername" id="newUsername" class="form-control koturno-dark text-light"
                                       placeholder="${provideNewUsername}" autofocus="autofocus" autocomplete="off"/>
                            </div>
                            <div class="form-group">
                                <label for="password"><fmt:message key="modal.body.confirm-with-password"/></label>
                                <fmt:message key="form.placeholder.provide-password" var="providePassword"/>
                                <input type="password" required name="password" id="password" class="form-control koturno-dark text-light"
                                       placeholder="${providePassword}"/>
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

        <div class="modal" id="editPasswordModal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content koturno-style border border-light rounded">
                    <div class="modal-header koturno-dark text-light">
                        <h5 class="modal-title"><fmt:message key="modal.title.edit-password"/></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true" class="text-light">×</span>
                        </button>
                    </div>
                    <div class="modal-body koturno-dark text-light">
                        <form method="post" action="/settings/user/change-password">
                            <div class="form-group">
                                <label for="oldPassword"><fmt:message key="body.label.old-password"/></label>
                                <fmt:message key="form.placeholder.provide-old-password" var="provideOldPassword"/>
                                <input type="password" required name="oldPassword" id="oldPassword" class="form-control koturno-dark text-light"
                                       placeholder="${provideOldPassword}"/>
                            </div>
                            <div class="form-group">
                                <label for="newPassword"><fmt:message key="body.label.new-password"/></label>
                                <fmt:message key="form.placeholder.provide-new-password" var="provideNewPassword"/>
                                <input type="password" required name="newPassword" id="newPassword" class="form-control koturno-dark text-light"
                                       placeholder="${provideNewPassword}"/>
                            </div>
                            <div class="form-group">
                                <label for="newPassword2"><fmt:message key="body.label.repeat-new-password"/></label>
                                <fmt:message key="form.placeholder.repeat-new-password" var="repeatNewPassword"/>
                                <input type="password" required name="newPassword2" id="newPassword2" class="form-control koturno-dark text-light"
                                       placeholder="${repeatNewPassword}"/>
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

        <div class="modal" id="removeFromDatabaseModal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content koturno-style border border-light rounded">
                    <div class="modal-header koturno-dark text-light">
                        <h5 class="modal-title"><fmt:message key="modal.title.remove-from-database"/></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true" class="text-light">×</span>
                        </button>
                    </div>
                    <div class="modal-body koturno-dark text-light">
                        <form method="post" action="/settings/db">
                            <input type="hidden" id="action" name="action" value=""/>
                            <div class="form-group">
                                <label for="password"><fmt:message key="modal.body.confirm-with-password"/></label>
                                <fmt:message key="form.placeholder.provide-password" var="providePassword"/>
                                <input type="password" required name="password" id="password" class="form-control koturno-dark text-light"
                                       placeholder="${providePassword}"/>
                            </div>
                            <button class="btn btn-outline-danger" type="submit"><fmt:message key="modal.button.remove"/></button>
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
                    <fmt:message key="head.title.settings"/>
                </span></h1>
                </div>
                <div class="col-8"></div>
            </div>

            <div class="row mt-4 ml-1">
                <div class="col-6 koturno-style border-bottom border-secondary h4">
                <c:if test="${user.role.equals('ROLE_USER')}">
                    <fmt:message key="body.label.user"/>
                </c:if>
                <c:if test="${user.role.equals('ROLE_EDITOR')}">
                    <fmt:message key="body.label.editor"/>
                </c:if>
                <c:if test="${user.role.equals('ROLE_ADMIN')}">
                    <fmt:message key="body.label.admin"/>
                </c:if>
                </div>
                <div class="col-6"></div>
            </div>

            <div class="row mt-1 ml-2">
                <div class="col-8 koturno-style border border-secondary rounded">
                    <div class="mt-1"><fmt:message key="body.message.change-username"/></div>
                    <div class="mt-2 mb-1">
                        <a class="btn btn-sm btn-outline-light" href="#editUsernameModal" data-toggle="modal" data-target="#editUsernameModal">
                            <fmt:message key="table.button-label.execute"/>
                        </a>
                    </div>
                </div>
                <div class="col-4">
                </div>
            </div>

            <div class="row mt-2 ml-2">
                <div class="col-8 koturno-style border border-secondary rounded">
                    <div class="mt-1"><fmt:message key="body.message.change-password"/></div>
                    <div class="mt-2 mb-1">
                        <a class="btn btn-sm btn-outline-light" href="#editPasswordModal" data-toggle="modal" data-target="#editPasswordModal">
                            <fmt:message key="table.button-label.execute"/>
                        </a>
                    </div>
                </div>
                <div class="col-4">
                </div>
            </div>

            <c:if test="${user.role.equals('ROLE_EDITOR') || user.role.equals('ROLE_ADMIN')}">
                <div class="row mt-4 ml-1">
                    <div class="col-6 koturno-style border-bottom border-secondary h4">
                        <fmt:message key="body.label.database-section"/>
                    </div>
                    <div class="col-6"></div>
                </div>

                <div class="row mt-1 ml-2">
                    <div class="col-8 koturno-style border border-secondary rounded">
                        <div class="mt-1"><fmt:message key="body.message.clear-history"/></div>
                        <div class="mt-2 mb-1">
                            <a class="btn btn-sm btn-outline-danger" href="#removeFromDatabaseModal" data-toggle="modal" data-target="#removeFromDatabaseModal" data-action="clear-history">
                                <fmt:message key="table.button-label.execute"/>
                            </a>
                        </div>
                    </div>
                    <div class="col-4">
                    </div>
                </div>

                <div class="row mt-2 ml-2">
                    <div class="col-8 koturno-style border border-secondary rounded">
                        <div class="mt-1"><fmt:message key="body.message.clear-hosts"/></div>
                        <div class="mt-2 mb-1">
                            <a class="btn btn-sm btn-outline-danger" href="#removeFromDatabaseModal" data-toggle="modal" data-target="#removeFromDatabaseModal" data-action="clear-hosts">
                                <fmt:message key="table.button-label.execute"/>
                            </a>
                        </div>
                    </div>
                    <div class="col-4">
                    </div>
                </div>

                <div class="row mt-2 ml-2">
                    <c:if test="${user.role.equals('ROLE_ADMIN')}">
                    <div class="col-8 koturno-style border border-secondary rounded">
                        <div class="mt-1"><fmt:message key="body.message.clear-database"/></div>
                        <div class="mt-2 mb-1">
                            <a class="btn btn-sm btn-outline-danger" href="#removeFromDatabaseModal" data-toggle="modal" data-target="#removeFromDatabaseModal" data-action="clear-database">
                                <fmt:message key="table.button-label.execute"/>
                            </a>
                        </div>
                    </div>
                    <div class="col-4">
                    </div>
                    </c:if>
                </div>
            </c:if>

            <div class="row mb-5">
            </div>

        </div>
    </div>
</div>

<jsp:include page="fragments/footer-bar.jsp"/>

<script src="/jquery-3.3.1.slim.min.js"></script>
<script src="/popper.min.js"></script>
<script src="/bootstrap.min.js"></script>
<script>
$('#removeFromDatabaseModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget)
    var action = button.data('action')
    var modal = $(this)
    $('#action').val(action)
})
</script>
</body>
</html>
