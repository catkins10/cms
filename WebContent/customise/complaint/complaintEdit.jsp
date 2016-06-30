<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

    
        <table border="1" cellpadding="0" cellspacing="0" class="table" width="100%">
            <colgroup><col><col width="50%"><col><col width="50%"></colgroup>
            <tbody>
                <tr>
                    <td class="tdtitle" nowrap>附件</td>
                    <td class="tdcontent" colspan="3"><ext:field property="attachment"/></td>
                </tr>
                <tr>
                    <td class="tdtitle" nowrap>记录ID</td>
                    <td class="tdcontent"><ext:field property="id"/></td>
                    <td class="tdtitle" nowrap>TEST</td>
                    <td class="tdcontent"><ext:field property="TEST"/></td>
                </tr>
                <tr>
                    <td class="tdtitle" nowrap>投诉时间</td>
                    <td class="tdcontent"><ext:field property="complaintTime"/></td>
                    <td class="tdtitle" nowrap>办理时间</td>
                    <td class="tdcontent"><ext:field property="workDay"/></td>
                </tr>
                <tr>
                    <td class="tdtitle" nowrap>投诉人单位</td>
                    <td class="tdcontent"><ext:field property="unit"/></td>
                    <td class="tdtitle" nowrap>投诉人姓名</td>
                    <td class="tdcontent"><ext:field property="name"/></td>
                </tr>
                <tr>
                    <td class="tdtitle" nowrap>地点</td>
                    <td class="tdcontent"><ext:field property="address"/></td>
                    <td class="tdtitle" nowrap>回访人</td>
                    <td class="tdcontent"><ext:field property="returnPerson"/></td>
                </tr>
                <tr>
                    <td class="tdtitle" nowrap>回访内容</td>
                    <td class="tdcontent" colspan="3"><ext:field property="returnContent"/></td>
                </tr>
                <tr>
                    <td class="tdtitle" nowrap>回访时间</td>
                    <td class="tdcontent"><ext:field property="returnTime"/></td>
                    <td class="tdtitle" nowrap>a</td>
                    <td class="tdcontent"><ext:field property="a"/></td>
                </tr>
                <tr>
                    <td class="tdtitle" nowrap>b</td>
                    <td class="tdcontent" colspan="3"><ext:field property="b"/></td>
                </tr>
            </tbody>
        </table>
    
