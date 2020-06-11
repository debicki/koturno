<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<fmt:setBundle basename="lang"/>

<div class="koturno-darkest koturno-style text-secondary border-top border-secondary">
    <jsp:useBean id="now" class="java.util.Date" />
    <fmt:formatDate var="year" value="${now}" pattern="yyyy" />
    <p class="m-0 text-center">Copyright &copy; Łukasz Dębicki ${year}</p>
</div>
