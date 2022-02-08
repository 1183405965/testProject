<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
	<head>
		<title></title>

		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="this is my page">
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">

		<link rel="stylesheet" type="text/css" href="css/right.css">

	</head>

	<body>
		<table width="100%" border="0" align="center" cellpadding="0"
			cellspacing="0">
			<tr>
				<td height="30">
					<table width="100%" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td height="24" bgcolor="#353c44">
								<table width="100%" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td>
											<table width="100%" border="0" cellspacing="0"
												cellpadding="0">
												<tr>
													<td width="6%" height="19" valign="bottom">
														<div align="center">
															<img src="images/tb.gif" width="14" height="14" />
														</div>
													</td>
													<td width="94%" valign="bottom">
														<span class="STYLE1"> 添加商品的基本信息</span>
													</td>
												</tr>
											</table>
										</td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</td>
			</tr>
			<tr>
				<td>
					<form action="${pageContext.request.contextPath}/good/GoodServlet" method="post" enctype="multipart/form-data">
						<input type="hidden" name="bs" value="add">
					<table width="100%" border="0" cellpadding="0" cellspacing="1"
						bgcolor="#a8c7ce">
						<tr>
							<td height="20" bgcolor="#FFFFFF">
								<div align="center">
									<span class="STYLE19">
										商品名称
									</span>
								</div>
							</td>
							<td height="20" bgcolor="#FFFFFF" class="STYLE6">
								<div align="left">
									<span class="STYLE19">
										&nbsp;&nbsp;<input type="text" name="pname" class="test"/>
									</span>
								</div>
							</td>
						</tr>
						<tr>
							<td height="20" bgcolor="#FFFFFF">
								<div align="center">
									<span class="STYLE19">
										商品类别
									</span>
								</div>
							</td>
							<td height="20" bgcolor="#FFFFFF" class="STYLE6">
								<div align="left">
									<span class="STYLE19">
										&nbsp;&nbsp;<input type="text" name="ptype" class="test"/>
									</span>
								</div>
							</td>
						</tr>
						<tr>
							<td height="20" bgcolor="#FFFFFF">
								<div align="center">
									<span class="STYLE19">
										商品单价
									</span>
								</div>
							</td>
							<td height="20" bgcolor="#FFFFFF" class="STYLE6">
								<div align="left">
									<span class="STYLE19">
										&nbsp;&nbsp;<input type="text" name="pprice" class="test"/>
									</span>
								</div>
							</td>
						</tr>
						<tr>
							<td height="20" bgcolor="#FFFFFF">
								<div align="center">
									<span class="STYLE19">
										商品图片
									</span>
								</div>
							</td>
							<td height="20" bgcolor="#FFFFFF" class="STYLE6">
								<div align="left">
									<span class="STYLE19">
										&nbsp;&nbsp;<input type="file" name="pimg" class="test"/>
									</span>
								</div>
							</td>
						</tr>
						<tr>
							<td height="20" bgcolor="#FFFFFF">
								<div align="center">
									<span class="STYLE19">
										&nbsp;
									</span>
								</div>
							</td>
							<td height="20" bgcolor="#FFFFFF" class="STYLE6">
								<div align="left">
									<span class="STYLE19">
										&nbsp;
										<input type="submit" value="添加商品" style="border: 1px solid #006699;"/>
										<input type="reset" value="重填信息" style="border: 1px solid #006699;"/>
									</span>
									<p>${errinfo}</p>
								</div>
							</td>
						</tr>
					</table>
					</form>
				</td>
			</tr>
		</table>
	</body>
</html>
