<%@ page language="java" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<HTML>
<HEAD>
    <meta http-equiv="Content-Language" content="zh-cn">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link href="${pageContext.request.contextPath}/css/Style1.css" rel="stylesheet" type="text/css"/>
    <script language="javascript" src="${pageContext.request.contextPath}/js/public.js"></script>

</HEAD>
<body>
<br>
<form id="Form1" name="Form1" action="${pageContext.request.contextPath}/user/list.jsp" method="post">
    <table cellSpacing="1" cellPadding="0" width="100%" align="center" bgColor="#f5fafe" border="0">
        <TBODY>
        <tr>
            <td class="ta_01" align="center" bgColor="#afd1f3">
                <strong>用户列表</strong>
            </td>
        </tr>
        <tr>

        </tr>
        <tr>
            <td class="ta_01" align="center" bgColor="#f5fafe">
                <table cellspacing="0" cellpadding="1" rules="all"
                       bordercolor="gray" border="1" id="DataGrid1"
                       style="BORDER-RIGHT: gray 1px solid; BORDER-TOP: gray 1px solid; BORDER-LEFT: gray 1px solid; WIDTH: 100%; WORD-BREAK: break-all; BORDER-BOTTOM: gray 1px solid; BORDER-COLLAPSE: collapse; BACKGROUND-COLOR: #f5fafe; WORD-WRAP: break-word">
                    <tr
                            style="FONT-WEIGHT: bold; FONT-SIZE: 12pt; HEIGHT: 25px; BACKGROUND-COLOR: #afd1f3">

                        <td align="center" width="18%">
                            序号
                        </td>
                        <td align="center" width="17%">
                            用户账号
                        </td>
                        <td align="center" width="17%">
                            用户名称
                        </td>
                        <td width="7%" align="center">
                            编辑
                        </td>
                        <td width="7%" align="center">
                            删除
                        </td>
                    </tr>
                    <c:forEach items="${userList }" var="u" varStatus="vs">
                    <tr onmouseover="this.style.backgroundColor = 'white'"
                        onmouseout="this.style.backgroundColor = '#F5FAFE';">
                        <td style="CURSOR: hand; HEIGHT: 22px" align="center"
                            width="18%">
                            <c:if test="${u.state == 0 }">
                                (已注销)
                            </c:if>
                            <c:if test="${u.state == 1}">
                                ${vs.count }
                            </c:if>

                        </td>
                        <td style="CURSOR: hand; HEIGHT: 22px" align="center"
                            width="17%">
                                ${u.account }
                        </td>
                        <td style="CURSOR: hand; HEIGHT: 22px" align="center"
                            width="17%">
                                ${u.username }
                        </td>
                        <td align="center" style="HEIGHT: 22px">
                            <a href="${ pageContext.request.contextPath }/admin/user/edit?uid=${u.uid}"/>
                            <img src="${pageContext.request.contextPath}/images/i_edit.gif" border="0"
                                 style="CURSOR: hand">
                            </a>
                        </td>
                        <td align="center" style="HEIGHT: 22px">
                            <c:if test="${u.state == 1 }">
                                <a href="${ pageContext.request.contextPath }/admin/user/updateState?uid=${u.uid}&state=0"/>
                                <%-- <img src="${pageContext.request.contextPath}/images/ok.gif" border="0" style="CURSOR: hand"> --%>
                                注销
                                </a>
                            </c:if>
                            <c:if test="${u.state == 0 }">
                                <a href="${ pageContext.request.contextPath }/admin/user/updateState?uid=${u.uid}&state=1"/>
                                <%-- <img src="${pageContext.request.contextPath}/images/i_del.gif" border="0" style="CURSOR: hand"> --%>
                                激活
                                </a>
                            </c:if>

                        </td>
                        </c:forEach>
                </table>
            </td>
        </tr>
        </TBODY>
    </table>
</form>
</body>
</HTML>

