package model

data class TextHolder(
    val text : String,
    val wordCounter : Int?,
    val symbolCounter : Int?,
    val letterCounter : Int?,
    val wordFrequency : Map<String,Int>,
    val symbolFrequency : Map<Char,Int>,
    val wordInterpretation : List<String>
)
