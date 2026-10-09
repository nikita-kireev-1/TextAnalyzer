package tivpomodule

fun wordCounter(
    text : String
) : Int {
    val letters = ('a'..'z').toList() +
            ('A'..'Z').toList() +
            ('а'..'я').toList() + 'ё' +
            ('А'..'Я').toList() + 'Ё'
    var previous = ' '
    var counter = 0
    for(i in text){
        if(previous !in letters && i in letters){
            counter++
        }
        previous = i
    }
    return counter
}