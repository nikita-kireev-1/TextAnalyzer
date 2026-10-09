package tivpomodule

fun InterpretTextToWords(
    text : String
) : List<String>{
    val letters = ('a'..'z').toList() +
            ('A'..'Z').toList() +
            ('а'..'я').toList() + 'ё' +
            ('А'..'Я').toList() + 'Ё'
    var previous = ' '
    var previousIndex = -1
    val words = mutableListOf<String>()
    for(i in text.indices){
        if(previous !in letters && text[i] in letters){
            previousIndex = i
        }else if(previous in letters && text[i] !in letters){
            words.add(text.substring(previousIndex,i).lowercase())
        }
        previous = text[i]
    }
    if (previous in letters && previousIndex >= 0) {
        words.add(text.substring(previousIndex).lowercase())
    }
    return words.toList()
}