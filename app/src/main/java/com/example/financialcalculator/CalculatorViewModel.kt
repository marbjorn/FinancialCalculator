package com.example.financialcalculator

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import java.math.BigDecimal
import kotlin.math.abs

enum class Operation() {
    ADD,
    SUBTRACT
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
        }

        return result.toPlainString()
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

fun String.toFormat() : String {
    val str : String = if (this.length >= 2 && this[0] == '0' && this[1] != '.') this[0] + "." + this.substring(1, this.lastIndex)
    else if (this.isNotEmpty() && this[0] == '.') "0$this"
    else this
    return str
        .filterNot { it.isWhitespace() }
}