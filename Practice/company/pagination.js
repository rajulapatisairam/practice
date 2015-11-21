function applyPagination(tableId, displayPageNumber, size,totalPages,currentPage,paginationIndex,javaScriptMethodName  ){
	var pagesLimit =5;
	var tableRecordsLimit = 10;
	var minimumPagination = pagesLimit * tableRecordsLimit; 
	currentPage = parseInt(currentPage);
	
	 $(tableId).tablesorter(); 
	// Pagination Starting Script.
	 var script = '<ul class="pagination bootpag">';
	 // Pagination is not required for no records and single page (i.e only 10 records).
	 if(size<=tableRecordsLimit){
		  return 0;
	 } else if( size <= minimumPagination){
		 var overFlow = (size%10);
		 if(overFlow == 0){
			 totalPages = totalPages -1;
		 }
		 
		 for (var  count = 0 ;count< totalPages; count++){	
			 script = script +'<li data-lp=0 class="" id='+(count+1)+'><a href="javascript:'+javaScriptMethodName+'('+count+');">'+(count+1)+'</a></li>';
		 }
	 }else{
	 // Pagination Starting index.

	 var previousPage = parseInt(currentPage-1);
	 var nextPage = parseInt(currentPage+1);

	 if( currentPage == 0){
		 //script = script +'<li data-lp="1" class="prev disabled"> <a href="javascript:void(0)">ï¿½ </a></li>';
	 }else{
		 script = script +'<li data-lp="0" class=""><a href="javascript:'+javaScriptMethodName+'(0);">First</a></li>';
		 script = script +'<li data-lp="1" class="prev"> <a href="javascript:'+javaScriptMethodName+'('+previousPage+');"> << </a></li>';
	 }

	var paginationLimit = paginationIndex+pagesLimit;
	
	if( paginationLimit >= totalPages){
		paginationIndex = totalPages -pagesLimit;
		paginationLimit = totalPages;
	}
	
	if(currentPage >1 && (paginationLimit != totalPages)){
		paginationIndex = paginationIndex -2;
		paginationLimit = paginationLimit -2;
	}
	 for (var  count = paginationIndex ;count<paginationLimit; count++){
		 script = script +'<li data-lp="'+count+'" class="" id='+(count+1)+'><a href="javascript:'+javaScriptMethodName+'('+count+');">'+(count+1)+'</a></li>';
	 }
	
	// Pagination Ending Script.
	 if(  ( currentPage >= totalPages )  || ( displayPageNumber == totalPages ) ){
		// script = script +'<li data-lp="'+count+'" class="next disabled"><a href="javascript:void(0)">ï¿½</a></li>';
		 } else{
			 script = script +'<li data-lp="'+count+'" class="next"><a href="javascript:'+javaScriptMethodName+'('+nextPage+');"> >> </a></li>';
			 script = script +'<li data-lp="0" class=""><a href="javascript:'+javaScriptMethodName+'(-1);">Last</a></li>';
		 }
	 }
	 script = script +'</ul>';
	//$('#page-selection').append('Current page '+displayPageNumber+'</br>'+script);
	$('#page-selection').append(script);
	$('#'+displayPageNumber).addClass('active');
	
}

function fetchingTableData(url,pageIndex,SearchFormId,dataDisplayId){
	
		   var URL = url+"?currentPage="+pageIndex;
		   // $('#customerCatalogPlansDivision').replaceWith('');
		   $.ajax({
			   url: URL,
			   type: 'post',
			  // data: {currentPage:pageIndex},
			   data : $("#"+SearchFormId).serialize(),
			   success: function (data, status) {
		//	     console.log("Success!!");
		          var sessionExpair = data.indexOf("Session expired");
		     //     console.log('Check Sessio nExpair Index '+sessionExpair);
		          if(sessionExpair != -1){
		        	  location.reload();
		        	  return 0;
		          }
			     $('#'+dataDisplayId).replaceWith(data);
			   },
			   error: function (xhr, desc, err) {
			     console.log(xhr);
			     console.log("Desc: " + desc + "\nErr:" + err);
			   }
			 });
}
