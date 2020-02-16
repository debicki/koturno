<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<footer class="py-5">
    <hr>

    <div class="container koturno-style">
        <jsp:useBean id="now" class="java.util.Date" />
        <fmt:formatDate var="year" value="${now}" pattern="yyyy" />
        <p class="m-0 text-center">Copyright &copy; Łukasz Dębicki ${year}</p>
    </div>
</footer>
