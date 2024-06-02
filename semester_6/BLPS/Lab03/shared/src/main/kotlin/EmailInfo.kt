import letters.Letter

class EmailInfo(
    var to: String,
    var letter: Letter,
) {
    constructor() : this("", Letter())
}