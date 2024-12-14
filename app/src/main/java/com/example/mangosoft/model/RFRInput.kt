package com.example.mangosoft.model

import com.google.gson.annotations.SerializedName

data class RFRInput(
    @SerializedName("Year") val Year: Int,
    @SerializedName("Type_Carabao") val Type_Carabao: Int,
    @SerializedName("Type_Indian") val Type_Indian: Int,
    @SerializedName("Type_Pico") val Type_Pico: Int,
    @SerializedName("Class_Class A") val Class_ClassA: Int,
    @SerializedName("Class_Class B") val Class_ClassB: Int,
    @SerializedName("Class_Class C") val Class_ClassC: Int,
    @SerializedName("Class_Class D") val Class_ClassD: Int,
    @SerializedName("Class_Class E") val Class_ClassE: Int,
    @SerializedName("Month_January") val Month_January: Int,
    @SerializedName("Month_February") val Month_February: Int,
    @SerializedName("Month_March") val Month_March: Int,
    @SerializedName("Month_April") val Month_April: Int,
    @SerializedName("Month_May") val Month_May: Int,
    @SerializedName("Month_June") val Month_June: Int,
    @SerializedName("Month_July") val Month_July: Int,
    @SerializedName("Month_August") val Month_August: Int,
    @SerializedName("Month_September") val Month_September: Int,
    @SerializedName("Month_October") val Month_October: Int,
    @SerializedName("Month_November") val Month_November: Int,
    @SerializedName("Month_December") val Month_December: Int
)



//data class RFRInput(
//    val Year: Int,
//    val Type_Carabao: Int,
//    val Type_Indian: Int,
//    val Type_Pico: Int,
//    val Class_ClassA: Int,
//    val Class_ClassB: Int,
//    val Class_ClassC: Int,
//    val Class_ClassD: Int,
//    val Class_ClassE: Int,
//    val Month_January: Int,
//    val Month_February: Int,
//    val Month_March: Int,
//    val Month_April: Int,
//    val Month_May: Int,
//    val Month_June: Int,
//    val Month_July: Int,
//    val Month_August: Int,
//    val Month_September: Int,
//    val Month_October: Int,
//    val Month_November: Int,
//    val Month_December: Int
//)