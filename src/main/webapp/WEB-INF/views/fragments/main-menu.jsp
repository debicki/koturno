<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<nav>

    <div class="text-center koturno-style">
        <div class="display-3">
            <strong>Koturno</strong>
        </div>
        <div class="h2">
            Beta
        </div>
        <sec:authorize access="!isAuthenticated()">
            <div style="font-size: 30px; line-height: 100px;">
                <a href="/login" class="btn btn-primary btn-lg">Zaloguj</a>
                <a href="/register" class="btn btn-primary btn-lg">Zarejestruj</a>
            </div>
        </sec:authorize>
        <sec:authorize access="isAuthenticated()">
            <div style="font-size: 30px; line-height: 100px;">
                <c:if test="${disabledMenuItem.equals('dashboard')}">
                    <a href="/" class="btn btn-secondary btn-lg">Podgląd</a>
                </c:if>
                <c:if test="${!disabledMenuItem.equals('dashboard')}">
                    <a href="/" class="btn btn-primary btn-lg">Podgląd</a>
                </c:if>
                <c:if test="${disabledMenuItem.equals('hosts')}">
                    <a href="/hosts" class="btn btn-secondary btn-lg">Hosty</a>
                </c:if>
                <c:if test="${!disabledMenuItem.equals('hosts')}">
                    <a href="/hosts" class="btn btn-primary btn-lg">Hosty</a>
                </c:if>
                <c:if test="${disabledMenuItem.equals('groups')}">
                    <a href="/groups" class="btn btn-secondary btn-lg">Grupy</a>
                </c:if>
                <c:if test="${!disabledMenuItem.equals('groups')}">
                    <a href="/groups" class="btn btn-primary btn-lg">Grupy</a>
                </c:if>
                <c:if test="${disabledMenuItem.equals('history')}">
                    <a href="/history" class="btn btn-secondary btn-lg">Historia</a>
                </c:if>
                <c:if test="${!disabledMenuItem.equals('history')}">
                    <a href="/history" class="btn btn-primary btn-lg">Historia</a>
                </c:if>
                <a href="/logout" class="btn btn-primary btn-lg ml-5">Wyloguj</a>
            </div>
        </sec:authorize>
    </div>

</nav>
