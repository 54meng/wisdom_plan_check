<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String pTableId = request.getParameter("tableId");
String tableId = pTableId!=null?pTableId:UUID.randomUUID().toString().replace("-", "");
String cType = request.getParameter("cType");
String userRole = (String)request.getSession().getAttribute("userRole");
%>
<!-- BEGIN PAGE HEADER-->
<h3 class="page-title">
<%
if("1".equals(cType)){
	%>所有用户<%
}else if("2".equals(cType)){
	%>处理中用户<%
}else if("3".equals(cType)){
	%>已完结用户<%
}else if("5".equals(cType)){
	%>回收站<%
}
%>
<small></small>
</h3>
<!-- BEGIN PAGE CONTENT-->
<div class="row">
	<div class="col-md-12">
		<!-- Begin: life time stats -->
		<div class="portlet box blue">
			<div class="portlet-title">
				<div class="caption">
					<i class="fa fa-cityping-cart"></i><%=("5".equals(cType) ? "回收站" : "用户信息") %>
				</div>
				<div class="actions">
					<%
						if("1".equals(cType) || "2".equals(cType) && ("1".equals(userRole) || "2".equals(userRole))){
							%>
								<a id="assignedTask" href="../../../collection.sp?act=assignedTask" data-target="#ajax" data-toggle="modal" />
								<a onclick="assignedTask();" class="btn default yellow-stripe">
								<i class="fa fa-plus"></i>
								<span class="hidden-480">
								任务分配</span>
								</a>
							<%
						}
					%>
					<%
						if(!"5".equals(cType)){
							%>
								<a id="updateStatusBatch" href="../../../collection.sp?act=updateStatusBatch" data-target="#ajax" data-toggle="modal" />
								<a onclick="updateStatusBatch();" class="btn default yellow-stripe">
								<i class="fa fa-edit"></i>
								<span class="hidden-480">
								批量修改状态</span>
								</a>
							<%
						}
					%>										
					<a href="collection_manager.jsp?tableId=<%=tableId %>&cType=<%=cType %>" id="refreshBtn" class="btn default yellow-stripe ajaxify">
					<i class="fa fa-refresh"></i>
					<span class="hidden-480">
					刷新 </span>
					</a>
				</div>
			</div>
			<div class="portlet-body">
				<div class="table-container">
					<% 
						if("1".equals(userRole) || ("2".equals(userRole) && !"5".equals(cType))){
							%>
							<div class="table-actions-wrapper">								
								<select class="table-group-action-input form-control input-inline  input-sm">
									<option value="delete">删除</option>
									<%
										if("5".equals(cType)){
											%>
											<option value="recover">恢复</option>
											<%
										}
									%>
								</select>
								<button class="btn btn-sm yellow table-group-action-submit"><i class="fa fa-check"></i>执行</button>
							</div>
							<%
						}
					%>
					
					<input type="hidden" id="tableId" value="dt_<%=tableId%>"/>
					<table class="table table-striped table-bordered table-hover" id="dt_<%=tableId%>">
					<thead>
					<tr role="row" class="heading" >
						<th width="1%">
							<input type="checkbox" class="group-checkable">
						</th>

						<th width="8%" class="text-center">
							 委托批次
						</th>
						<th width="9%" class="text-center">
							 姓名
						</th>
						<th width="8%" class="text-center">
							 卡号
						</th>					
						<th width="9%" class="text-center">
							 证件号
						</th>
						<th width="6%" class="text-center">
							 联系方式
						</th>
						<th width="6%" class="text-center">
							 余额
						</th>
						<th width="6%" class="text-center">
							 本金
						</th>
						<th width="9%" class="text-center">
							 所属银行
						</th>
						<th width="8%" class="text-center">
							 操作人
						</th>
						<th width="30%" class="text-center">
							 操作
						</th>
					</tr>
					<tr role="row" class="filter">
						<td>
						</td>

						<td>
							<input type="text" class="form-control form-filter input-sm" name="filter_entrustBatch_LIKE">
						</td>
						<td>
							<input type="text" class="form-control form-filter input-sm" name="filter_cardholderName_LIKE">
						</td>	
						<td>
							<input type="text" class="form-control form-filter input-sm" name="filter_cardNum_LIKE">
						</td>
						<td>
							<input type="text" class="form-control form-filter input-sm" name="filter_certificateNum_LIKE">
						</td>
						<td>
							<input type="text" class="form-control form-filter input-sm" name="filter_cardholderMobilePhone_LIKE">
						</td>
						
						<td>
							<input type="text" class="form-control form-filter input-sm" name="filter_balance_LIKE">
						</td>
						<td>
							<input type="text" class="form-control form-filter input-sm" name="filter_balanceRmb_LIKE">
						</td>
						<td>
							<select name="filter_belongBank_EQ" class="form-control form-filter input-sm">
								<option value="">不限</option>
								<option value="1">中信</option>
								<option value="2">浦发</option>								
							</select>
						</td>
						<td>
							<!--<select class="form-control form-filter input-sm" name="filter_operUserId_EQ">
								<option value="">不限</option>
								<c:forEach items="${userList}" var="item">
									<option value="${item.userId}">${item.userName}</option>
								</c:forEach>
							</select>
							-->
							<input type="text" class="form-control form-filter input-sm" name="filter_userName_LIKE">
						</td>
						<td>
							<button class="btn btn-xs yellow filter-submit margin-bottom" id="searchBtn"><i class="fa fa-search"></i>查询</button>
							<button class="btn btn-xs red filter-cancel"><i class="fa fa-times"></i>重置</button>
						</td>
						
					</tr>
					</thead>
					<tbody>
					</tbody>
					</table>
				</div>
			</div>
		</div>
		<!-- End: life time stats -->
	</div>
</div>
<!-- END PAGE CONTENT-->
<script type="text/javascript">
var grid = new Datatable();
function assignedTask(){
	if (grid.getSelectedRowsCount() === 0) {
       Metronic.alert({
           type: 'danger',
           icon: 'warning',
           message: '没有选择记录',
           container: grid.getTableWrapper(),
           place: 'prepend',
           closeInSeconds:2
       });
    }else{
    	var a = document.getElementById("assignedTask");    
    	var cIds = grid.getSelectedRows();
    	a.href = "../../../collection.sp?act=assignedTask&cIds="+cIds;       
        //取消<a>标签原先的onclick事件,使<a>标签点击后通过href跳转(因为无法用js跳转)^-^  
        a.setAttribute("onclick",'');  
        //激发标签点击事件OVER  
        a.click("return false"); 
    }
}
function updateStatusBatch(){
	if (grid.getSelectedRowsCount() === 0) {
       Metronic.alert({
           type: 'danger',
           icon: 'warning',
           message: '没有选择记录',
           container: grid.getTableWrapper(),
           place: 'prepend',
           closeInSeconds:2
       });
    }else{
    	var a = document.getElementById("updateStatusBatch");    
    	var cIds = grid.getSelectedRows();
    	a.href = "../../../collection.sp?act=updateStatusBatch&cIds="+cIds;       
        //取消<a>标签原先的onclick事件,使<a>标签点击后通过href跳转(因为无法用js跳转)^-^  
        a.setAttribute("onclick",'');  
        //激发标签点击事件OVER  
        a.click("return false"); 
    }
}
var TableAjax = function () {
    var initPickers = function () {
        //init date pickers
        $('.date-picker').datepicker({
            rtl: Metronic.isRTL(),
            autoclose: true
        });
    }

    var handleRecords = function () {

        grid.init({
            src: $("#dt_<%=tableId%>"),
            onSuccess: function (grid) {
                // execute some code after table records loaded
            },
            onError: function (grid) {
                // execute some code on network or other general error  
            },
            onDataLoad: function(grid) {
                // execute some code on ajax data load
            },
            loadingMessage: '正在加载...',
            dataTable: { // here you can define a typical datatable settings from http://datatables.net/usage/options 

                // Uncomment below line("dom" parameter) to fix the dropdown overflow issue in the datatable cells. The default datatable layout
                // setup uses scrollable div(table-scrollable) with overflow:auto to enable vertical scroll(see: assets/global/scripts/datatable.js). 
                // So when dropdowns used the scrollable div should be removed. 
                //"dom": "<'row'<'col-md-8 col-sm-12'pli><'col-md-4 col-sm-12'<'table-group-actions pull-right'>>r>t<'row'<'col-md-8 col-sm-12'pli><'col-md-4 col-sm-12'>>",
                "bSort":true,
                "bStateSave": true, // save datatable state(pagination, sort, etc) in cookie.

                "lengthMenu": [
                    [10, 20, 50, 100],
                    [10, 20, 50, 100] // change per page values here
                ],
                "aoColumnDefs": [ { "bSortable": false, "aTargets": [0,1,2,3,4,5,8,9,10] }],
                "bPaginate":true,
                "pageLength": 10, // default record count per page
                //"sAjaxSource": "/travel/index.sp?act=index",
                "ajax": {
                    "url": "<%=basePath%>collection.sp?act=loadData&cType=<%=cType%>", // ajax source
                },
                "order": [
                    
                ]
            }
        });

        // handle group actionsubmit button click
        grid.getTableWrapper().on('click', '.table-group-action-submit', function (e) {
            e.preventDefault();
            var selectRows = grid.getSelectedRows();
            var pageInfo = grid.getDataTable().page.info();
            var selectCount = grid.getSelectedRowsCount();
            var action = $(".table-group-action-input", grid.getTableWrapper());
            if (action.val() != "" && selectCount > 0) {           	
	            if(action.val() == "delete"){
		            if (!confirm(<%=cType%>=='5' ? '确定要彻底删除所选数据？' : '确定要把所选数据放进回收站？')) {
		                return;
		            }
	            }else {
	            	if (!confirm('确定要恢复所选数据？')) {
		                return;
		            }
	            }
                grid.setAjaxParam("groupAction", true);
                grid.setAjaxParam("groupActionType", action.val());
                grid.setAjaxParam("selectItem", grid.getSelectedRows());
                grid.getDataTable().draw(false);
                grid.clearAjaxParams();
            } else if (action.val() == "") {
                Metronic.alert({
                    type: 'danger',
                    icon: 'warning',
                    message: '请选择一个操作',
                    container: grid.getTableWrapper(),
                    place: 'prepend',
                    closeInSeconds:1
                });
            } else if (grid.getSelectedRowsCount() === 0) {
                Metronic.alert({
                    type: 'danger',
                    icon: 'warning',
                    message: '没有选择记录',
                    container: grid.getTableWrapper(),
                    place: 'prepend',
                    closeInSeconds:1
                });
            }
        });
    }

    return {

        //main function to initiate the module
        init: function () {

            initPickers();
            handleRecords();
        }

    };

}();
TableAjax.init();
var ajaxModal = $("#ajax");
ajaxModal.on('hidden.bs.modal', function () {
	ajaxModal.html(""+
	"<div class=\"modal-dialog modal-lg\">"+
		"<div class=\"modal-content\">"+
			"<div class=\"modal-body\">"+
            	"<img src=\"../../assets/global/img/loading-spinner-grey.gif\" alt=\"\" class=\"loading\">"+
				"<span>&nbsp;&nbsp;正在加载...</span>"+
			"</div>"+
		"</div>"+
	"</div>");
});
</script>