<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<script type = "text/javascript">
var results =[];
var refference = 0;
$( document ).ready(function() {
	 $('input[type="radio"]').change(function() {
		   radioButtonAction(this);
		});
	
    $('input[type="checkbox"]').change(function() {
   checkBoxAction(this);
});
});
function radioButtonAction(radio){
	var refferenceNumber = $(radio).attr('value');
$.get("http://localhost:8080/Practice/Practice/testing_studentAction.action",{refference : refferenceNumber,name:'sairam rajulapati'}, function(data, status){
    alert("Data: " + data+' and result is: '+data.result+"\n Student Recor5ds "+data.studentsRecordss);
    // studentTable
    //studentsRecordss
    //numbersDIv
    $.each(data.numbers,function(index,value){
    	$('#numbersDIv').append('<br>'+value);
    });
    
    var content = '';
    content += '<tbody>';
    $('#studentTable').find("tr:gt(0)").remove();
    $.each(data.studentsRecordss,function(index,value){
     index=index+1;
    // alert('Index is: '+index+" value is: "+value.empName);
     
     content += '<tr >';
     
     content += '<td> <input type="text" 	readonly value='+value.messageType+' id='+value.refferenceName+'t1 name="msgType" /></td>';
     content += '<td> <input type="text" 	readonly value='+value.refferenceName+' id='+value.refferenceName+'t2 name="refName" /> </td>';
     content += '<td >'+value.studentName+'</td>';
     content += "<td> <input type='text' name ='original' onblur=changeAction("+value.messageType+","+value.refferenceName+",this) ></td>";
     content += "<td  style='display: none;'> <input type='text' 	readonly name='original' value="+value.messageType+" ></td>";
     content +='</tr>';
     
    });
    content += '</tbody>';
    $('#studentTable').append(content);
    
});
}
function changeAction(messageType,refferenceName,currentValue){
	
	if($(currentValue).val()!=''){
	//alert(' message Type is: '+messageType+'\n Refference Name is'+refferenceName+'\n Current Value is '+$(currentValue).val());
	var msgType = $('#'+refferenceName+'t1');
	var reffName = $('#'+refferenceName+'t2');
	var diffrence = parseInt($(msgType).val()) - parseInt($(currentValue).val());
	var newValue = parseInt($(reffName).val()) + parseInt($(currentValue).val());
	$(msgType).val(diffrence);
	$(reffName).val(newValue);
	
	}
} 

function checkBoxAction(checkBox){
var checkBoxCount = $('input[type="checkbox"]:checked').length;

var currentSelected = $(checkBox).attr('class');
if(checkBoxCount === 1){
refference = $(checkBox).attr('class');
}
if(checkBoxCount !=0 && $(checkBox).prop('checked') && refference != currentSelected) {
alert ("Your Selected " + $(checkBox).attr('class') + " But ONly Allowed "+refference);
$(checkBox).prop('checked',false);
}

}
</script>
</head>
<body>
<table style="width:50%" border="1">
  <tr>
    <th>Check Box</th>
    <th> Radio</th>
	<th>Refference</th>
	<th>Message Type</th>
	<th>Student Name</th>		
  </tr>
  <s:iterator value='#request.studentRecords' var="tableName" status="itStatus" id="attr">
  <tr>
     <td> <s:checkbox theme="simple" name="messageType" cssClass="%{#attr.messageType}" value="false"/></td>
     <td> <s:radio theme="simple" name="reffernceName"  list="{refferenceName}" /></td>
     
   <td><s:property value="refferenceName"/></td>
   <td><s:property value="messageType"/></td>
   <td><s:property value="studentName"/></td> 
  </tr>
  </s:iterator>
</table>
<br><br>
<table id="studentTable" border="1">
<tr>
<td> MessageType </td>
<td> Refference Name </td>
<td> Studdent Name </td>
<tr>
</table>

<br>
<br>
<div id="numbersDIv">

</div>

 </body>
</html>
