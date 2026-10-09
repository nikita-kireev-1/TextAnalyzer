package tivpomodule

fun symbolFrequencyCounter(
    text : String
) : Map<Char,Int> {
    val result = mutableMapOf<Char, Int>()
    for(i in text){
        if(i in result){
            result[i] = result[i]!! + 1
        }else{
            result[i] = 1
        }
    }
    return result
}