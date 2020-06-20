<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<fmt:setBundle basename="lang"/>

<html lang="pl_PL">
<head>
    <title><fmt:message key="head.title.hosts"/></title>
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

        <div class="modal" id="newHostModal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content koturno-style border border-light rounded">
                    <div class="modal-header koturno-dark text-light">
                        <h5 class="modal-title"><fmt:message key="modal.title.add-host"/></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true" class="text-light">×</span>
                        </button>
                    </div>
                    <div class="modal-body koturno-dark text-light">
                        <form method="post" action="/hosts">
                            <div class="form-row">
                                <div class="form-group col-8">
                                    <label for="address"><fmt:message key="modal.body.address"/></label>
                                    <fmt:message key="form.placeholder.provide-host-address" var="provideHostAddress"/>
                                    <input type="text" required name="address" id="address" class="form-control koturno-dark text-light"
                                           placeholder="${provideHostAddress}" autofocus="autofocus" autocomplete="off"/>
                                </div>
                                <div class="form-group col-4">
                                    <label for="activity"><fmt:message key="modal.body.activity"/></label>
                                    <select name="activity" id="activity" class="form-control koturno-dark text-light">
                                        <option selected><fmt:message key="modal.body.active"/></option>
                                        <option><fmt:message key="modal.body.inactive"/></option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="name"><fmt:message key="modal.body.name"/></label>
                                <fmt:message key="form.placeholder.provide-host-name" var="provideHostName"/>
                                <input type="text" name="name" id="name" class="form-control koturno-dark text-light"
                                       placeholder="${provideHostName}"/>
                            </div>
                            <div class="form-group">
                                <label for="description"><fmt:message key="modal.body.description"/></label>
                                <fmt:message key="form.placeholder.provide-host-description" var="provideHostDescription"/>
                                <input type="text" name="description" id="description" class="form-control koturno-dark text-light"
                                       placeholder="${provideHostDescription}"/>
                            </div>
                            <div class="form-group">
                                <label for="hostGroupName"><fmt:message key="modal.body.group"/></label>
                                <select name="hostGroupName" id="hostGroupName" class="form-control koturno-dark text-light">
                                    <option selected>${hostGroupList.get(0).name}</option>
                                    <c:forEach items="${hostGroupList}" var="hostGroup" varStatus="hostGroupStatus">
                                        <c:if test="${!hostGroup.name.equals('default')}">
                                            <option>${hostGroup.name}</option>
                                        </c:if>
                                    </c:forEach>
                                </select>
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

        <div class="modal" id="importHostsModal" tabindex="-1" role="dialog">
            <div class="modal-dialog" role="document">
                <div class="modal-content koturno-style border border-light rounded">
                    <div class="modal-header koturno-dark text-light">
                        <h5 class="modal-title"><fmt:message key="submenu.label.import-hosts"/></h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true" class="text-light">×</span>
                        </button>
                    </div>
                    <div class="modal-body koturno-dark text-light">
                        <form method="post" enctype="multipart/form-data" action="/hosts/import">
                            <div class="form-group custom-file mb-3">
                                <label for="file" class="custom-file-label koturno-dark text-light"></label>
                                <input type="file" required name="file" id="file" class="form-control custom-file-input"/>
                            </div>
                            <button class="btn btn-outline-primary" type="submit"><fmt:message key="modal.button.import"/></button>
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
                    <fmt:message key="head.title.hosts"/>
                </span></h1>
                </div>
                <div class="col-8"></div>
            </div>

            <div class="row mt-4">
                <div class="col-1">
                </div>
                <div class="col-10" style="min-height: 5rem">
                    <c:if test="${error.equals('host-added')}">
                        <div class="alert koturno-alert-success alert-dismissible fade show" role="alert">
                            <strong>
                                <fmt:message key="messages.title.success"/>
                            </strong> <fmt:message key="messages.information.host-added"/>
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    </c:if>
                    <c:if test="${error.equals('host-exists')}">
                        <div class="alert koturno-alert-danger alert-dismissible fade show" role="alert">
                            <strong>
                                <fmt:message key="messages.title.error"/>
                            </strong> <fmt:message key="messages.error.host-exists"/>
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    </c:if>
                    <c:if test="${error.equals('check-host')}">
                        <div class="alert koturno-alert-warning alert-dismissible fade show" role="alert">
                            <strong>
                                <fmt:message key="messages.title.warning"/>
                            </strong> <fmt:message key="messages.error.check-host"/>
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    </c:if>
                    <c:if test="${error.equals('10')}">
                        <div class="alert koturno-alert-success alert-dismissible fade show" role="alert">
                            <strong>
                                <fmt:message key="messages.title.success"/>
                            </strong> <fmt:message key="messages.information.host-removed"/>
                            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                                <span aria-hidden="true">&times;</span>
                            </button>
                        </div>
                    </c:if>
                    <c:if test="${importSuccess != null}">
                        <div class="alert alert-secondary alert-dismissible fade show" role="alert">
                            <strong><fmt:message key="messages.summary.title"/></strong><br>
                            <fmt:message key="messages.summary.success"/>: ${importSuccess}<br>
                            <fmt:message key="messages.summary.warnings"/>: ${importWarnings}<br>
                            <fmt:message key="messages.summary.errors"/>: ${importErrors}
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
                    <a href="#newHostModal" data-toggle="modal" data-target="#newHostModal" class="btn btn-outline-light">
                        <fmt:message key="submenu.label.new-host"/>
                    </a>
                    <a href="#importHostsModal" data-toggle="modal" data-target="#importHostsModal" class="btn btn-outline-light">
                        <fmt:message key="submenu.label.import-hosts"/>
                    </a>
                </div>
            </div>

            <div class="row mt-4">
                <div class="col-12 pb-3">
                    <c:if test="${hosts == null || hosts.size() == 0}">
                        <p class="h1 koturno-style"><fmt:message key="messages.information.hosts-are-empty"/></p>
                    </c:if>
                    <c:if test="${hosts.size() > 0}">
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
                                    <c:if test="${host.isActive() && offlineHosts.contains(host)}">
                                        <td class="align-middle">
                                            <div class="border-bottom border-top border-danger">${hostStatus.count}</div>
                                        </td>
                                    </c:if>
                                    <c:if test="${host.isActive() && unstableHosts.contains(host)}">
                                        <td class="align-middle">
                                            <div class="border-bottom border-top border-warning">${hostStatus.count}</div>
                                        </td>
                                    </c:if>
                                    <c:if test="${host.isActive() && !offlineHosts.contains(host) && !unstableHosts.contains(host)}">
                                        <td class="align-middle">
                                            <div class="border-bottom border-top border-success">${hostStatus.count}</div>
                                        </td>
                                    </c:if>
                                    <c:if test="${!host.isActive()}">
                                        <td class="align-middle">
                                            <div class="border-bottom border-top border-secondary">${hostStatus.count}</div>
                                        </td>
                                    </c:if>
                                    <td class="align-middle">${host.name}</td>
                                    <td class="align-middle">${host.address}</td>
                                    <td class="align-middle">${host.description}</td>
                                    <td>
                                        <div class="btn-group" role="group">
                                            <button id="hostButton" type="button" class="btn btn-outline-light btn-sm dropdown-toggle koturno-style" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                            <fmt:message key="table.button-label.host"/>
                                            </button>
                                            <div class="dropdown-menu koturno-dark border-light rounded" aria-labelledby="hostButton">
                                                <a class="dropdown-item text-light" href="/host?id=${host.id}&action=info">
                                                    <fmt:message key="table.button-label.details"/>
                                                </a>
                                                <a class="dropdown-item text-light" href="#" onclick="window.open('/ping?address=${host.address}','_blank');return false">
                                                    <fmt:message key="table.button-label.ping"/>
                                                </a>
                                            </div>
                                        </div>
                                    </td>
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

<script src="/jquery-3.3.1.slim.min.js"></script>
<script src="/popper.min.js"></script>
<script src="/bootstrap.min.js"></script>
<script src="/koturno.uploader.js"></script>
</body>
</html>
