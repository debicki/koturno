<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<nav>

    <div class="text-center koturno-style">
        <div class="display-3">
            <strong>Koturno</strong>
        </div>
        <div class="h2">
            Alpha
        </div>
        <sec:authorize access="!isAuthenticated()">
            <div style="font-size: 30px; line-height: 100px;">
                <a href="/login">Zaloguj</a>
                <a href="/register">Zarejestruj</a>
            </div>
        </sec:authorize>
        <sec:authorize access="isAuthenticated()">
            <div style="font-size: 30px; line-height: 100px;">
                <c:if test="${disabledMenuItem.equals('dashboard')}">
                    <a href="/" style="color: gray">Podgląd</a>
                </c:if>
                <c:if test="${!disabledMenuItem.equals('dashboard')}">
                    <a href="/">Podgląd</a>
                </c:if>
                <c:if test="${disabledMenuItem.equals('hosts')}">
                    <a href="/hosts" style="color: gray">Hosty</a>
                </c:if>
                <c:if test="${!disabledMenuItem.equals('hosts')}">
                    <a href="/hosts">Hosty</a>
                </c:if>
                <c:if test="${disabledMenuItem.equals('groups')}">
                    <a href="/groups" style="color: gray">Grupy</a>
                </c:if>
                <c:if test="${!disabledMenuItem.equals('groups')}">
                    <a href="/groups">Grupy</a>
                </c:if>
                <c:if test="${disabledMenuItem.equals('history')}">
                    <a href="/history" style="color: gray">Historia</a>
                </c:if>
                <c:if test="${!disabledMenuItem.equals('history')}">
                    <a href="/history">Historia</a>
                </c:if>
            </div>
        </sec:authorize>
    </div>

</nav>
