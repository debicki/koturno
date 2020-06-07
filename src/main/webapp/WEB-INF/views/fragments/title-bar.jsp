<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<fmt:setBundle basename="lang"/>

<nav class="navbar sticky-top navbar-expand-lg navbar-dark koturno-darkest">
    <a class="navbar-brand" href="/">
        <svg class="bi bi-exclude" width="32" height="32" viewBox="0 0 16 16" fill="currentColor"
        xmlns="http://www.w3.org/2000/svg">
        <path fill-rule="evenodd"
            d="M1.5 0A1.5 1.5 0 0 0 0 1.5v9A1.5 1.5 0 0 0 1.5 12H4v2.5A1.5 1.5 0 0 0 5.5 16h9a1.5 1.5 0 0 0 1.5-1.5v-9A1.5 1.5 0 0 0 14.5 4H12V1.5A1.5 1.5 0 0 0 10.5 0h-9zM12 4H5.5A1.5 1.5 0 0 0 4 5.5V12h6.5a1.5 1.5 0 0 0 1.5-1.5V4z" />
        </svg>
        <span class="font-weight-lighter koturno-style">Koturno <fmt:message key="koturno.version"/></span>
    </a>
    <div class="navbar-nav ml-auto koturno-style">
        <c:if test="${loggedUser == null}">
            <span class="navbar-text mr-3">
                <fmt:message key="menu.label.no-logged"/>
            </span>
        </c:if>
        <c:if test="${loggedUser != null}">
            <span class="navbar-text mr-3">
                <fmt:message key="menu.label.logged-in-as"/> ${loggedUser}
            </span>
        </c:if>
        <c:if test="${firstUser == true}">
            <a class="nav-item nav-link btn btn-outline-light text-light" href="/register">
                <fmt:message key="menu.label.register"/>
            </a>
        </c:if>
        <c:if test="${firstUser == false}">
            <sec:authorize access="!isAuthenticated()">
                <a class="nav-item nav-link btn btn-outline-light text-light" href="/login">
                    <fmt:message key="menu.label.login"/>
                </a>
            </sec:authorize>
            <sec:authorize access="isAuthenticated()">
                <a class="nav-item nav-link btn btn-outline-light text-light" href="/logout">
                    <fmt:message key="menu.label.logout"/>
                </a>
            </sec:authorize>
        </c:if>
    </div>
</nav>
