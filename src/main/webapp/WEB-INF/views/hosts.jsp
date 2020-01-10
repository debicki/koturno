<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html lang="pl_PL">
<head>
    <title>Hosty</title>
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
        <div class="col-3">
        </div>
        <div class="col-6">
            <c:if test="${error.equals('0')}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    Host został dodany pomyślnie
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
            <c:if test="${error.equals('3')}">
                <div class="alert alert-warning alert-dismissible fade show" role="alert">
                    Host został dodany, lecz adres może zawierać błędy<br>
                    Sprawdź wpisany adres IP lub połączenie z serwerem DNS
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </c:if>
            <c:if test="${error.equals('10')}">
                <div class="alert alert-success alert-dismissible fade show" role="alert">
                    Host został usunięty pomyślnie
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
            </c:if>
            <c:if test="${importSuccess != null}">
                <div class="alert alert-secondary alert-dismissible fade show" role="alert">
                    Raport z importu hostów:<br>
                    - dodanych i rozpoznanych przez DNS hostów: ${importSuccess}<br>
                    - dodanych, lecz nierozpoznanych przed DNS hostów: ${importWarnings}<br>
                    - pominiętych z powodu istnienia w bazie danych hostów: ${importErrors}
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
            <a href="#newHostModal" data-toggle="modal" data-target="#newHostModal" class="btn btn-primary">
                Nowy host
            </a>
            <a href="#importHostsModal" data-toggle="modal" data-target="#importHostsModal" class="btn btn-primary">
                Importuj hosty
            </a>
        </div>
    </div>

    <br>

    <div class="modal" id="newHostModal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content koturno-style">
                <div class="modal-header">
                    <h5 class="modal-title">Nowy host</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form method="post" action="/hosts">
                        <div class="form-row">
                            <div class="form-group col-8">
                                <label for="address">Adres</label>
                                <input type="text" required name="address" id="address" class="form-control" placeholder="Podaj adres hosta"/>
                            </div>
                            <div class="form-group col-4">
                                <label for="activity">Aktywność</label>
                                <select name="activity" id="activity" class="form-control">
                                    <option selected>Aktywny</option>
                                    <option>Nieaktywny</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="name">Nazwa</label>
                            <input type="text" name="name" id="name" class="form-control" placeholder="Podaj nazwę hosta"/>
                        </div>
                        <div class="form-group">
                            <label for="description">Opis</label>
                            <input type="text" name="description" id="description" class="form-control" placeholder="Podaj opis hosta"/>
                        </div>
                        <div class="form-group">
                            <label for="hostGroupName">Grupa</label>
                            <select name="hostGroupName" id="hostGroupName" class="form-control">
                                <option selected>${hostGroupList.get(0).name}</option>
                                <c:forEach items="${hostGroupList}" var="hostGroup" varStatus="hostGroupStatus">
                                    <c:if test="${!hostGroup.name.equals('default')}">
                                        <option>${hostGroup.name}</option>
                                    </c:if>
                                </c:forEach>
                            </select>
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

    <div class="modal" id="importHostsModal" tabindex="-1" role="dialog">
        <div class="modal-dialog" role="document">
            <div class="modal-content koturno-style">
                <div class="modal-header">
                    <h5 class="modal-title">Importuj hosty</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">×</span>
                    </button>
                </div>
                <div class="modal-body">
                    <form method="post" enctype="multipart/form-data" action="/hosts/import">
                        <div class="form-group">
                            <label for="file">Plik importu</label>
                            <input type="file" required name="file" id="file" class="form-control"/>
                        </div>
                        <button class="btn btn-success" type="submit">Importuj</button>
                        <button class="btn btn-secondary" type="reset">Wyczyść pola</button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Anuluj</button>
                        <sec:csrfInput/>
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
                        <c:if test="${offlineHosts.contains(host)}">
                            <td class="table-danger">${host.address}</td>
                        </c:if>
                        <c:if test="${unstableHosts.contains(host)}">
                            <td class="table-warning">${host.address}</td>
                        </c:if>
                        <c:if test="${host.isActive() && !offlineHosts.contains(host) && !unstableHosts.contains(host)}">
                            <td class="table-success">${host.address}</td>
                        </c:if>
                        <c:if test="${!host.isActive()}">
                            <td class="table-secondary">${host.address}</td>
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
