/**
 * Script para BookTube
 */

window.onload = initAll;
var debugWin=null;
var selectedItem = null;

function debugWinIsOpen(){	
	if( debugWin && !debugWin.closed){
		return true;
	}
	return false;
}

function initDebugWindow(){
	var debugWinWidth = 300;
	var debugWinHeight = 300;
	var xPos, yPos;

	if( !debugWinIsOpen()){
		xPos = screen.availWidth-debugWinWidth;
		yPos = screen.availHeight-debugWinHeight;
		debugWin = window.open("","debugWin", "toolbar,location=no,width="+debugWinWidth+",height="+debugWinHeight+",left="+xPos+",top="+yPos);		
		addContent(debugWin);
		debugWin.focus();
	}
}

function initAll(){
	//initDebugWindow();
}

function addContent(aWindow){
	var newDoc = aWindow.document;
	var newBr, newText;
	
	newDoc.title = "Debug Window";
	newDoc.body.innerHTML = "<div id='content'> </div>";
	newDoc.bgColor = "#FFFFFF";
	newDoc.fgColor = "#000000";
//	log("<< Console >>");

	
//	console.log("hola");
//	console.log("chan");
//	newDoc.write("<p>holaaaa</p>");
//	newDoc.write("<p>jaque</p>");
	
//	var loopDiv = newDoc.getElementById("looper");
//	for (var i=0; i<3; i++) {
//		newText = newDoc.createTextNode("The loop is now at: " + i);
//		newBr = newDoc.createElement("br");
//		
//		loopDiv.appendChild(newText);
//		loopDiv.appendChild(newBr);
//	}
}

function log( aString){
	windowWrite( debugWin, aString);
}

function windowWrite(aWindow, aString){
	if( aWindow ){
		var newDoc = aWindow.document;
		var newLine = newDoc.createElement("<p>"); 
		newLine.innerHTML = aString;			
		newDoc.getElementById("content").appendChild(newLine);
	}else{
		alert("Target Window does not exist");
	}
}

function setClassSelected(anItem){
	setClass(anItem, "selected");
}

function setClass(anItem, newClassName){
	alert("hola fooot");
//	log("selectMenuItemQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ");
//	debugWin.document.write("<p>lllllllllllllllllllllllllllllllllllllllllllll</p>");
//	anItem.class = "selected";
	anItem.setAttribute("class", newClassName);
//	console.log(anItem.class);
	console.log(anItem);
}

