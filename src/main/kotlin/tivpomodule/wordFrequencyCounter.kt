package tivpomodule

fun wordFrequencyCounter(
    wordInterpretation : List<String>
) : Map<String,Int>{
    val result = mutableMapOf<String, Int>()
    for(i in wordInterpretation){
        if(i in result){
            result[i] = result[i]!! + 1
        }else{
            result[i] = 1
        }
    }
    return result
}