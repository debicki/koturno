<%--
  Created by IntelliJ IDEA.
  User: sacull
  Date: 08.11.2019
  Time: 22:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<nav>

    <div style="color: black; font-variant: small-caps; text-align: center;">
        <div class="display-2">
            <strong>Koturno</strong>
        </div>
        <div class="h1">
            Alpha
        </div>
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
    </div>

</nav>
