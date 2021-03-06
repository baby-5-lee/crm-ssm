<%@page contentType="text/html;charset=UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<link href="../../jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="../../jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="../../jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="../../jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="../../jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="../../jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>
<link rel="stylesheet" type="text/css" href="../../jquery/bs_pagination/jquery.bs_pagination.min.css">
<script type="text/javascript" src="../../jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="../../jquery/bs_pagination/en.js"></script>

<script type="text/javascript">

	$(function(){
		pageList(1,3);
		$("#search-btn").click(function () {
			$("#hidden-name").val($("#search-name").val());
			$("#hidden-owner").val($("#search-owner").val());
			$("#hidden-company").val($("#search-company").val());
			$("#hidden-phone").val($("#search-phone").val());
			$("#hidden-mphone").val($("#search-mphone").val());
			$("#hidden-source").val($("#search-source").val());
			$("#hidden-state").val($("#search-state").val());
			pageList(1,$("#cluePage").bs_pagination('getOption', 'rowsPerPage'));
			$("#search-btn").blur();
		});
		$("#qx").click(function () {
			$(".dx").prop("checked",this.checked);
		});
		$("#search-page").on("click",".dx",function (){
			$("#qx").prop("checked",$(".dx").length===$(".dx:checked").length);
		});
		$("#create-btn").click(function (){
			$(".time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "top-left"
			});
			$.ajax({
				url:"getUserList.do",
				type:"get",
				dataType:"json",
				success:function (data) {
					let userHtml = "";
					$.each(data,function (i,n) {
						userHtml+="<option value='"+n.id+"'>"+n.name+"</option>";
					})
					$("#create-clueOwner").html(userHtml);
					$("#create-clueOwner").val("${user.id}");
					$("#createClueModal").modal("show");
				}
			})
		});
		$("#close-btn").click(function () {
			$("#createClueModal").modal("hide");
			//$(".form-control").val("");
			$("#save-form")[0].reset();
		});
		$("#save-btn").click(function () {
			$.ajax({
				url:"saveClue.do",
				data:{
					"owner":$("#create-clueOwner").val(),
					"company":$("#create-company").val().trim(),
					"appellation":$("#create-call").val(),
					"fullname":$("#create-surname").val().trim(),
					"job":$("#create-job").val().trim(),
					"email":$("#create-email").val().trim(),
					"phone":$("#create-phone").val().trim(),
					"website":$("#create-website").val().trim(),
					"mphone":$("#create-mphone").val().trim(),
					"state":$("#create-status").val(),
					"source":$("#create-source").val(),
					"description":$("#create-describe").val().trim(),
					"contactSummary":$("#create-contactSummary").val().trim(),
					"nextContactTime":$("#create-nextContactTime").val(),
					"address":$("#create-address").val().trim()
				},
				type:"post",
				dataType:"json",
				success:function (data) {
					if (data.success){
						pageList(1,$("#cluePage").bs_pagination('getOption', 'rowsPerPage'));
						$("#createClueModal").modal("hide");
						//$(".form-control").val("");
						$("#save-form")[0].reset();
					}else {
						alert("??????????????????");
					}
				}
			})
		});
		$("#edit-btn").click(function () {
			$("#edit-btn").blur();
			let dx=$(".dx:checked");
			if (dx.length===0){
				alert("???????????????????????????");
				return;
			}else if (dx.length>1){
				alert("??????????????????????????????");
				return;
			}
			$(".edit-time").datetimepicker({
				minView: "month",
				language:  'zh-CN',
				format: 'yyyy-mm-dd',
				autoclose: true,
				todayBtn: true,
				pickerPosition: "top-left"
			});
			$.ajax({
				url:"getClue.do",
				data:{"id":dx.val()},
				type:"get",
				dataType:"json",
				success:function (data) {
					//{"userList":[{},{},{}],"activity":{}}
					let html = "";
					$.each(data.userList,function (i,n) {
						html+="<option value='"+n.id+"'>"+n.name+"</option>";
					})
					$("#edit-clueOwner").html(html);
					$("#edit-clueOwner").val(data.clue.owner);
					$("#edit-company").val(data.clue.company);
					$("#edit-call").val(data.clue.appellation);
					$("#edit-surname").val(data.clue.fullname)
					$("#edit-job").val(data.clue.job);
					$("#edit-email").val(data.clue.email);
					$("#edit-phone").val(data.clue.phone);
					$("#edit-mphone").val(data.clue.mphone);
					$("#edit-website").val(data.clue.website);
					$("#edit-status").val(data.clue.state);
					$("#edit-source").val(data.clue.source);
					$("#edit-describe").val(data.clue.description);
					$("#edit-contactSummary").val(data.clue.contactSummary);
					$("#edit-nextContactTime").val(data.clue.nextContactTime);
					$("#edit-address").val(data.clue.address);
					$("#editClueModal").modal("show");
				}
			})
		});
		$("#update-btn").click(function () {
            $.ajax({
                url:"updateClue.do",
                data:{
                    "id":$(".dx:checked").val(),
                    "fullname":$("#edit-surname").val().trim(),
                    "appellation":$("#edit-call").val(),
                    "owner":$("#edit-clueOwner").val(),
                    "company":$("#edit-company").val().trim(),
                    "job":$("#edit-job").val().trim(),
                    "email":$("#edit-email").val().trim(),
                    "phone":$("#edit-phone").val().trim(),
                    "website":$("#edit-website").val().trim(),
                    "mphone":$("#edit-mphone").val().trim(),
                    "state":$("#edit-status").val(),
                    "source":$("#edit-source").val(),
                    "description":$("#edit-describe").val().trim(),
                    "contactSummary":$("#edit-contactSummary").val().trim(),
                    "nextContactTime":$("#edit-nextContactTime").val(),
                    "address":$("#edit-address").val().trim(),
                },
                type:"post",
                dataType:"json",
                success:function (data) {
                    if (data.success){
                        pageList($("#cluePage").bs_pagination('getOption', 'currentPage'),$("#cluePage").bs_pagination('getOption', 'rowsPerPage'));
                        $("#editClueModal").modal("hide");
                    }else {
                        alert("??????????????????");
                    }
                }
            })
        });
		$("#remove-btn").click(function () {
			$("#remove-btn").blur();
			let dx = $(".dx:checked");
			if (dx.length!==0){
				if (!confirm("???????????????????????????????????????????????????????????????")){
					return;
				}
				let ids = "";
				$(dx).each(function (i) {
					ids+="id="+$(dx[i]).val();
					if (i!==(dx.length-1)){
						ids+="&";
					}
				})
				$.ajax({
					url:"remove.do",
					data:ids,
					type:"post",
					dataType:"json",
					success:function (data) {
						if (data.success){
							pageList(1,$("#cluePage").bs_pagination('getOption', 'rowsPerPage'));
						}else {
							alert("????????????");
						}
					}
				})
			}else {
				alert("??????????????????????????????");
			}
		});
	});
	function pageList(pageNo,pageSize) {
		$("#search-name").val($("#hidden-name").val());
		$("#search-owner").val($("#hidden-owner").val());
		$("#search-company").val($("#hidden-company").val());
		$("#search-phone").val($("#hidden-phone").val());
		$("#search-mphone").val($("#hidden-mphone").val());
		$("#search-source").val($("#hidden-source").val());
		$("#search-state").val($("#hidden-state").val());
		$.ajax({
			url: "pageList.do",
			data: {
				"pageNo": pageNo,
				"pageSize": pageSize,
				"name": $("#search-name").val(),
				"owner": $("#search-owner").val(),
				"company": $("#search-company").val(),
				"phone": $("#search-phone").val(),
				"mphone": $("#search-mphone").val(),
				"source":$("#search-source").val(),
				"state":$("#search-state").val()
			},
			dataType: "json",
			type: "get",
			success: function (data) {
				$("#qx").prop("checked", false);
				let html = "";
				let dataList = data.dataList;
				$.each(dataList, function (i, n) {
					html+='<tr>';
					html+='	<td><input class="dx" type="checkbox" value="'+n.id+'" /></td>';
					html+='	<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='+"'detail.jsp?id="+n.id+"'"+';">'+n.fullname+n.appellation+'</a></td>';
					html+='	<td>'+n.company+'</td>';
					html+='	<td>'+n.phone+'</td>';
					html+='	<td>'+n.mphone+'</td>';
					html+='	<td>'+n.source+'</td>';
					html+='	<td>'+n.owner+'</td>';
					html+='	<td>'+n.state+'</td>';
					html+='</tr>';
				})
				$("#search-page").html(html);
				let totalPage = parseInt((data.total - 1) / pageSize) + 1;

				$("#cluePage").bs_pagination({
					currentPage: pageNo, // ??????
					rowsPerPage: pageSize, // ???????????????????????????
					maxRowsPerPage: 20, // ?????????????????????????????????
					totalPages: totalPage, // ?????????
					totalRows: data.total, // ???????????????

					visiblePageLinks: 3, // ??????????????????

					showGoToPage: true,
					showRowsPerPage: true,
					showRowsInfo: true,
					showRowsDefaultInfo: true,

					onChangePage: function (event, data) {
						pageList(data.currentPage, data.rowsPerPage);
					}
				});

			}
		})
	}
</script>
</head>
<body>
	<input type="hidden" id="hidden-name"/>
	<input type="hidden" id="hidden-owner"/>
	<input type="hidden" id="hidden-company"/>
	<input type="hidden" id="hidden-phone"/>
	<input type="hidden" id="hidden-mphone"/>
	<input type="hidden" id="hidden-source"/>
	<input type="hidden" id="hidden-state"/>
	<!-- ??????????????????????????? -->
	<div class="modal fade" id="createClueModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">??</span>
					</button>
					<h4 class="modal-title" id="myModalLabel">????????????</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form" id="save-form">
					
						<div class="form-group">
							<label for="create-clueOwner" class="col-sm-2 control-label">?????????<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-clueOwner">

								</select>
							</div>
							<label for="create-company" class="col-sm-2 control-label">??????<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-company">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-call" class="col-sm-2 control-label">??????</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-call">
									<option></option>
									<c:forEach items="${appellation}" var="aplt">
										<option value="${aplt.value}">${aplt.text}</option>
									</c:forEach>
								</select>
							</div>
							<label for="create-surname" class="col-sm-2 control-label">??????<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-surname">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-job" class="col-sm-2 control-label">??????</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-job">
							</div>
							<label for="create-email" class="col-sm-2 control-label">??????</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-email">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-phone" class="col-sm-2 control-label">????????????</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-phone">
							</div>
							<label for="create-website" class="col-sm-2 control-label">????????????</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-website">
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-mphone" class="col-sm-2 control-label">??????</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="create-mphone">
							</div>
							<label for="create-status" class="col-sm-2 control-label">????????????</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-status">
								  <option></option>
									<c:forEach items="${clueState}" var="cs">
										<option value="${cs.value}">${cs.text}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="create-source" class="col-sm-2 control-label">????????????</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-source">
								  <option></option>
									<c:forEach items="${source}" var="sc">
										<option value="${sc.value}">${sc.text}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						

						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">????????????</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>
						
						<div style="position: relative;top: 15px;">
							<div class="form-group">
								<label for="create-contactSummary" class="col-sm-2 control-label">????????????</label>
								<div class="col-sm-10" style="width: 81%;">
									<textarea class="form-control" rows="3" id="create-contactSummary"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label for="create-nextContactTime" class="col-sm-2 control-label">??????????????????</label>
								<div class="col-sm-10" style="width: 300px;">
									<input type="text" class="form-control time" id="create-nextContactTime">
								</div>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>
						
						<div style="position: relative;top: 20px;">
							<div class="form-group">
                                <label for="create-address" class="col-sm-2 control-label">????????????</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="create-address"></textarea>
                                </div>
							</div>
						</div>
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" id="close-btn">??????</button>
					<button type="button" class="btn btn-primary" id="save-btn">??????</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- ??????????????????????????? -->
	<div class="modal fade" id="editClueModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 90%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">??</span>
					</button>
					<h4 class="modal-title">????????????</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal" role="form">
					
						<div class="form-group">
							<label for="edit-clueOwner" class="col-sm-2 control-label">?????????<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-clueOwner">

								</select>
							</div>
							<label for="edit-company" class="col-sm-2 control-label">??????<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-company">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-call" class="col-sm-2 control-label">??????</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-call">
								  <option></option>
									<c:forEach items="${appellation}" var="aplt">
										<option value="${aplt.value}">${aplt.text}</option>
									</c:forEach>
								</select>
							</div>
							<label for="edit-surname" class="col-sm-2 control-label">??????<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-surname">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-job" class="col-sm-2 control-label">??????</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-job">
							</div>
							<label for="edit-email" class="col-sm-2 control-label">??????</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-email">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-phone" class="col-sm-2 control-label">????????????</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-phone">
							</div>
							<label for="edit-website" class="col-sm-2 control-label">????????????</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-website">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-mphone" class="col-sm-2 control-label">??????</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-mphone">
							</div>
							<label for="edit-status" class="col-sm-2 control-label">????????????</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-status">
								  <option></option>
									<c:forEach items="${clueState}" var="cs">
										<option value="${cs.value}">${cs.text}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-source" class="col-sm-2 control-label">????????????</label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-source">
								  <option></option>
									<c:forEach items="${source}" var="sc">
										<option value="${sc.value}">${sc.text}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">??????</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe"></textarea>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative;"></div>
						
						<div style="position: relative;top: 15px;">
							<div class="form-group">
								<label for="edit-contactSummary" class="col-sm-2 control-label">????????????</label>
								<div class="col-sm-10" style="width: 81%;">
									<textarea class="form-control" rows="3" id="edit-contactSummary"></textarea>
								</div>
							</div>
							<div class="form-group">
								<label for="edit-nextContactTime" class="col-sm-2 control-label">??????????????????</label>
								<div class="col-sm-10" style="width: 300px;">
									<input type="text" class="form-control edit-time" id="edit-nextContactTime">
								</div>
							</div>
						</div>
						
						<div style="height: 1px; width: 103%; background-color: #D5D5D5; left: -13px; position: relative; top : 10px;"></div>

                        <div style="position: relative;top: 20px;">
                            <div class="form-group">
                                <label for="edit-address" class="col-sm-2 control-label">????????????</label>
                                <div class="col-sm-10" style="width: 81%;">
                                    <textarea class="form-control" rows="1" id="edit-address"></textarea>
                                </div>
                            </div>
                        </div>
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">??????</button>
					<button type="button" class="btn btn-primary" id="update-btn">??????</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>????????????</h3>
			</div>
		</div>
	</div>
	
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
	
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">??????</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">??????</div>
				      <input class="form-control" type="text" id="search-company">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">????????????</div>
				      <input class="form-control" type="text" id="search-phone">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">????????????</div>
					  <select class="form-control" id="search-source">
					  	  <option></option>
						  <c:forEach items="${source}" var="sc">
							  <option value="${sc.value}">${sc.text}</option>
						  </c:forEach>
					  </select>
				    </div>
				  </div>
				  
				  <br>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">?????????</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>
				  
				  
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">??????</div>
				      <input class="form-control" type="text" id="search-mphone">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">????????????</div>
					  <select class="form-control" id="search-state">
					  	<option></option>
						  <c:forEach items="${clueState}" var="cs">
							  <option value="${cs.value}">${cs.text}</option>
						  </c:forEach>
					  </select>
				    </div>
				  </div>

				  <button type="button" class="btn btn-default" id="search-btn">??????</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 40px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="create-btn"><span class="glyphicon glyphicon-plus"></span> ??????</button>
				  <button type="button" class="btn btn-default" id="edit-btn"><span class="glyphicon glyphicon-pencil"></span> ??????</button>
				  <button type="button" class="btn btn-danger" id="remove-btn"><span class="glyphicon glyphicon-minus"></span> ??????</button>
				</div>
				
				
			</div>
			<div style="position: relative;top: 50px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="qx" /></td>
							<td>??????</td>
							<td>??????</td>
							<td>????????????</td>
							<td>??????</td>
							<td>????????????</td>
							<td>?????????</td>
							<td>????????????</td>
						</tr>
					</thead>
					<tbody id="search-page">

					</tbody>
				</table>
			</div>
			
			<div style="height: 50px; position: relative;top: 60px;">
				<div id="cluePage">

				</div>
			</div>
			
		</div>
		
	</div>
</body>
</html>