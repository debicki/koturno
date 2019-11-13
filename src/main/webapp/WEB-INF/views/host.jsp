<%--
  Created by IntelliJ IDEA.
  User: sacull
  Date: 10.11.2019
  Time: 12:17
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
                    Host został zapisany pomyślnie
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </c:if>
            <c:if test="${error.equals('1')}">
                <div class="alert alert-danger alert-dismissible fade show" role="alert">
                    Host z tym adresem już istnieje
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
            <div class="h1">${host.name}</div>
            <div class="h2">${host.address}</div>
            <div class="lead">
                <strong>Grupa: <a href=/group?id=${host.hostGroup.id}&action=info>${host.hostGroup.name}</a></strong>
            </div>
            <div class="lead"><strong>Dodany dnia: ${host.dayWhenCreated}</strong></div>
            <c:if test="${host.isActive()}">
                <div class="lead"><strong>Status: aktywny</strong></div>
            </c:if>
            <c:if test="${!host.isActive()}">
                <div class="lead"><strong>Status: nieaktywny</strong></div>
            </c:if>
            <div class="lead"><strong>Opis: ${host.description}</strong></div>
        </div>
    </div>

    <div class="row">
        <div class="col-12 text-center koturno-style">
            <a href="#editHostModal" data-toggle="modal" data-target="#editHostModal" class="btn btn-primary">
                Edycja hosta
            </a>
        </div>
    </div>

    <br>

    <div class="modal" id="editHostModal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content koturno-style">
                <div class="modal-header">
                    <h5 class="modal-title">Edycja hosta</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form method="post" action="/host">
                        <input type="hidden" name="originAddress" value="${host.address}"/>
                        <div class="form-row">
                            <div class="form-group col-8">
                                <label for="address">Adres</label>
                                <input type="text" required name="address" id="address" class="form-control" value="${host.address}"/>
                            </div>
                            <div class="form-group col-4">
                                <label for="activity">Aktywność</label>
                                <select name="activity" id="activity" class="form-control">
                                    <c:if test="${host.isActive()}">
                                        <option selected>Aktywny</option>
                                        <option>Nieaktywny</option>
                                    </c:if>
                                    <c:if test="${!host.isActive()}">
                                        <option selected>Nieaktywny</option>
                                        <option>Aktywny</option>
                                    </c:if>
                                </select>
                            </div>
                        </div><div class="form-group">
                            <label for="name">Nazwa</label>
                            <c:if test="${!host.name.equals('')}">
                                <input type="text" name="name" id="name" class="form-control" value="${host.name}"/>
                            </c:if>
                            <c:if test="${host.name.equals('')}">
                                <input type="text" name="name" id="name" class="form-control" placeholder="Podaj nazwę hosta"/>
                            </c:if>
                    </div>
                        <div class="form-group">
                            <label for="description">Opis</label>
                            <c:if test="${!host.description.equals('')}">
                                <input type="text" name="description" id="description" class="form-control" value="${host.description}"/>
                            </c:if>
                            <c:if test="${host.description.equals('')}">
                                <input type="text" name="description" id="description" class="form-control" placeholder="Podaj opis hosta"/>
                            </c:if>
                        </div>
                        <div class="form-group">
                            <label for="hostGroupName">Grupa</label>
                            <select name="hostGroupName" id="hostGroupName" class="form-control">
                                <option selected>${host.hostGroup.name}</option>
                                <c:forEach items="${hostGroupList}" var="hostGroup" varStatus="hostGroupStatus">
                                    <c:if test="${!hostGroup.name.equals(host.hostGroup.name)}">
                                        <option>${hostGroup.name}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
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
            <table class="table table-hover table-bordered text-center koturno-style">
                <thead>
                <tr class="thead-dark">
                    <th>Lp.</th>
                    <th colspan="2">Początek</th>
                    <th colspan="2">Koniec</th>
                    <th>Opis</th>
                    <th>Akcje</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${inaccessibilityList}" var="inaccessibility" varStatus="inaccessibilityStatus">
                    <tr>
                        <td>${inaccessibilityStatus.count}</td>
                        <td>${inaccessibility.dayOfBegin}</td>
                        <td>${inaccessibility.hourOfBegin}</td>
                        <c:if test="${inaccessibility.start == inaccessibility.end}">
                            <td colspan="2">TRWA</td>
                        </c:if>
                        <c:if test="${inaccessibility.start != inaccessibility.end}">
                            <td>${inaccessibility.dayOfEnd}</td>
                            <td>${inaccessibility.hourOfEnd}</td>
                        </c:if><td>${inaccessibility.description}</td>
                        <td>
                            <a href=/inaccessibility?id=${inaccessibility.id}&action=info>
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
