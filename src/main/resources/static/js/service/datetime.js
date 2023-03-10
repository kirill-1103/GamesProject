export  function fromArrayToDate(array) {
    let month = array[1].toString().length === 1 ? '0' + array[1] : array[1]
    let day = array[2].toString().length === 1 ? '0' + array[2] : array[2]
    return day + "." + month + '.' + array[0]
}

export function fromArrayToHoursMinutesSeconds(array) {
    let hours = array[3] > 9 ? array[3] : '0' + array[3];
    let minutes = array[4] > 9 ? array[4] : '0' + array[4];
    let seconds = array[5] > 9 ? array[5] : '0' + array[5];
    return hours + ":" + minutes + ":" + seconds;
}

export function fromStringToHoursMinutesSeconds(string){
    console.log(string)
    return string.substring(string.indexOf('T')+1,string.indexOf('.'));
}

export function fromArrayToDateWithTime(array){
    return fromArrayToDate(array) +" / "+fromArrayToHoursMinutesSeconds(array)
}

export function reformatTime(time){
    if(time<0){
        return "∞";
    }
    let minutes = Math.floor(time/60/10);
    let seconds = Math.floor((time - minutes*60*10)/10)
    let min_str = minutes > 9 ?  String(minutes) : "0"+String(minutes);
    let sec_str = seconds > 9 ?  String(seconds) : "0"+String(seconds);
    return min_str + ":" + sec_str;
}