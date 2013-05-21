<#list resultPage.result as customer>
BEGIN:VCARD
VERSION:3.0
N:<#if customer.linkman?has_content>${customer.linkman!}<#else>${customer.name!}</#if>
<#if customer.mobile?has_content>
<#list customer.mobile?split(' ') as a>
<#list a?split(',') as b>
TEL;TYPE=CELL:${b}
</#list>
</#list>
</#if>
<#if customer.phone?has_content>
<#list customer.phone?split(' ') as a>
<#list a?split(',') as b>
TEL;TYPE=WORK:${b}
</#list>
</#list>
</#if>
<#if customer.region?? || customer.address?has_content>
ADR:<#if customer.region??>${customer.region.fullname}</#if>${customer.address!}
</#if>
ORG:${customer.name!}
<#if customer.memo?has_content>
NOTE:${customer.memo!}
</#if>
END:VCARD
</#list>