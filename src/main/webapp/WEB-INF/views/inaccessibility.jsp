<%--
  Created by IntelliJ IDEA.
  User: sacull
  Date: 10.11.2019
  Time: 15:33
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
        <div class="col-12 text-center koturno-style">
            <div class="h1">${inaccessibility.host.name}</div>
            <div class="h2">${inaccessibility.host.address}</div>
            <div class="lead"><strong>Data początku: ${inaccessibility.dayOfBegin}</strong></div>
            <div class="lead"><strong>Godzina początku: ${inaccessibility.hourOfBegin}</strong></div>
            <c:if test="${inaccessibility.start == inaccessibility.end}">
                <div class="lead"><strong>Data końca: TRWA</strong></div>
                <div class="lead"><strong>Godzina końca: TRWA</strong></div>
            </c:if>
            <c:if test="${inaccessibility.start != inaccessibility.end}">
                <div class="lead"><strong>Data końca: ${inaccessibility.dayOfEnd}</strong></div>
                <div class="lead"><strong>Godzina końca: ${inaccessibility.hourOfEnd}</strong></div>
            </c:if>
            <div class="lead"><strong>Opis: ${inaccessibility.description}</strong></div>
        </div>
    </div>

    <div class="row">
        <div class="col-12 text-center koturno-style">
            <a href="#editInaccessibilityModal" data-toggle="modal" data-target="#editInaccessibilityModal" class="btn btn-primary">
                Edycja opisu
            </a>
        </div>
    </div>

    <div class="modal" id="editInaccessibilityModal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content koturno-style">
                <div class="modal-header">
                    <h5 class="modal-title">Edycja opisu</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form method="post" action="/inaccessibility">
                        <input type="hidden" name="id" value="${inaccessibility.id}"/>
                        <div class="form-group">
                            <label for="description">Opis</label>
                            <c:if test="${!inaccessibility.description.equals('')}">
                                <input type="text" name="description" id="description" class="form-control" value="${inaccessibility.description}"/>
                            </c:if>
                            <c:if test="${inaccessibility.description.equals('')}">
                                <input type="text" name="description" id="description" class="form-control" placeholder="Podaj opis"/>
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

</div>

<script src="/jquery-3.3.1.slim.min.js"></script>
<script src="/popper.min.js"></script>
<script src="/bootstrap.min.js"></script>
</body>
</html>

