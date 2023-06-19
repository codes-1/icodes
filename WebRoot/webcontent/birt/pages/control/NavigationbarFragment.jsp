<%-----------------------------------------------------------------------------
	Copyright (c) 2004 Actuate Corporation and others.
	All rights reserved. This program and the accompanying materials 
	are made available under the terms of the Eclipse Public License v1.0
	which accompanies this distribution, and is available at
	http://www.eclipse.org/legal/epl-v10.html
	
	Contributors:
		Actuate Corporation - Initial implementation.
-----------------------------------------------------------------------------%>
<%@ page contentType="text/html; charset=utf-8" %>
<%@ page session="false" buffer="none" %>
<%@ page import="org.eclipse.birt.report.presentation.aggregation.IFragment,
				 org.eclipse.birt.report.context.BaseAttributeBean,
				 org.eclipse.birt.report.utility.ParameterAccessor,
				 org.eclipse.birt.report.resource.BirtResources" %>

<%-----------------------------------------------------------------------------
	Expected java beans
-----------------------------------------------------------------------------%>
<jsp:useBean id="fragment" type="org.eclipse.birt.report.presentation.aggregation.IFragment" scope="request" />
<jsp:useBean id="attributeBean" type="org.eclipse.birt.report.context.BaseAttributeBean" scope="request" />

<%-----------------------------------------------------------------------------
	Navigation bar fragment
-----------------------------------------------------------------------------%>
<% String imagesPath = "birt/images/"; %>
<TR style="background-color:#D3E2E5;color:#000000;font-family:Tahoma;font-size:12px;">
	<TD>
		<DIV ID="toolbar" style="float:left !important;">
			<TABLE CELLSPACING="1px" CELLPADDING="1px" border="0">
				<TR>
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
					   <INPUT TYPE="image" NAME='parameter' SRC="birt/images/Report_parameters.gif"
					   		TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.parameter" )%>"	
					   		ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.parameter" )%>" CLASS="birtviewer_clickable">
					</TD>
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
					   <INPUT TYPE="image" NAME='exportReport' SRC="birt/images/ExportReport.gif"
					   		TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.exportreport" )%>"
					   		ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.exportreport" )%>" CLASS="birtviewer_clickable">
					</TD>
					<TD WIDTH="6px"/>
					<TD WIDTH="15px">
					   <INPUT TYPE="image" NAME='print' SRC="birt/images/Print.gif"
					   		TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.print" )%>"
					   		ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.toolbar.print" )%>" CLASS="birtviewer_clickable">
					</TD>									
					<TD WIDTH="6px"/>
				</TR>
			</TABLE>
		</DIV>
		<DIV id="navigationBar">
			<TABLE CELLSPACING="0" CELLPADDING="0" border="0">
				<TR>
					<TD style="display:none;">
						<%
							if ( attributeBean.getBookmark( ) != null )
							{
						%>
							<SPAN ID='pageNumber'></SPAN>
							<div ID='totalPage'></div>
						<%
							}
							else
							{
						%>
							<SPAN ID='pageNumber'><%= ""+attributeBean.getReportPage( ) %></SPAN>
							<div ID='totalPage'></div>
						<%
							}
						%>
					</TD>
					<TD WIDTH="15px" style="padding-top:3px;">
						<INPUT TYPE="image" SRC="<%= imagesPath + (attributeBean.isRtl()?"LastPage":"FirstPage") + "_disabled.gif" %>" NAME='first'
							ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.navbar.first" )%>" 
							TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.navbar.first" )%>" CLASS="birtviewer_clickable">
					</TD>
					<TD WIDTH="2px">&nbsp;</TD>
					<TD WIDTH="15px" style="padding-top:3px;">
						<INPUT TYPE="image" SRC="<%= imagesPath + (attributeBean.isRtl()?"NextPage":"PreviousPage") + "_disabled.gif" %>" NAME='previous' 
							ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.navbar.previous" )%>" 
							TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.navbar.previous" )%>" CLASS="birtviewer_clickable">
					</TD>
					<TD WIDTH="2px">&nbsp;</TD>
					<TD WIDTH="15px" style="padding-top:3px;">
						<INPUT TYPE="image" SRC="<%= imagesPath + (attributeBean.isRtl()?"PreviousPage":"NextPage") + "_disabled.gif" %>" NAME='next'
						    ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.navbar.next" )%>" 
							TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.navbar.next" )%>" CLASS="birtviewer_clickable">
					</TD>
					<TD WIDTH="2px">&nbsp;</TD>
					<TD WIDTH="15px" style="padding-top:3px;">
						<INPUT TYPE="image" SRC="<%= imagesPath + (attributeBean.isRtl()?"FirstPage":"LastPage") + "_disabled.gif" %>" NAME='last'
						    ALT="<%= BirtResources.getHtmlMessage( "birt.viewer.navbar.last" )%>"
							TITLE="<%= BirtResources.getHtmlMessage( "birt.viewer.navbar.last" )%>" CLASS="birtviewer_clickable">
					</TD>
					<TD WIDTH="8px">&nbsp;</TD>
					<TD ALIGN="right">
						<INPUT ID='gotoPage' TYPE='text' VALUE='' MAXLENGTH="3" CLASS="birtviewer_navbar_input" style="text-align:center;width:20px;">
					</TD>
					<TD WIDTH="3px">&nbsp;/</TD>
					<TD ALIGN="right" WIDTH="10px">
						<div id="mypmPageSize"></div>
					</TD>
					<TD WIDTH="10px">&nbsp;</TD>
					<TD ><div id="mypmMessage" style="font-family: Tahoma;font-weight: bold;font-size:12px;color:blue;text-align:left;"></div></TD>
				</TR>
			</TABLE>
		</DIV>
	</TD>
</TR>
