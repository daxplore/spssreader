<?xml version="1.0" encoding="UTF-8"?>
<!--
Converts an XML doucment into HTML 

Author: Pascal Heus (pascal.heus@gmail.com)
Version: 2007.02
Platform: XSL 1.0

License: 
	Copyright 2006 Pascal Heus (pascal.heus@gmail.com)

    This program is free software; you can redistribute it and/or modify it under the terms of the
    GNU Lesser General Public License as published by the Free Software Foundation; either version
    2.1 of the License, or (at your option) any later version.
  
    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
    without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Lesser General Public License for more details.
  
    The full text of the license is available at http://www.gnu.org/copyleft/lesser.html
-->
<xsl:stylesheet version="1.0"
	xmlns="http://www.w3.org/1999/xhtml"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	>
	
	<xsl:output method="html" encoding="UTF-8"/>
	
	<xsl:template match="/">
		<xsl:call-template name="xml2html">
			<div>
				<xsl:with-param name="node" select="*"/>
			</div>
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="xml2html">
		<xsl:param name="node"/>
		<xsl:param name="level" select="0"/>
		
		<xsl:variable name="indent" select="$level * 15"/>
		<xsl:variable name="sLabelDefaultStyle">color:blue;</xsl:variable>
		<xsl:variable name="sLabelElementNameStyle">color:#400;</xsl:variable>
		<xsl:variable name="sLabelAttributeNameStyle">color:red;</xsl:variable>
		<xsl:variable name="sLabelAttributeValueStyle">color:black;</xsl:variable>
		<xsl:variable name="sLabelTextStyle">color:black;</xsl:variable>
		<xsl:variable name="nLabelTextMaxLength">100</xsl:variable>

		<xsl:for-each select="$node">
			<xsl:if test="(name()='Gate' or name()='Circuit') and name(..)='QIS'"><hr/></xsl:if>
			<xsl:choose>
				<xsl:when test="./* or normalize-space($node/text())">
					<!-- node with children or text -->
					<div style="{$sLabelDefaultStyle};margin-left:{$indent}px;">
						<xsl:text>&lt;</xsl:text>
						<span style="{$sLabelElementNameStyle}"><xsl:value-of select="name()"/></span>
						<!-- node attributes -->
						<xsl:for-each select="@*">
							<xsl:text> </xsl:text>
							<span style="{$sLabelAttributeNameStyle}"><xsl:value-of select="name()"/></span>
							<xsl:text>="</xsl:text>
							<span style="{$sLabelAttributeValueStyle}"><xsl:value-of select="."/></span>
							<xsl:text>"</xsl:text>
						</xsl:for-each>
						<!-- node text -->
						<xsl:text>&gt;</xsl:text>
						<xsl:value-of select="normalize-space($node/text())"/>
					</div>
					<!-- children -->
					<xsl:for-each select="./*">
						<xsl:call-template name="xml2html">
							<xsl:with-param name="node" select="."/>
							<xsl:with-param name="level" select="$level + 1"/>
						</xsl:call-template>
					</xsl:for-each>
					<!-- close node -->
					<div style="{$sLabelDefaultStyle};margin-left:{$indent}px;">
						<xsl:text>&lt;/</xsl:text>
						<span style="{$sLabelElementNameStyle}"><xsl:value-of select="name($node)"/></span>
						<xsl:text>&gt;</xsl:text>
					</div>
				</xsl:when>
				<xsl:otherwise>
					<!-- node has no children or text -->
					<div style="{$sLabelDefaultStyle};margin-left:{$indent}px;">
						<xsl:text>&lt;</xsl:text>
						<span style="{$sLabelElementNameStyle}"><xsl:value-of select="name()"/></span>
						<!-- node attributes -->
						<xsl:for-each select="@*">
							<xsl:text> </xsl:text>
							<span style="{$sLabelAttributeNameStyle}"><xsl:value-of select="name()"/></span>
							<xsl:text>="</xsl:text>
							<span style="{$sLabelAttributeValueStyle}"><xsl:value-of select="."/></span>
							<xsl:text>"</xsl:text>
						</xsl:for-each>
						<xsl:text> /&gt;</xsl:text>
					</div>
				</xsl:otherwise>
			</xsl:choose>
		</xsl:for-each>
	</xsl:template>
</xsl:stylesheet>
