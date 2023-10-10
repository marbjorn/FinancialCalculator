package com.example.financialcalculator


import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Preview
@Composable
fun Calculator(
    viewModel : CalculatorViewModel = viewModel()
) {

    val num1 = remember{ viewModel.num1 }
    val num2 = remember{ viewModel.num2 }
    val result = remember{ viewModel.result }

    Column(Modifier.fillMaxWidth().padding(20.dp)) {
        StudentForm()
        Spacer(modifier = Modifier.padding(20.dp))
        NumericField(viewModel, num1, label = {Text("Первое число")})
        Spacer(modifier = Modifier.padding(10.dp))
        NumericField(viewModel, num2, label = {Text("Второе число")})
        Spacer(modifier = Modifier.padding(10.dp))
        NumericField(viewModel, result, isBlocked = true, label = {Text("Результат")})

        Spacer(modifier = Modifier.padding(20.dp))
        Row(modifier = Modifier.fillMaxWidth(), ) {
            OperationButton(viewModel = viewModel,
                operation = Operation.ADD,
                operationStr = "+",
                num1 = num1,
                num2 = num2,
                result = result,
                modifier = Modifier.fillMaxWidth().weight(0.5f))
            Spacer(modifier = Modifier.padding(10.dp))
            OperationButton(viewModel = viewModel,
                operation = Operation.SUBTRACT,
                operationStr = "-",
                num1 = num1,
                num2 = num2,
                result = result,
                modifier = Modifier.fillMaxWidth().weight(0.5f))
        }

        Spacer(modifier = Modifier.padding(20.dp))
    }

}


@Preview
@Composable
fun OperationButton(
    viewModel: CalculatorViewModel = viewModel(),
    operation: Operation = Operation.ADD,
    num1 : MutableState<String> = mutableStateOf(""),
    num2 : MutableState<String> = mutableStateOf(""),
    result : MutableState<String> = mutableStateOf(""),
    operationStr : String = "+",
    modifier: Modifier = Modifier.fillMaxWidth()
) {

    Button(
        onClick = {
            Log.d("CLICK", num1.value)
            val res =  viewModel.doOperation(operation, num1.value, num2.value)
            if (res != null) result.value = res

        },
        content = { Text(operationStr, fontSize = 25.sp) },
        modifier = modifier)
}

@Preview
@Composable
fun NumericField(
    viewModel: CalculatorViewModel = viewModel(),
    str : MutableState<String> = mutableStateOf(""),
    isBlocked : Boolean = false,
    label : @Composable() (() -> Unit) = { Text("Введите число") }
) {
    val focusManager = LocalFocusManager.current

    Column {
        TextField(
            value = str.value,
            modifier = Modifier.fillMaxWidth(),
            label = label,
            enabled = !isBlocked,
            onValueChange = {
                if (!isBlocked) {
                    str.value = it
                }
            },
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = true,
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            )
        )
        val number = str.value.toNumber()
        if (number == null) {
            Text("Неверный ввод", modifier = Modifier.padding(6.dp), color = Color.Red)
        }
        else if (!viewModel.validateNumRange(number)) {
            Text("Переполнение", modifier = Modifier.padding(6.dp), color = Color.Red)
        }
    }

}
//ФИО студента, курс, группа, год
@Preview
@Composable
fun StudentForm(
    name : String = "Антонченко Евгений Эдуардович",
    course : String = "3 курс",
    group : String = "12 группа",
    year : String = "2023"
) {
    Box(modifier = Modifier.fillMaxWidth()
        .shadow(10.dp)
        .background(
            Color.White,
            shape = RoundedCornerShape(10.dp)
            )
        ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(name)
            Text(course)
            Text(group)
            Text(year)
        }
    }
}