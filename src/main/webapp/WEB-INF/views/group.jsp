<%--
  Created by IntelliJ IDEA.
  User: sacull
  Date: 10.11.2019
  Time: 15:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html lang="pl_PL">
<head>
    <title>Szczegóły hosta</title>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <link rel="stylesheet" href="/koturno.main.css"/>
    <link rel="stylesheet" href="/bootstrap.min.css"/>
</head>
<body>
<jsp:include page="fragments/main-menu.jsp"/>

<div class="container">

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

    <div class="row">
        <div class="col-12 koturno-style">
            <a href="#editGroupModal" data-toggle="modal" data-target="#editGroupModal" class="btn btn-primary">
                Edycja grupy
            </a>
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
                            <input type="text" required name="name" id="name" class="form-control" value="${group.name}"/>
                        </div>
                        <div class="form-group">
                            <label for="description">Opis</label>
                            <c:if test="${!group.description.equals('')}">
                                <input type="text" name="description" id="description" class="form-control" value="${group.description}"/>
                            </c:if>
                            <c:if test="${group.description.equals('')}">
                                <input type="text" name="description" id="description" class="form-control" placeholder="Podaj opis grupy"/>
                            </c:if>
                        </div>
                        <button class="btn btn-success" type="submit">Zapisz</button>
                        <button class="btn btn-secondary" type="reset">Wyczyść pola</button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Anuluj</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-12" style="padding-bottom: 20px">
            <table class="table table-hover table-bordered koturno-style">
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
                        <td>${hostStatus.count}</td>
                        <td>${host.name}</td>
                        <c:if test="${host.isActive()}">
                            <td class="table-success">
                                <a href=/host?id=${host.id}&action=info>
                                        ${host.address}
                                </a>
                            </td>
                        </c:if>
                        <c:if test="${!host.isActive()}">
                            <td class="table-secondary">
                                <a href=/host?id=${host.id}&action=info>
                                        ${host.address}
                                </a>
                            </td>
                        </c:if>
                        <td>${host.description}</td>
                        <td>
                            <a href=/host?id=${host.id}&action=info>
                                zobacz
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

</div>

<script src="/jquery-3.3.1.slim.min.js"></script>
<script src="/popper.min.js"></script>
<script src="/bootstrap.min.js"></script>
</body>
</html>

