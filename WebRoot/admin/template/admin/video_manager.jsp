<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String pTableId = request.getParameter("tableId");
String tableId = pTableId!=null?pTableId:UUID.randomUUID().toString().replace("-", "");
%>
<!-- BEGIN PAGE HEADER-->
<h3 class="page-title">
视频管理 <small>对视频进行增删改查</small>
</h3>
<!-- BEGIN PAGE CONTENT-->
<div class="row">
	<div class="col-md-12">
		<!-- Begin: life time stats -->
		<div class="portlet box blue">
			<div class="portlet-title">
				<div class="caption">
					<i class="fa fa-shopping-cart"></i>视频管理
				</div>
				<div class="actions">
					
					
					<a href="video_manager.jsp?tableId=<%=tableId %>" id="refreshBtn" class="btn default yellow-stripe ajaxify">
					<i class="fa fa-refresh"></i>
					<span class="hidden-480">
					刷新 </span>
					</a>
				</div>
			</div>
			<div class="portlet-body">
				<div class="table-container">
					<div class="table-actions-wrapper">
						<span>
						</span>
						<select class="table-group-action-input form-control input-inline  input-sm">
							<option value="delete">删除</option>
						</select>
						<button class="btn btn-sm yellow table-group-action-submit"><i class="fa fa-check"></i>执行</button>
					</div>
					
					<input type="hidden" id="tableId" value="dt_<%=tableId%>"/>
					<table class="table table-striped table-bordered table-hover" id="dt_<%=tableId%>">
					<thead>
					<tr role="row" class="heading">
						<th width="2%">
							<input type="checkbox" class="group-checkable">
						</th>
						<th width="10%">
							 ID
						</th>
						<th width="10%">
							 名称
						</th>					
						<th width="10%">
							 分类
						</th>
						<th width="5%">
							 评分
						</th>
						<th width="5%">
							 播放数
						</th>
						<th width="5%">
							 点赞数
						</th>
						<th width="10%">
							 专题
						</th>
						<th width="10%">
							 主角
						</th>
						<th width="10%">
							 大小
						</th>
						<th width="13%">
							 创建时间
						</th>
						<th width="15%">
							 操作
						</th>
					</tr>
					<tr role="row" class="filter">
						<td></td>
						<td></td>			
						<td>
							<input type="text" class="form-control form-filter input-sm" name="filter_name_LIKE">
						</td>	
						<td></td>					
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>					
						<td>
							<button class="btn btn-xs yellow filter-submit margin-bottom" id="searchBtn"><i class="fa fa-search"></i>查询</button>
							
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
var TableAjax = function () {
    var initPickers = function () {
        //init date pickers
        $('.date-picker').datepicker({
            rtl: Metronic.isRTL(),
            autoclose: true
        });
    }

    var handleRecords = function () {

        var grid = new Datatable();

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
                "bSort":false,
                "bStateSave": true, // save datatable state(pagination, sort, etc) in cookie.

                "lengthMenu": [
                    [10, 20, 50, 100],
                    [10, 20, 50, 100] // change per page values here
                ],
                "bPaginate":true,
                "pageLength": 10, // default record count per page
                //"sAjaxSource": "/travel/index.sp?act=index",
                "ajax": {
                    "url": "../../../video.sp?act=loadData", // ajax source
                },
                /*"order": [
                    [1, "asc"]
                ]*/// set first column as a default sort by asc
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
            	if (!confirm('确定删除所选数据？')) {
	                return;
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