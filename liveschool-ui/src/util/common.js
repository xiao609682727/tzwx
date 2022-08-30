



export function getQuesType(index) {
	//单选
	if(index == 1){
		return "单选题";
	}
	//多选题
	if(index == 2){
		return "多选题";
	}
	//判断题
	if(index == 3){
		return "判断题";
	}
	//填空题
	if(index == 4){
		return "填空题";
	}
}

export function indexToString(index) {
	var charcode = "";
	return index.toString(26).split("").map(function(item,i){
		charcode = item.charCodeAt(0);
		charcode+=(charcode>=97?10:49);
		return String.fromCharCode(charcode)
	}).join("").toUpperCase();
}