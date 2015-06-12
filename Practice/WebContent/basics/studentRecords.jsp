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
$.get("http://localhost:8080/Practice/Practice/testing_studentAction.action",{refference : refferenceNumber}, function(data, status){
    alert("Data: " + data );
});
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
  <s:iterator value='#request.studentRecords' var="tableName" status="itStatus">
  <tr>
   <td><input type="checkbox" class="<s:property value="messageType"/>"/></td>
   <td><input type="radio" name="reffernceName" value="<s:property value="refferenceName"/>" /></td>
   <td><s:property value="refferenceName"/></td>
   <td><s:property value="messageType"/></td>
   <td><s:property value="studentName"/></td> 
  </tr>
  </s:iterator>
</table>
 </body>
</html>
