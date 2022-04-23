package com.put.ubi.rpncalculator

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import java.util.*
import kotlin.math.*

class Calculator(mainActivity: MainActivity) {
    // constants
    private val mainAct      = mainActivity
    private val stackSize    = 4
    private val screenFields = arrayOf<TextView>(
        mainAct.findViewById(R.id.stack1),
        mainAct.findViewById(R.id.stack2),
        mainAct.findViewById(R.id.stack3),
        mainAct.findViewById(R.id.stack4)
    )
    val REQUEST_CODE = 10000

    // variables
    private var stack                = Stack<Double>()
    private var history              = Stack<Stack<Double>>()
    private var numberPrecision      = 2
    private var inputField: TextView = mainAct.findViewById(R.id.inputField)

    // methods
    private fun messageToast(msg: String) {
        Toast.makeText(mainAct, msg, Toast.LENGTH_SHORT).show()
    }

    // first row methods
    fun undo() {
        if (history.isNotEmpty()) {
            stack = history.pop()
            updateStack()
        } else {
            messageToast("History is empty")
        }
    }

    fun delete() {
        if (inputField.text.isNotEmpty()) {
            inputField.text = inputField.text.dropLast(1)
        } else {
            messageToast("Input field is empty")
        }
    }

    fun clear() {
        if (stack.isNotEmpty()) {
            updateHistory()
            stack.clear()
            updateStack()
        } else {
            messageToast("Stack is already empty")
        }
    }

    // first / second / third / fourth row methods
    fun oneArgumentAction(action: Char) {
        if (stack.isNotEmpty()) {
            updateHistory()
            val first: Double = stack.pop()

            if (action == 's') {
                if (first >= 0) {
                    stack.push(sqrt(first))
                } else {
                    undo()
                    messageToast("Cannot sqrt negative number")
                }
            }
            updateStack()
        } else {
            messageToast("Stack is empty")
        }
    }

    fun twoArgumentAction(action: Char) {
        if (stack.size > 1) {
            updateHistory()
            val first:  Double = stack.pop()
            val second: Double = stack.pop()

            when (action) {
                // first row actions
                's' -> {
                    stack.push(first)
                    stack.push(second)
                }

                // second row actions
                '^' -> stack.push(second.pow(first))

                // third row actions
                '*' -> stack.push(second * first)

                '/' -> {
                    if (first != 0.0) {
                        stack.push(second / first)
                    } else {
                        undo()
                        messageToast("Cannot divide by zero")
                    }
                }

                // fourth row actions
                '+' -> stack.push(second + first)

                '-' -> stack.push(second - first)
            }
            updateStack()
        } else {
            messageToast("Stack size is less than 2")
        }
    }

    // fifth row methods

    // --- settings stuff start ---
    fun openSettings() {
        mainAct.startActivityForResult(Intent(mainAct, Settings::class.java), REQUEST_CODE)
    }

    private fun changeNumberPrecision(newPrecision: String) {
        numberPrecision = newPrecision.toInt()
    }

    private fun changeColor(color: String): Int {
        return when (color) {
            "Dim Gray"        -> ContextCompat.getColor(mainAct, R.color.dim_gray)
            "Dark Slate Gray" -> ContextCompat.getColor(mainAct, R.color.dark_slate_gray)
            "Purple"          -> ContextCompat.getColor(mainAct, R.color.purple)
            "Android Green"   -> ContextCompat.getColor(mainAct, R.color.android_green)
            "Brown Sugar"     -> ContextCompat.getColor(mainAct, R.color.brown_sugar)
            "Madder Lake"     -> ContextCompat.getColor(mainAct, R.color.madder_lake)
            "Light Salmon"    -> ContextCompat.getColor(mainAct, R.color.light_salmon)
            else              -> Color.GRAY
        }
    }

    fun saveSettings(options: ArrayList<String>) {
        // init
        val normalButtons = arrayOf<Button>(
            // second row
            mainAct.findViewById(R.id.num7),
            mainAct.findViewById(R.id.num8),
            mainAct.findViewById(R.id.num9),
            mainAct.findViewById(R.id.power),
            mainAct.findViewById(R.id.sqrt),

            // third row
            mainAct.findViewById(R.id.num4),
            mainAct.findViewById(R.id.num5),
            mainAct.findViewById(R.id.num6),
            mainAct.findViewById(R.id.multiplication),
            mainAct.findViewById(R.id.division),

            // fourth row
            mainAct.findViewById(R.id.num1),
            mainAct.findViewById(R.id.num2),
            mainAct.findViewById(R.id.num3),
            mainAct.findViewById(R.id.plus),
            mainAct.findViewById(R.id.minus),

            // fifth row
            mainAct.findViewById(R.id.num0),
            mainAct.findViewById(R.id.dot),
            mainAct.findViewById(R.id.changeSign)
        )
        val specialButtons = arrayOf<Button>(
            // first row
            mainAct.findViewById(R.id.undo),
            mainAct.findViewById(R.id.drop),
            mainAct.findViewById(R.id.swap),
            mainAct.findViewById(R.id.del),
            mainAct.findViewById(R.id.clear),

            // fifth row
            mainAct.findViewById(R.id.settings),
            mainAct.findViewById(R.id.enter)
        )

        // update
        changeNumberPrecision(options[0])
        mainAct.findViewById<ConstraintLayout>(R.id.screen).setBackgroundColor(changeColor(options[1]))
        for (button in normalButtons) {
            button.setBackgroundColor(changeColor(options[2]))
        }
        for (button in specialButtons) {
            button.setBackgroundColor(changeColor(options[3]))
        }
        updateStack()
    }
    // --- settings stuff end ---

    fun changeSign() {
        val field = inputField.text
        if (field.isNotEmpty()) {
            if (field[0] == '-') {
                inputField.text = field.drop(1)
            } else {
                inputField.text = "-".plus(field)
            }
        } else if (stack.isNotEmpty()) {
            updateHistory()
            stack[stack.size-1] = stack[stack.size-1] * -1
            updateStack()
        } else {
            messageToast("Input field is empty")
        }
    }

    fun addToStack() {
        updateHistory()
        val field = inputField.text
        when {
            field.isNotEmpty() -> {
                try {
                    stack.push(inputField.text.toString().toDouble())
                } catch (e: Exception) {
                    undo()
                    messageToast("Cannot convert given String to Double")
                } finally {
                    inputField.text = ""
                }
            }
            stack.isNotEmpty() -> {
                stack.push(stack.lastElement())
            } else -> {
                undo()
                messageToast("Input field is empty")
            }
        }
        updateStack()
    }

    // inputField method
    fun addToString(s: String) {
        if (inputField.text.length < 14) {
            inputField.text = inputField.text.toString().plus(s)
        } else {
            messageToast("Too large number in input field")
        }
    }

    // stack methods
    @SuppressLint("SetTextI18n")
    fun updateStack() {
        for (i in 0 until stackSize) {
            screenFields[i].text = (i+1).toString().plus(": ")
        }

        if (stack.isNotEmpty()) {
            val precision: Double = 10.0.pow(numberPrecision)

            for (i in 0 until min(stack.size, stackSize)) {
                val number: Double = round(stack[stack.size-i-1]*precision)/precision
                stack[stack.size-i-1] = number

                var displayNumber: String = number.toString()
                try {
                    val index = displayNumber.indexOf('.')
                    displayNumber = displayNumber.padEnd(index+numberPrecision+1, '0')
                } catch (e: Exception) {
                    messageToast("Cannot set proper number precision")
                }
                screenFields[i].text = screenFields[i].text.toString().plus(displayNumber)
            }
        }
    }

    private fun updateHistory() {
        history.push(stack.clone() as Stack<Double>?)
    }
}
