<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<link rel="stylesheet" href="//code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
<head>
<style type="text/css">
.ui-datepicker-calendar {
    display: none;
}
</style>

</head>
<body>

<!-- Start breadcrumbs -->
	<div class="row">
		<div class="col-md-12">
			<div class="breadcrumbs"><span>
				<s:text name="Analyze"/> </span> <img src="images/bread-arrow.png"> 
				  <a href="rpa_analyze.action"><s:text name="Rate Plan Analysis" /></a>
			</div>
		</div>
	</div>
<!-- End breadcrumbs -->

<!-- Start phead -->
	<div class="row">
		<div class="col-md-12">
			<div class="phead"><span class="mhead"> <s:text name="Analyze" />
				<!-- <a href="#">
					<img src="images/analysis.png" title="Analyze"  style="width:25px;height:25px;  margin-left: 831px;	"
						     data-toggle="modal" data-target="#addCarrierDiv" onClick="analysisModel()"  >
				</a> --> 
				</span>  <br clear="all" />
			</div>
		</div>
	</div>
	<!-- End phead -->					
					
<!-- Search Criteria -->

 <div class="row form" id="searchDiv">
    <div class="col-md-4" style="vertical-align: top;">
    	<div class="form-group">
    		<font style="font-family:tahoma;font-weight:bold;white-space: nowrap;">Carrier &amp; Raw Data Format<br></font>
    	</div>
    	<div class="form-group">
              <label class="control-label col-sm-4" for="inputEmail" style="width:30%">Carrier</label>
              <div class="col-sm-8">
             	<s:select id="carrierId" list="#request.CARRIER" listKey="companyCarrierId" listValue="carrierName" 
               					cssClass="form-control"  onchange="rawDataBillDates()">
               	</s:select> 
             </div>
        </div>
        
        <!-- <img src="images/loading_confrm.gif" id="buttonProcess"  style="display: none;position:absolute;left:620px;"> -->
        
        <div class="form-group">
              <label class="control-label col-sm-4" for="inputEmail" style="width:30%">Data Format</label>
              <div class="col-sm-8">
             	<select id="dataForm" Class="form-control"  onchange="rawDataBillDates()">
					<option value="1"> Verizon RDD</option>
				</select>
             </div>
        </div>  
    
    </div>
    
    <div class="col-md-4" style="border-right : 1px solid #CAD5DE;border-left : 1px solid #CAD5DE;vertical-align: top;">
    	<div class="form-group">
    		<font style="font-family:tahoma;font-weight:bold;white-space: nowrap;">Bill Cycle Dates<br></font>
    	</div>
    	<div id="datesDivision" style="height:206px; overflow-y: auto;"></div>
    	<!-- <div id="datesDivision" style="height:300px"></div> -->
    </div>
    
    <div class="col-md-4" style="vertical-align: top;">
    	<s:form id="rpAnalysisForm"  method="post"  enctype="multipart/form-data"  theme="simple">
    		<div class="form-group">
    			<font style="font-family:tahoma;font-weight:bold;white-space: nowrap;">Analysis Details<br></font>
    		</div>
    		<div class="form-group">
              <label class="control-label col-sm-4" for="inputEmail" style="width:30%">Name <font style="color:red">*</font></label>
              <div class="col-sm-8">
             	<input type="text" class="form-control" id="name" value="" name="name" maxlength="35"
									style="border: 1px solid #96BEDD;border-radius: 4px 4px 4px 4px;background: #FFFFFF;"> 
             </div>
           </div>
           
           <div class="form-group">
              <label class="control-label col-sm-4" for="inputEmail" style="width:30%">From Date <font style="color:red">*</font></label>
              <div class="col-sm-8">
             	<input type="text" class="form-control" id="fromDate" value="" name="fromDate" maxlength="35"
									style="border: 1px solid #96BEDD;border-radius: 4px 4px 4px 4px;background: #FFFFFF;" 
									onclick="setDate(this.value,'fromDate')">
             </div>
           </div>
           
           <div class="form-group">
              <label class="control-label col-sm-4" for="inputEmail" style="width:30%">To Date <font style="color:red">*</font></label>
              <div class="col-sm-8">
             	<input type="text" class="form-control" id="toDate" value="" name="toDate" maxlength="35"
									style="border: 1px solid #96BEDD;border-radius: 4px 4px 4px 4px;background: #FFFFFF;" 
									onclick="setDate(this.value,'toDate')">
             </div>
           </div>
           
           <div class="form-group">
              <label class="control-label col-sm-4" for="inputEmail" style="width:30%">Notes<font style="color:red">&nbsp;</font></label>
              <div class="col-sm-8">
             	<input type="text" class="form-control" id="notes" value="" name="notes" maxlength="35"
									style="border: 1px solid #96BEDD;border-radius: 4px 4px 4px 4px;background: #FFFFFF;">
             </div>
           </div>
           
           <div class="row">
           		<div class="col-sm-12 text-right">
	             	<button type="button" class="btn btn-success" onclick="rpaAction();" id="importButton" > Analyze </button>
					<img src="images/loading_confrm.gif" id="updateProgressBar1" style="dispaly:none;"/>
					<button type="button" class="btn btn-danger" data-dismiss="modal">Cancel</button>
				</div>
           </div>
	 	</s:form>
    </div>
    
 </div>
 <br/>
  <s:form action="rpa_analyzeDetails.action" id="analyzeDetailsPage"> </s:form> 	 
  <input type="hidden" id="minDate"/>
  <input type="hidden" id="maxDate"/>
  
  <div class="row"  id = "searchDataDeatails"></div>

<s:form action="rpa_exportLogFile.action" id="fileDownload">
<input type="hidden" id="analysisIDValue" name="analysisId"/>
</s:form>  
  </body>
  
  <script>
		    $( document ).ready(function() {
		        $('#updateProgressBar1').hide();
		        $('#importButton').show();
		        fromDate();
		        rawDataBillDates();
		        
		        //fetchingInventoryDetails(0);
		        $('#loaddiv').css("display","none");
		        $('#dvLoading').css("display","none");
		        
		    });
    function analysisModel(){
    	  $('#updateProgressBar1').hide();
          $('#importButton').show();
    }

    function rpaAction(){
        var fromDatee =$('#fromDate').val();
        var toDatee =$('#toDate').val();
        var minDatee =$('#minDate').val();
        var maxDatee =$('#maxDate').val();
     // Convert all dates into date object for comparision between dates.                 
        var fromDate = new Date(fromDatee);
        var toDate = new Date(toDatee);
        var minDate = new Date(minDatee);
        var maxDate = new Date(maxDatee);
        //console.log(' check Validation is  : '+(minDatee == maxDatee && fromDatee == toDatee ));
         $('#updateProgressBar1').show();
         $('#importButton').hide();
       	rpaAnalize(fromDatee,toDatee); 
    }

    function exportLogFile(ratePlanId){
		//console.log(' Rate Plan ID is : '+ratePlanId);
    	  var actionURL = "rpa_exportLogFile.action";
    	  $('#analysisIDValue').val(ratePlanId);
    	  document.fileDownload.action = actionURL;
    		document.fileDownload.submit();
	}


    function reAnalysis(analysisId,fromDate, toDate){
        // console.log(' ReAnalysis ID is : '+analysisId+'from Date is '+fromDate+' toDate is '+toDate);
         $('#progresssBar'+analysisId).show();
         $('#icon'+analysisId).hide();
           var actionURL = "rpa_rpaReAnalize.action";
           $.ajax({
        		url :actionURL,
        		dataType: "html",
        		data: { analysisId : analysisId, fromDate :fromDate ,toDate: toDate,carrierId : $('#carrierId').val(),dataFormate:$('#dataForm').val()},
               type:"post",
               cache:false,
        		success : function(data) {
        			var sessionExpair = data.indexOf("Session expired");
        			  if(sessionExpair != -1){
        				        	  location.reload();
        				        	  return 0;
        				          }
        			            		
       			console.log(' Dataa is '+data);
       			$('#progresssBar'+analysisId).hide();
       		     $('#icon'+analysisId).show();
        		 	var successIndex;
        		 	var newUsersCount = 0;
        		 	var missingRatePlansCount = 0;
       		  $.each(JSON.parse(data),function(index, value) {
       		// Sample OutPUt is : Dataa is [{"newUsersCount":777,"result":"success","missingRatePlansCount":41,"missingCostCenterCode":780}]
       			successIndex = value.result.indexOf("success");
       			newUsersCount = value.newUsersCount;
       			missingRatePlansCount = value.missingRatePlansCount; 	
			      });
       		$('#progresssBar'+analysisId).closest('td').prev('td').prev('td').text(missingRatePlansCount);
       		$('#progresssBar'+analysisId).closest('td').prev('td').prev('td').prev('td').text(newUsersCount);

       			 if(  successIndex == 0 ){
   				  		jAlert('Analysis done successfully');
   					//	$('#analyzeDetailsPage').submit();  
   						return 0;
   			}else{
   						jAlert('Analysis failed, please try again !...');
   						return 0;
   			}
        		}
        	});
         return 0;   
        }

    
    function rpaAnalize(fromDatee,toDatee){
        var actionURL = "rpa_rpaAnalize.action";
        $('form#rpAnalysisForm').ajaxSubmit({
    		url :actionURL,
    		dataType: "html",
    		data: { carrierId : $('#carrierId').val(),dataFormate:$('#dataForm').val()},
           type:"post",
           cache:false,
    		success : function(data) {
        			var successIndex ;
	    			var failIndex ;
	    			var nameDuplication;
	    			 $.each(JSON.parse(data),function(index, value) {
		    			 //Dataa is [{"newUsersCount":777,"result":"success","missingRatePlansCount":41,"missingCostCenterCode":780}]
	    	       			successIndex = value.result.indexOf("success");
		    	       		 failIndex  = value.result.indexOf("fail");
		 	    			nameDuplication = value.result.indexOf('Name already Existed');
	    	      });
					$('#importButton').show();
					$('#updateProgressBar1').hide();
					console.log(' rpaAnalize() Response is '+data); 
					if( nameDuplication == 0 ){
						$('#name').focus();
						jAlert(' Name Already Existed !, Please try with new name ...');
						return 0;
					}
					else  if(  successIndex == 0 ){
						  		jAlert('Analysis done successfully');
								$('#analyzeDetailsPage').submit();
								return 0;
					}else{
								jAlert('Analysis failed, please try again !...');
								return 0;
					}
					
    		}
    	});
    }
    function rawDataBillDates(){
        var actionURL = "rpa_billCycleDates.action";
    	$.ajax({
    		url :actionURL,
    		dataType: "html",
    		async : false,
    		data: { carrierId : $('#carrierId').val(),dataFormate:$('#dataForm').val()},
           type:"post",
           cache:false,
    		success : function(Data) {
					processData(Data);
					showHideDivs();
    		}
    	});
    	fetchingInventoryDetails(0);	
    }
    
 // Pagination Script ...

    function fetchingInventoryDetails(pageIndex) {
		var URL = "rpa_ratePlanAnslysisData.action";
		URL = URL + "?currentPage=" + pageIndex;
		// $('#customerCatalogPlansDivision').replaceWith('');
		$.ajax({
			url : URL,
			type : 'post',
			data : {
				carrierId : $('#carrierId').val(),
				dataFormate : $('#dataForm').val()
			},
			success : function(data, status) {
				var sessionExpair = data.indexOf("Session expired");
				//console.log('Check Sessio nExpair Index '+sessionExpair);
				if (sessionExpair != -1) {
					location.reload();
					return 0;
				}
				$('#searchDataDeatails').replaceWith(data);
			},
			error : function(xhr, desc, err) {
				console.log(xhr);
				console.log("Desc: " + desc + "\nErr:" + err);
			}
		});
	}
			
			function processData(jsonData) {
				console.log(' Json Data is ' + jsonData);
				$.each(JSON.parse(jsonData),
				function(index, value) {
					if (value.isDataExisted) {
						$('#minDate').val(value.minDate);
						$('#maxDate').val(value.maxDate);
						var yearsOrder = new Array();
						yearsOrder = value.yearsOrder.split(",");
						var dates = value.datesList;
						var script = "";
						var imgName = "images/acd_open_arrow.png";
						var style = "display:block;padding:5px;";

						yearsOrder.forEach( function( yearOrder ) {
								$.each( dates, function( year, months ) {
									 if( yearOrder == year ) {
												script = script+ "<div id='"+ year	+"' onclick='showHideDivs(this.id)' style='cursor:pointer;border-bottom: 2px solid #CAD5DE;padding:10px;'>"+
																											 "<img id='"+year+"-img' src='"+imgName+"' width='20' height='20' />"+
																											 "<b>"+year+ "</b> (year)";
											script = script+"<ul id = '"+year+"-list' style='"+style+"'>";
											
											$.each(months, function(month) {
														script = script+ "<li style='padding-left:20px; padding-top:5px; padding-bottom:5px;'>"+ months[month]	+ "</li>";
											});
											script = script+"</ul>";
											script += "</div>";
											imgName = "images/acd_close_arrow.png";
											style = "display:none";
									 }// End of YearOrder == Year
							}); // Ending of $.each(dates, function(year, months) {
						}); // Ending of yearsOrder.forEach(function(yearOrder){
						
						$("#datesDivision").html("").append(script);

					} else {
						console.log(" Data Not Existed ");
						$("#datesDivision").html("").append(" No records to display ");
					}
				});
			}

			function showHideDivs(clickDivId) {
				var childTableId = $('#' + clickDivId + '-list').attr("id");
				var childImageId = $('#' + clickDivId + '-img').attr("id");

				if ($('#' + childTableId).css('display') == "none") {
					$("#" + childImageId).attr("src",	"images/acd_open_arrow.png");
					$("#" + childTableId).slideDown('slow');
				} else {
					$("#" + childImageId).attr("src", "images/acd_close_arrow.png");
					$("#" + childTableId).slideUp('slow');
				}

			}

			function slideMe(divid, imgid, imgtitle) {
				if (document.getElementById(divid).style.display == ""	|| document.getElementById(divid).style.display == "block") {
					$("#" + imgid).attr("src",	"images/" + imgtitle + "_close_arrow.png");
					$("#" + divid).slideUp('slow');
				} else {
					$("#" + imgid).attr("src",	"images/" + imgtitle + "_open_arrow.png");
					$("#" + divid).slideDown('slow');
				}
			}

			function setDate(value, id) {
				if (value != ''){
					value = $.trim(value);
					var newDate = $.datepicker.parseDate('M,yy,dd', value+",1");
					$("#" + id).datepicker('setDate', newDate);
				}
			}

			function fromDate() {
				var pickerOpts = {
					changeMonth : true,
					changeYear : true,
					showButtonPanel : true,
					dateFormat : 'MMM yy',
					onClose : function(dateText, inst) {
						var month = $("#ui-datepicker-div .ui-datepicker-month :selected").val();
						var year = $("#ui-datepicker-div .ui-datepicker-year :selected").val();
						$(this).datepicker('setDate', new Date(year, month, 1));
					}
				};

				$("#fromDate, #toDate").datepicker({
					format : "MM-yyyy",
					viewMode : "months",
					minViewMode : "months"
				});

				$(".ui-datepicker-calendar").css("display", "none")
			}
		</script>	
