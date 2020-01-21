<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html lang="pl_PL">
<head>
    <title>Szczegóły hosta</title>
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
            <a href="#editGroupModal" data-toggle="modal" data-target="#editGroupModal" class="btn btn-primary">
                Edytuj grupę
            </a>
            <a href="#removeGroupModal" data-toggle="modal" data-target="#removeGroupModal" class="btn btn-danger">
                Usuń grupę
            </a>
        </div>
    </div>

    <br>

    <div class="row">
        <div class="col-3">
        </div>
        <div class="col-6">
            <c:if test="${error.equals('0')}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    Grupa została zapisana pomyślnie
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </c:if>
            <c:if test="${error.equals('2')}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    Grupa z tą nazwą już istnieje
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </c:if>
            <c:if test="${error.equals('3')}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    Nie można modyfikować grupy domyślnej
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
        <div class="col-12 text-center koturno-style">
            <div class="lead"><strong>Nazwa: ${group.name}</strong></div>
            <div class="lead"><strong>Opis: ${group.description}</strong></div>
        </div>
    </div>

    <br>

    <div class="modal" id="editGroupModal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content koturno-style">
                <div class="modal-header">
                    <h5 class="modal-title">Edycja grupy</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form method="post" action="/group">
                        <input type="hidden" name="originName" value="${group.name}"/>
                        <div class="form-group">
                            <label for="name">Nazwa</label>
                            <input type="text" required name="name" id="name" class="form-control"
                                   value="${group.name}"/>
                        </div>
                        <div class="form-group">
                            <label for="description">Opis</label>
                            <c:if test="${!group.description.equals('')}">
                                <input type="text" name="description" id="description" class="form-control"
                                       value="${group.description}"/>
                            </c:if>
                            <c:if test="${group.description.equals('')}">
                                <input type="text" name="description" id="description" class="form-control"
                                       placeholder="Podaj opis grupy"/>
                            </c:if>
                        </div>
                        <button class="btn btn-success" type="submit">Zapisz</button>
                        <button class="btn btn-secondary" type="reset">Wyczyść pola</button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Anuluj</button>
                        <sec:csrfInput/>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="modal" id="removeGroupModal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content koturno-style">
                <div class="modal-header">
                    <h5 class="modal-title">Usuwanie grupy</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form method="get" action="/group">
                        <input type="hidden" name="id" value="${group.id}"/>
                        <input type="hidden" name="action" value="remove"/>
                        <div class="koturno-style">Usunąć grupę ${group.name}?</div>
                        <br>
                        <button class="btn btn-danger" type="submit">Usuń</button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Anuluj</button>
                        <sec:csrfInput/>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <c:if test="${hosts.size() == 0}">
        <div class="row">
            <div class="col-12 pb-3">
                <p class="h1 text-center koturno-style">Grupa jest pusta</p>
            </div>
        </div>
    </c:if>
    <c:if test="${hosts.size() > 0}">
        <div class="row">
            <div class="col-12 pb-3">
                <table class="table table-hover table-bordered text-center koturno-style">
                    <thead>
                    <tr class="thead-dark">
                        <th>Lp.</th>
                        <th>Nazwa</th>
                        <th>Adres</th>
                        <th>Opis</th>
                        <th>Akcje</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${hosts}" var="host" varStatus="hostStatus">
                        <tr>
                            <td class="align-middle">${hostStatus.count}</td>
                            <td class="align-middle">${host.name}</td>
                            <c:if test="${offlineHosts.contains(host)}">
                                <td class="table-danger align-middle">${host.address}</td>
                            </c:if>
                            <c:if test="${unstableHosts.contains(host)}">
                                <td class="table-warning align-middle">${host.address}</td>
                            </c:if>
                            <c:if test="${host.isActive() && !offlineHosts.contains(host) && !unstableHosts.contains(host)}">
                                <td class="table-success align-middle">${host.address}</td>
                            </c:if>
                            <c:if test="${!host.isActive()}">
                                <td class="table-secondary align-middle">${host.address}</td>
                            </c:if>
                            <td>${host.description}</td>
                            <td>
                                <a href=/host?id=${host.id}&action=info class="btn btn-primary btn-sm">
                                    zobacz
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

<script src="/jquery-3.3.1.slim.min.js"></script>
<script src="/popper.min.js"></script>
<script src="/bootstrap.min.js"></script>
</body>
</html>

