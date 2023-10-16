package com.example.financialcalculator

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import kotlin.math.abs

enum class Operation() {
    ADD,
    SUBTRACT,
    MULTIPLY,
    DIVIDE
}

class CalculatorViewModel : ViewModel() {
    val num1 = mutableStateOf("0")
    val num2 = mutableStateOf("0")
    val result = mutableStateOf("0")


    fun doOperation(operation : Operation, num1 : String , num2 : String) : String? {
        val number1 = num1.toNumber()
        val number2 = num2.toNumber()

        Log.d("NUM1", number1.toString())
        Log.d("NUM2", number2.toString())

        if (number1 == null || number2 == null || !validateNumRange(number1) || !validateNumRange(number2)) {
            return null;
        }
        val result = when(operation) {
            Operation.ADD -> number1 + number2
            Operation.SUBTRACT -> number1 - number2
            Operation.MULTIPLY -> number1 * number2
            Operation.DIVIDE -> {
                if (number2 != BigDecimal.ZERO) number1 / number2
                else return null
            }
        }

        return result.toFormatPlainString()
    }

    fun validateNumRange(num : BigDecimal) : Boolean {
        return (
                num.abs() <= BigDecimal(1000000000000.000000))
    }
}

fun String.toNumber() : BigDecimal? {
    return this
        .replace(',', '.')
        .filterNot { it == 'e' || it == 'E' || it.isWhitespace() }
        .toBigDecimalOrNull()
}

fun BigDecimal.toFormatPlainString(places : Int = 6): String? {
    val df = DecimalFormat("%.${places}f")
    this.setScale(places, RoundingMode.HALF_UP)
    val customSymbol = DecimalFormatSymbols()
    customSymbol.groupingSeparator = ' '
    df.decimalFormatSymbols = customSymbol
    return df.format(this)
}
fun String.toFormat() : String {
    val str : String = if (this.length >= 2 && this[0] == '0' && this[1] != '.') this[0] + "." + this.substring(1, this.lastIndex)
    else if (this.isNotEmpty() && this[0] == '.') "0$this"
    else this

    return str
        .filterNot { it.isWhitespace() }
}