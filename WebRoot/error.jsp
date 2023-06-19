<%@ page contentType="text/html; charset=UTF-8"%>
<%@ page isErrorPage="true"%>
<HTML>
<script Language="JavaScript">

</script>
<HEAD>
	<TITLE>系统提示</TITLE>
	<META content="text/html; charset=UTF-8" http-equiv=Content-Type>
</HEAD>
<body leftmargin="0" topmargin="0" marginwidth="0" marginheight="0" bgcolor="#ffffff" style="text-align: center">
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
    <br>
	<center>
		<div style="margin:0 auto;text-align:center;width:685px;height:260px;background:url(<%=request.getContextPath()%>/jsp/common/images/error_bj.gif);"></div>
		<div align="center" style="margin-top:-170px;">
			<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
				<tr>
					<td height="50" align="center"><span id="tip"><font style="color:blue">提醒:</font></span></td>
				</tr>
				<tr>
					<td>
						<table width="100%" border="0" align="center" bordercolordark="#FFFFFF" bordercolorlight="#cccccc" cellpadding="25" cellspacing="0" height="100%">
							<tr>
								<td height="30" nowrap align="center" valign="middle">&nbsp;&nbsp;
									<font color="blue">系统正忙请重试										
 									</font>
								<!--<td height="100%" width="100%">
         						</td>-->
								</td>
							</tr>
						</table>
					 </td>
			      </tr>
			</table>
		</div>
	</center>
</body>

</HTML>
