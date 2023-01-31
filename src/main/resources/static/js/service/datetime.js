export default function fromArrayToDate(array) {
    let month = array[1].toString().length === 1 ? '0' + array[1] : array[1]
    let day = array[2].toString().length === 1 ? '0' + array[2] : array[2]
    return day + "." + month + '.' + array[0]
}