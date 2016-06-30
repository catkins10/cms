<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="/WEB-INF/struts-ext" prefix="ext" %>

    
        <table border="1" cellpadding="0" cellspacing="0" class="table" width="100%">
            <colgroup><col><col width="33%"><col><col width="33%"><col><col width="33%"></colgroup>
            <tbody>
                <tr>
                    <td class="tdtitle" nowrap>公开的主题</td>
                    <td class="tdcontent"><ext:field property="publicSubject"/></td>
                    <td class="tdtitle" nowrap>公布办理流程</td>
                    <td class="tdcontent"><ext:field property="publicWorkflow"/></td>
                    <td class="tdtitle" nowrap>公布正文</td>
                    <td class="tdcontent"><ext:field property="publicBody"/></td>
                </tr>
                <tr>
                    <td class="tdtitle" nowrap>是否公布到网站</td>
                    <td class="tdcontent"><ext:field property="publicPass"/></td>
                    <td class="tdtitle" nowrap>附注</td>
                    <td class="tdcontent"><ext:field property="remark"/></td>
                    <td class="tdtitle" nowrap>是否允许公开</td>
                    <td class="tdcontent"><ext:field property="isPublic"/></td>
                </tr>
                <tr>
                    <td class="tdtitle" nowrap>邮政编码</td>
                    <td class="tdcontent"><ext:field property="creatorPostalcode"/></td>
                    <td class="tdtitle" nowrap>创建人地址</td>
                    <td class="tdcontent"><ext:field property="creatorAddress"/></td>
                    <td class="tdtitle" nowrap>创建人职业</td>
                    <td class="tdcontent"><ext:field property="creatorJob"/></td>
                </tr>
                <tr>
                    <td class="tdtitle" nowrap>创建人所在单位</td>
                    <td class="tdcontent"><ext:field property="creatorUnit"/></td>
                    <td class="tdtitle" nowrap>创建人传真</td>
                    <td class="tdcontent"><ext:field property="creatorFax"/></td>
                    <td class="tdtitle" nowrap>创建人手机</td>
                    <td class="tdcontent"><ext:field property="creatorMobile"/></td>
                </tr>
                <tr>
                    <td class="tdtitle" nowrap>创建人IP</td>
                    <td class="tdcontent"><ext:field property="creatorIP"/></td>
                    <td class="tdtitle" nowrap>邮箱</td>
                    <td class="tdcontent"><ext:field property="creatorMail"/></td>
                    <td class="tdtitle" nowrap>创建人证件号码</td>
                    <td class="tdcontent"><ext:field property="creatorIdentityCard"/></td>
                </tr>
                <tr>
                    <td class="tdtitle" nowrap>创建人证件名称</td>
                    <td class="tdcontent"><ext:field property="creatorCertificateName"/></td>
                    <td class="tdtitle" nowrap>联系电话</td>
                    <td class="tdcontent"><ext:field property="creatorTel"/></td>
                    <td class="tdtitle" nowrap>创建人性别</td>
                    <td class="tdcontent"><ext:field property="creatorSex"/></td>
                </tr>
                <tr>
                    <td class="tdtitle" nowrap>创建人姓名</td>
                    <td class="tdcontent"><ext:field property="creator"/></td>
                    <td class="tdtitle" nowrap>创建时间</td>
                    <td class="tdcontent"><ext:field property="created"/></td>
                    <td class="tdtitle" nowrap>指定工作日</td>
                    <td class="tdcontent"><ext:field property="workingDay"/></td>
                </tr>
                <tr>
                    <td class="tdtitle" nowrap>内容</td>
                    <td class="tdcontent" colspan="5"><ext:field property="content"/></td>
                </tr>
                <tr>
                    <td class="tdtitle" nowrap>主题</td>
                    <td class="tdcontent"><ext:field property="subject"/></td>
                    <td class="tdtitle" nowrap>查询密码</td>
                    <td class="tdcontent" colspan="3"><ext:field property="queryPassword"/></td>
                </tr>
            </tbody>
        </table>
    
