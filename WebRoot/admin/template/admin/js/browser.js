//扩展3:30 2009-3-2
//************** String functions **************
function gE(__id)
{
	return document.getElementById(__id);
}
	
Number.prototype.Fixed=function(n)
{
	var tmp=Math.pow(10,n);
	return Math.round(this*tmp)/tmp;
}
	
String.prototype.appendString=function(maxlength,code,position){
	var i,appendValue="";
	var l=this.length;
	for(i=0;i<maxlength-l;i++)
	{
		appendValue=appendValue+code;
	}
	if (position)
		return appendValue+this;
	else
		return this+appendValue;
}
	
String.prototype.trim=function(){  //删除左右两端的空格
		
	return this.replace(/(^\s*)|(\s*$)/g, "");
}
	
String.prototype.left = function(nLength){
	return this.substr(0, nLength);
};
String.prototype.right = function(nLength){
	return this.substr(this.length - nLength, nLength);
};
String.prototype.mid = function(index, nLength){
	return this.substr(index, nLength);
};
	
String.prototype.count = function(char){
	var ar = this.split(char);
	return (ar.length - 1);
}
	
String.prototype.isNumber=function()
{
	if(!this) return false;
	var strP=/^\d+(\.\d+)?$/;
	if(!strP.test(this)) return false;
	try{
		if(parseFloat(this)!=this) return false;
	}
	catch(ex)
	{
		return false;
	}
	return true;
}
	
String.prototype.replaceAll = function(reallyDo, replaceWith, ignoreCase) {
	if (!RegExp.prototype.isPrototypeOf(reallyDo)) {
	    return this.replace(new RegExp(reallyDo, (ignoreCase ? "gi": "g")), replaceWith);
	} else {
	    return this.replace(reallyDo, replaceWith);
	}
}
	
Array.prototype.isInArray = function(v){
	for( var i=0;i < this.length;i++ )
	{
		if(v == this[i])
		{
			return true;
		}
	}
	return false;
}

Array.prototype.find = function (func) {
    var temp = [];
    for (var i = 0; i < this.length; i++) {
        if (func(this[i])) {
            temp[temp.length] = this[i];
        }
    }
    return temp;
}


String.prototype.formatToDate = function (_format) {

    var _this = this.replaceAll("-", "/");
    
    var myDate = new Date(_this);

    if (isNaN(myDate.getFullYear())) {
        return "";
    }
    return myDate.format(_format);
}
//扩展3:30 2009-3-2
String.prototype.QueryString=function(sArgName)
{
	return QueryString(sArgName,this);
}
	
	
function QueryString(sArgName,sStr)
{
	var sURL;

	if (sStr)
		sURL=sStr;
	else
		sURL=location.toString()

	var tempStr,urlargment;
	sArgName=sArgName.toLowerCase();

	if (sURL.split("?").length==1)
		return "";
		
	urlargment=sURL.split("?")[1];
	tempStr = urlargment.split("&");

	var returnValue="";

	for(i=0;i<tempStr.length;i++) {
	    //alert(tempStr[i].toString().toLowerCase() + "\n" + sArgName + "\n" + tempStr[i].toString().toLowerCase().indexOf(sArgName + "="));
	    if (tempStr[i].toString().toLowerCase().indexOf(sArgName + "=") == 0) {
	        if (returnValue=="") {
	            returnValue = tempStr[i].split("=")[1];
	        }
	        else {
	            returnValue += ","+tempStr[i].split("=")[1];
	        }
	    }
	}
	return returnValue;
}

//json相关==============================
function Obj2str(o) {
    //将json对象转为string
	if (o == undefined) {
	    return "";
	}
	var r = [],i;
	if (typeof o == "string") return "\"" + o.replace(/([\"\\])/g, "\\$1").replace(/(\n)/g, "\\n").replace(/(\r)/g, "\\r").replace(/(\t)/g, "\\t") + "\"";
	if (typeof o == "object") {
	    if (!o.sort) {
	        for (i in o)
	            r.push("\"" + i + "\":" + Obj2str(o[i]));
	        if (!!document.all && !/^\n?function\s*toString\(\)\s*\{\n?\s*\[native code\]\n?\s*\}\n?\s*$/.test(o.toString)) {
	            r.push("toString:" + o.toString.toString());
	        }
	        r = "{" + r.join() + "}"
	    } else {
	        for (i = 0; i < o.length; i++)
	            r.push(Obj2str(o[i]))
	        r = "[" + r.join() + "]";
	    }
	    return r;
	}
	return o.toString().replace(/\"\:/g, '":""');
}

function jsonunescape(obj) {
    for (x in obj) {
        obj[x] = unescape(obj[x]);
    }
}