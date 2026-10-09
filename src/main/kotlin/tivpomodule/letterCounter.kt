package tivpomodule

fun letterCounter(
    text : String
) : Int {
    val letters = ('a'..'z').toList() +
            ('A'..'Z').toList() +
            ('а'..'я').toList() + 'ё' +
            ('А'..'Я').toList() + 'Ё'
    var counter = 0
    for(i in text){
        if(i in letters){
            counter++
        }
    }
    return counter
}