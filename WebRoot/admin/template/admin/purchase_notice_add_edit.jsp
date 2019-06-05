<%@page import="com.wpc.persistence.PurchaseNotice"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>  
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
boolean isNew = (Boolean)request.getAttribute("isNew");
PurchaseNotice entity = (PurchaseNotice)request.getAttribute("purchaseNotice");
%>

<style>
	.modal-radio div{
	 padding:0px 10px ;
	}
	.modal-radio div span{
		padding-left: 3px;
	}
	.modal-btn{
	    display: inline-block;
	    padding: 6px 12px;
	    margin-bottom: 0;
	    font-size: 14px;
	    font-weight: 400;
	    line-height: 1.42857143;
	    text-align: center;
	    white-space: nowrap;
	    vertical-align: middle;
	    -ms-touch-action: manipulation;
	    touch-action: manipulation;
	    /* 定义选中鼠标光标 */
	    cursor: pointer;
	    -webkit-user-select: none;
	    -moz-user-select: none;
	    -ms-user-select: none;
	    user-select: none;
	    outline: none;
	    border: 1px solid transparent;
	    border-radius: 4px;
	    border-color:#ff0000 ;
	}
	/* 定义颜色 */
	.modal-btn-outline{
		background-color: transparent;
		border-color: #26a69a;
		color: #26a69a;
		transition: all 0.5s;
	}
	.modal-btn-outline:active,.btn-outline:hover,.btn-outline:focus{
		background-color: #26a69a;
		border-color: #26a69a;
		color: #FFFFFF;
	}
	
</style>
<!-- modal-content -->
<div class="modal-header">
	<button type="button" class="close" data-dismiss="modal" aria-hidden="true"></button>
	<h4 class="modal-title"><%=(isNew?"新建采购通知":"编辑采购通知") %></h4>
</div>
<div class="modal-content form ">
	<!-- BEGIN FORM-->
	<form action="purchaseNotice.sp" enctype="multipart/form-data" method="POST" id="form_sample_1" class="form-horizontal" novalidate="novalidate">
		<input type="hidden" name="act" value="save" />
		<input type="hidden" name="id" value="${purchaseNotice.id }" />
		<div class="form-body">
			<div class="alert alert-danger display-hide">
				<button class="close" data-close="alert"></button>
				你的表单存在输入错误，请检查以下表单.
			</div>
			<div class="alert alert-success display-hide">
				<button class="close" data-close="alert"></button>
				你的表单验证成功!
			</div>
			<!--<div class="form-group">
				<label class="col-md-3 control-label">采购内容<span class="required">
				* </span></label>
				<div class="col-md-7">
					<textarea name="content" class="form-control" rows="5" data-required="1">${purchaseNotice.content}</textarea>
				</div>
			</div>
			<div class="form-group">
				<label class="control-label col-md-3">截止时间<span class="required">
				* </span>
				</label>
				<div class="col-md-7">
					<input type="date" id="deadline" name="deadline" value="${purchaseNotice.deadlineStr}" data-required="1" class="form-control"/>
				</div>
			</div>-->
			
				<div class="form-group">
					<div class="col-md-12 modal-radio" style="display: flex;">
						<div class="" >
							<input type="radio" name="tabSwitch" id="" value="1" checked><span>表一 </span> 
						</div>
						<div class="">
							<input type="radio" name="tabSwitch" id="" value="2" ><span>表二</span> 
						</div>     
					</div>
				</div>
			<div class="table1" style="">
				<div class="table-responsive">
					<table class="table table-bordered table-striped" id="table1">
					<thead>
					  <tr>
						<td>产品编码</td>
						<td>需求数量</td>
						<td>计量单位</td>
						<td>评估价格</td>
						<td>需求日期</td>
						<td>采购组</td>
						<td>采购组织</td>
						<td>波次编号</td>
						<td>非标准产品说明原因</td>
						<td>交货方式</td>
						<td>ID号</td>
					  </tr>
					</thead>
					<tbody>
					  <tr>
						<td>123456</td>
						<td>25</td>
						<td>个</td>
						<td>1000</td>
						<td>2019-08-08 18:18:18</td>
						<td>一组</td>
						<td>采购部</td>
						<td>188888</td>
						<td>无</td>
						<td>线下</td>
						<td>888888</td>
					  </tr>
					  <tr>
						<td>654321</td>
						<td>25</td>
						<td>个</td>
						<td>1000</td>
						<td>2019-08-08 18:18:18</td>
						<td>一组</td>
						<td>采购部</td>
						<td>188888</td>
						<td>无</td>
						<td>线下</td>
						<td>888888</td>
					  </tr>
					 
					</tbody>
				</table>
				</div>
			
				<div class="form-group">
					<div class="col-md-12" style="display: flex;justify-content: center;" >
						<div class="modal-btn modal-btn-outline" id="addTable1"><i class="fa fa-plus"></i> 添加行</div>
					</div>
				</div>
			
			</div>
			
			
			<div class="table2" style="display: none;">
				
				<div class="form-group">
					<label class="control-label col-md-2" style="text-align: left;">采购单申请类型<span class="required">
					* </span>
					</label>
					<div class="col-md-7">
						<input type="text" id="" name="" value="" class="form-control"/>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-md-2" style="text-align: left;">单位/部门<span class="required">
					* </span>
					</label>
					<div class="col-md-7">
						<input type="text" id="" name="" value="" class="form-control"/>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-md-2" style="text-align: left;">采购组<span class="required">
					* </span>
					</label>
					<div class="col-md-7">
						<input type="text" id="" name="" value="" class="form-control"/>
					</div>
				</div>
				<div class="form-group">
					<label class="control-label col-md-2" style="text-align: left;">波次编号<span class="required">
					* </span>
					</label>
					<div class="col-md-7">
						<input type="text" id="" name="" value="" class="form-control"/>
					</div>
				</div>
				
				<div class="table-responsive">
				<table class="table table-bordered table-striped" id="table2">
					<thead>
					  <tr>
						<td>序号</td>
						<td>产品编码</td>
						<td>产品信息</td>
						<td>需求数量</td>
						<td>单位</td>
						<td>单价</td>
						<td>交货日期</td>
						<td>交货地点及交货方式</td>
						<td>ID</td>
						<td>非标准产品说明原因</td>
						<td>申请人</td>
						<td>特定因素A</td>
						<td>资产编号</td>
						<td>级别</td>
						<td>特定因素B</td>
					  </tr>
					</thead>
					<tbody>
					  <tr>
						<td>088888</td>
						<td>188888</td>
						<td>隔离开关</td>
						<td>100</td>
						<td>个</td>
						<td>100</td>
						<td>2019-08-08</td>
						<td>线下</td>
						<td>888</td>
						<td>无</td>
						<td>张三</td>
						<td>无</td>
						<td>2888888</td>
						<td>1</td>
						<td>无</td>
					  </tr>
					  <tr>
						<td>088888</td>
						<td>188888</td>
						<td>隔离开关</td>
						<td>100</td>
						<td>个</td>
						<td>100</td>
						<td>2019-08-08</td>
						<td>线下</td>
						<td>888</td>
						<td>无</td>
						<td>张三</td>
						<td>无</td>
						<td>2888888</td>
						<td>1</td>
						<td>无</td>
					  </tr>
					 
					</tbody>
				</table>
				</div>
			
				<div class="form-group">
					<div class="col-md-12" style="display: flex;justify-content: center;" >
						<div class="modal-btn modal-btn-outline" id="addTable2"><i class="fa fa-plus"></i> 添加行</div>
					</div>
				</div>
			
			</div>
				
			
			
			
			
		</div>
		<div class="form-actions">
			<div class="row">
				<div class="col-md-offset-3 col-md-9">
					<button id="submitBtn" type="submit" class="btn green">保存</button>
					<button type="button" class="btn default" data-dismiss="modal">关闭</button>
				</div>
			</div>
		</div>
	</form>
	<!-- END FORM-->
</div>
<!-- /.modal-content -->

<script>
	$('.modal-lg').css('width','1152');
	// 表格
	$('#table1').SetEditable({
		$addTabButton: $('#addTable1')
	});
	$('#table2').SetEditable({
		$addTabButton: $('#addTable2')
	});
	
	$('input:radio[name="tabSwitch"]').change(function(){
		var radioVal = $(this).val();
        if (radioVal =="2"){
        	console.log("2")
        	$(".table1").hide(); 
	        $(".table2").show();
        }else{
        	console.log("1")
	        $(".table2").hide();
	        $(".table1").show();
        }

		
		
	});

	
//Html编码获取Html转义实体
function htmlEncode(value){
  return $('<div/>').text(value).html();
}
//Html解码获取Html实体
function htmlDecode(value){
  return $('<div/>').html(value).text();
}
//提示窗口
function boxAlert(title,message){
	 bootbox.dialog({
         'message': message,
         'title': title,
         'buttons': {
           main: {
             label: "关闭",
             className: "blue",
           }
         }
     });
}
//表单验证方法
var handleValidation = function() {
    	// for more info visit the official plugin documentation: 
        // http://docs.jquery.com/Plugins/Validation
        var form1 = $('#form_sample_1');
        var error1 = $('.alert-danger', form1);
        var success1 = $('.alert-success', form1);

        form1.validate({
            errorElement: 'span', //default input error message container
            errorClass: 'help-block help-block-error', // default input error message class
            focusInvalid: false, // do not focus the last invalid input
            ignore: "",  // validate all fields including form hidden input
            messages: {
            	content: {                  
                    required: "采购内容不能为空"
                },
                deadline: {                  
                    required: "请选择截止时间"
                }
            },
            rules: {
            	content: {
                    required: true             
                },
                deadline: {
                    required: true             
                }
            },
            invalidHandler: function (event, validator) { //display error alert on form submit              
                success1.hide();
                error1.show();
                Metronic.scrollTo(error1, -200);
            },
            highlight: function (element) { // hightlight error inputs
                $(element)
                    .closest('.form-group').addClass('has-error'); // set error class to the control group
            },
            unhighlight: function (element) { // revert the change done by hightlight
                $(element)
                    .closest('.form-group').removeClass('has-error'); // set error class to the control group
            },
            success: function (label) {
                label
                    .closest('.form-group').removeClass('has-error'); // set success class to the control group
            },
            submitHandler: function (form) {
                //success1.show();
                error1.hide();
                try{
                	var submitBtn = $("#submitBtn");
                    var action = form1.attr("action").split('?');
                    var extParam="";
                    if(action.length>1){extParam = "&"+ action[1]}
               	  	var option = {
               		   	type: "POST",
               		   	url: "../../../"+action[0],
               		   	//data: form1.serialize()+extParam,
               		 	//async: true,
               		 	dataType:"json",
               		 	beforeSend: function(XMLHttpRequest){
               		 	 	//this; // 调用本次AJAX请求时传递的options参数
               		 		submitBtn.text("正在保存...");
               		 	},
               		   	success: function(data){
               		   		console.log(data&&data.ret);
               		   		if(data.ret==0){
        	               		submitBtn.text("保存成功!");
        	           		   	setTimeout(function () {
        	           		   		$('#ajax').modal('hide');
        	           		   		var tableId = $("#tableId").val();
        	           		   		var table = $("#"+tableId).DataTable();
        	           		   		if(<%=isNew%>){
            	           		   		table.draw();
        	           		   		}else{
            	           		   		table.draw(false);
        	           		   		}
        	                   	}, 300);
               		   		}else if(data.ret==1){
               		   			submitBtn.text("保存");
	               		   		boxAlert("保存失败，错误信息",data.msg);
               		   		}else{
	               		   		submitBtn.text("保存");
	               		   		boxAlert("保存失败，返回未知数据",htmlEncode(JSON.stringify(data)));
               		   		}
              			},
            			complete: function(XMLHttpRequest, textStatus){
            		   		if(textStatus!="success"&&textStatus!="error"){
                		 		submitBtn.text("保存");
                		 		boxAlert("保存失败，错误信息",htmlEncode(XMLHttpRequest.status+":"+(XMLHttpRequest.text)));
            		   		}
            			},
            			error:function(XMLHttpRequest, textStatus, errorThrown){
            		   		submitBtn.text("保存");
            		 		boxAlert("保存失败，ajax请求存在异常",XMLHttpRequest.status+":"+errorThrown);
            		 		try{console.log('异常对象：',errorThrown);}catch(e){}
            			}
               		};
               		$(form).ajaxSubmit(option);
            	}catch(e){
                	try{console.log(e);}catch(e){}
            	}
        	}
    });
}
handleValidation();
</script>