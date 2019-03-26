/**
 * 숫자로 변환 가능한지 체크하는 함수
 * "3" -> true
 * "3e" -> false
 * "5.2" -> true
 * @param n
 * @returns {boolean}
 */
function isNumeric(n) {
    return !isNaN(parseFloat(n)) && isFinite(n);
}


/**
 * 천 단위 콤마 찍기
 * 소수점 제외
 *
 * "102665" -> "102,665"
 * "111102665.3453535" -> "111,102,665.3453535"
 *
 * @param numString
 * @returns {string}
 */
function formatCommaNumber(numString) {
    let integerPart='';
    let decimalPart='';

    //소수점 있으면 분리하기
    if(numString.includes(".")){
        numberPart = numString.split(".");
        integerPart = numberPart[0];
        decimalPart = ".".concat(numberPart[1]);
    } else {
        integerPart = numString;
    }

    //정수 부분 천단위 콤마 처리하기
    let commaIntegerPart = integerPart.replace(/(\d)(?=(\d{3})+(?!\d))/g, '$1,')

    return commaIntegerPart.concat(decimalPart);
}