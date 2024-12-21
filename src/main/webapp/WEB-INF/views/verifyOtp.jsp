<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<fmt:setLocale value="vi_VN"/>
<html>
<head>
    <title>verifyOtp</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/verifyOTPStep2" method="post">
    <input type="text" name="otp" placeholder="nhap ma otp tu gmail">
    <button type="submit"> Xac thuc otp </button>
</form>
</body>
</html>
