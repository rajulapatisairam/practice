<%@ taglib uri="/struts-tags" prefix="s"%>
<script src="<s:url value="/scripts/common.js"/>" ></script>

<div id="customerCatalogPlansDivision" >	
			<table  style="width:100%;"  id="rptblData" class="table table-hover table-bordered listmrg">
			<thead>
			<tr>	
					<th> Plan Code <i class="fa fa-fw fa-sort"></i></th>
					<th> Plan Name <i class="fa fa-fw fa-sort"></i></th>
					<th> Voice <i class="fa fa-fw fa-sort"></i></th>
					<th> Data  <i class="fa fa-fw fa-sort"></i></th>
					<th> Text  <i class="fa fa-fw fa-sort"></i></th>
					<th> Cost  <i class="fa fa-fw fa-sort"></i></th>
					<th> Edit </th> 	
					</tr>	
					</thead>
				<s:iterator value="#request.plans" status="index" var="plan">
					<tr>
					<td><s:property value="#plan.planCode"/> </td>
					<td><s:property value="#plan.planName"/> </td>
					<td><s:property value="#plan.includedPeakMins"/> </td>
					<td><s:property value="#plan.includedData"/> </td>
					<td><s:property value="#plan.includedText"/> </td>
					<td><s:property value="#plan.totalMrc"/> </td>
					<td><a href="javascript:editDataFeatuer('<s:property value="#plan.rateLibID"/>')">
				<img src="images/actions/edit-icon.png" width="25" id="editImageId" height="26" alt="edit" title="edit"></a> </td>
					</tr>
				</s:iterator>		
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
			 // $('#paginationDiv').append('<h6>Pagination Work Started! </h6>');
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
			 
				// Pagination Starting Script.
			 var script = '<ul class="pagination bootpag">';
			 // Pagination is not required for no records and single page (i.e only 10 records).
			 if(size<=tableRecordsLimit){
				  return 0;
			 } else if( size <= minimumPagination){
				 for (var  count = 0 ;count<=totalPages; count++){	
					 script = script +'<li data-lp=0 class=""><a href="javascript:fetchingCarrierPlans('+count+');">'+(count+1)+'</a></li>';
				 }
			 }else{
			 // Pagination Starting index.
			 var paginationIndex = <s:property value="#request.paginationIndex"/>;
			 var previousPage = parseInt(currentPage-1);
			 var nextPage = parseInt(currentPage+1);

			 if( currentPage == 0){
				 //script = script +'<li data-lp="1" class="prev disabled"> <a href="javascript:void(0)">« </a></li>';
			 }else{
				 script = script +'<li data-lp="0" class=""><a href="javascript:fetchingCarrierPlans(0);">First</a></li>';
				 script = script +'<li data-lp="1" class="prev"> <a href="javascript:fetchingCarrierPlans('+previousPage+');">« </a></li>';
			 }

			var paginationLimit = paginationIndex+pagesLimit;
			if( paginationLimit > totalPages){
				paginationIndex = totalPages -pagesLimit;
				paginationLimit = totalPages;
			}
			 for (var  count = paginationIndex ;count<paginationLimit; count++){
				 script = script +'<li data-lp="'+count+'" class=""><a href="javascript:fetchingCarrierPlans('+count+');">'+(count+1)+'</a></li>';
			 }
			
			// Pagination Ending Script.
			 if(  currentPage >= totalPages){
				// script = script +'<li data-lp="'+count+'" class="next disabled"><a href="javascript:void(0)">»</a></li>';
				 } else{
					 script = script +'<li data-lp="'+count+'" class="next"><a href="javascript:fetchingCarrierPlans('+nextPage+');">»</a></li>';
					 script = script +'<li data-lp="0" class=""><a href="javascript:fetchingCarrierPlans(-1);">Last</a></li>';
				 }
			 }
			 script = script +'</ul>';
			$('#page-selection').append('Current page '+displayPageNumber+'</br>'+script);
			} );
		</script>
		
