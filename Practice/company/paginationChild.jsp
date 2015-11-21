<%@ taglib uri="/struts-tags" prefix="s"%>
<script src="<s:url value="/scripts/common.js"/>" ></script>
<script src="<s:url value="/js/pagination.js"/>" ></script>
<!--  Table Sorting Icon link -->
<link href="//maxcdn.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css" rel="stylesheet">
<!--  Table SOrting  -->
<script src="js/jquery.tablesorter.js"> </script>
<script src="js/jquery.tablesorter.min.js"> </script>
<script src="js/pagination.js"> </script>
<!--  Table SOrting  Ending-->


<div id="searchDataDeatails" >	
			<table  style="width:100%;"  id="rptblData" class="table table-hover table-bordered listmrg">
			<thead>
			<tr>	
         						<th>Analysis Name <i class="fa fa-fw fa-sort" style = "cursor: pointer;"></i></th>
         						<th>From Date <i class="fa fa-fw fa-sort" style = "cursor: pointer;"></i></th>
         						<th>To Date <i class="fa fa-fw fa-sort" style = "cursor: pointer;"></i></th>
         						<th>Notes <i class="fa fa-fw fa-sort" style = "cursor: pointer;"></i></th>
         						<th>New Users </th>
         						<th>Missing Rate Plans </th>
         						<th> Export Log File </th>
         						<th> Re Analysis </th>
					</tr>	
					</thead>
				<s:if test="#request.results.size>0">
					
				<s:iterator value="#request.results" status="index" var="plan">
					<tr>
         							<td><s:property value="name"/></td>
         							<td><s:date name="fromDate" format="dd-MMM-yyyy" />  </td>
         							<td><s:date name="toDate" format="dd-MMM-yyyy" /></td>
         							<td><s:property value="notes"/></td>
									<td><s:property value="newUsersCount"/></td>
									<td><s:property value="missingRatePlanCount"/></td>
					<td><a href="#" onclick="exportLogFile(<s:property value="ratePlanAnalysisId"/>)"> <img src="images/actions/edit-icon.png" width="25" id="editImageId" height="26" alt="edit" title="edit"></a></td>
					<td>
					<img src="images/loading_confrm.gif" id="progresssBar<s:property value="ratePlanAnalysisId"/>"  class="loading"/>
					<a href="#" onclick="reAnalysis(<s:property value="ratePlanAnalysisId"/>,'<s:date name="fromDate" format="yyyy-MM-dd" />','<s:date name="toDate" format="yyyy-MM-dd" />')"> <img src="images/actions/edit-icon.png" width="25" id="icon<s:property value="ratePlanAnalysisId"/>" height="26" alt="edit" title="edit"></a>
					</td>
					</tr>
				</s:iterator>
				</s:if>
				<s:else>
				<tr>
				<td colspan="9" align="center"><s:text name="No records to display" /></td>
				</tr>
				</s:else>			
			</table>
			<div class="row">
			<div class="col-md-12 text-left">
						Total Records : <s:property value="#request.maximumRowCount"/>
			</div>
	<div class="col-md-12 text-right">	 
	<div id="page-selection"></div>
</div>
</div>
</div>
		<script>
		$(document).ready(function() {
			$('.loading').hide();
			 // $('#paginationDiv').append('<h6>Pagination Work Started! </h6>');
			 var tableId =$('#rptblData'); 
			var pagesLimit =5;
			var tableRecordsLimit = 10;
			var minimumPagination = pagesLimit * tableRecordsLimit;   
			var displayPageNumber = <s:property value="#request.displayPageNumber"/>;
			 // Total Number of records.
			 var size  = <s:property value="#request.maximumRowCount"/>;
			 // Total Number pages in pagination, 10 records /Page.
			 var totalPages  = <s:property value="#request.totalPages"/>;
			 // Current Pointing Page.
			 var currentPage = <s:property value="#request.currentPage"/>;
			 		currentPage = parseInt(currentPage);
			 var paginationIndex = <s:property value="#request.paginationIndex"/>;
             var javaScriptMethodName = "fetchingInventoryDetails";
			 applyPagination(tableId, displayPageNumber, size,totalPages,currentPage,paginationIndex,javaScriptMethodName  );
		}); 
		
		</script>
		
