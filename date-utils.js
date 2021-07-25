/**
* Date() 객체를 string 으로 변환 (년,월,일)
*
* @param date new Date(2021, 0, 1)
* @param delimiter
* @returns {string} 2021-01-01
*/
export function toStringByFormatting(date, delimiter = '-') {
  const year = date.getFullYear()
  const month = leftPad(date.getMonth() + 1)
  const day = leftPad(date.getDate())


  return [year, month, day].join(delimiter)
}

function leftPad(value) {
  if (value >= 10) {
    return value
  }
  return `0${value}`
}
