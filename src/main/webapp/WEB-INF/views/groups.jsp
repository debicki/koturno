<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html lang="pl_PL">
<head>
    <title>Grupy</title>
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
            <a href="#newGroupModal" data-toggle="modal" data-target="#newGroupModal" class="btn btn-primary">
                Nowa grupa
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
                    Grupa została utworzona pomyślnie
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
            <c:if test="${error.equals('10')}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    Grupa została usunięta pomyślnie
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </c:if>
            <c:if test="${error.equals('11')}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    Nie można usunąć grupę zawierającą hosty
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </c:if>
            <c:if test="${error.equals('12')}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    Nie można usunąć grupy domyślnej
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </c:if>
        </div>
        <div class="col-3">
        </div>
    </div>

    <div class="modal" id="newGroupModal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content koturno-style">
                <div class="modal-header">
                    <h5 class="modal-title">Nowa grupa</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form method="post" action="/groups">
                        <div class="form-group">
                            <label for="name">Nazwa</label>
                            <input type="text" required name="name" id="name" class="form-control"
                                   placeholder="Podaj nazwę grupy" autofocus="autofocus"/>
                        </div>
                        <div class="form-group">
                            <label for="description">Opis</label>
                            <input type="text" name="description" id="description" class="form-control"
                                   placeholder="Podaj opis grupy"/>
                        </div>
                        <button class="btn btn-success" type="submit">Dodaj</button>
                        <button class="btn btn-secondary" type="reset">Wyczyść pola</button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Anuluj</button>
                        <sec:csrfInput/>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-12 pb-3">
           <table class="table table-hover table-bordered text-center koturno-style">
                <thead>
                <tr class="thead-dark">
                    <th>Lp.</th>
                    <th>Nazwa</th>
                    <th>Opis</th>
                    <th>Liczba hostów</th>
                    <th>Akcje</th>
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
                            <a href=/group?id=${group.id}&action=info class="btn btn-primary btn-sm">
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
