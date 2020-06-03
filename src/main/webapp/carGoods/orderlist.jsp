<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt"  prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="../css/list.css">
    <title>下单列表</title>
</head>
<body>
<div class="w">
    <div class="list">
        <div class="list-bd">
            <table width="100%" border="0" cellspacing="0" cellpadding="0" align="center">
                <tr>
                    <th style="display:none">编号</th>
                    <th width="33%">编号</th>
                    <th width="33%">价格</th>
                    <th width="33%">操作</th>
                    <%--<th width="10%">数量</th>--%>
                </tr>
                <c:forEach items="${list}" var="item">
                    <tr>
                        <td style="display:none">${item.id}</td>
                        <td>${item.number}</td>
                        <td>${item.price}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/cartController/orderdetail?id=${item.id}">订单详情</a>
                            <a href="${pageContext.request.contextPath}/cartController/teardowndetail?id=${item.id}" onclick="getCartOrder()">拆单</a>
                        </td>

                    </tr>
                </c:forEach>
            </table>
        </div>
    </div>
</div>
<script type="application/javascript">
    getCartOrder = function(id){
        alert("拆单成功！");
    }
</script>
</body>
</html>