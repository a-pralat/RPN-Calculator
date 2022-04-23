package com.put.ubi.rpncalculator

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.constraintlayout.widget.ConstraintLayout
import com.put.ubi.rpncalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding:    ActivityMainBinding
    private lateinit var calculator: Calculator

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        calculator = Calculator(this)

        // first row
        binding.undo.setOnClickListener  { calculator.undo() }
        binding.drop.setOnClickListener  { calculator.oneArgumentAction('d') }
        binding.swap.setOnClickListener  { calculator.twoArgumentAction('s') }
        binding.del.setOnClickListener   { calculator.delete() }
        binding.clear.setOnClickListener { calculator.clear() }

        // second row
        binding.num7.setOnClickListener  { calculator.addToString("7") }
        binding.num8.setOnClickListener  { calculator.addToString("8") }
        binding.num9.setOnClickListener  { calculator.addToString("9") }
        binding.power.setOnClickListener { calculator.twoArgumentAction('^') }
        binding.sqrt.setOnClickListener  { calculator.oneArgumentAction('s') }

        // third row
        binding.num4.setOnClickListener           { calculator.addToString("4") }
        binding.num5.setOnClickListener           { calculator.addToString("5") }
        binding.num6.setOnClickListener           { calculator.addToString("6") }
        binding.multiplication.setOnClickListener { calculator.twoArgumentAction('*') }
        binding.division.setOnClickListener       { calculator.twoArgumentAction('/') }

        // fourth row
        binding.num1.setOnClickListener  { calculator.addToString("1") }
        binding.num2.setOnClickListener  { calculator.addToString("2") }
        binding.num3.setOnClickListener  { calculator.addToString("3") }
        binding.plus.setOnClickListener  { calculator.twoArgumentAction('+') }
        binding.minus.setOnClickListener { calculator.twoArgumentAction('-') }

        // fifth row
        binding.settings.setOnClickListener   { calculator.openSettings() }
        binding.num0.setOnClickListener       { calculator.addToString("0") }
        binding.dot.setOnClickListener        { calculator.addToString(".") }
        binding.changeSign.setOnClickListener { calculator.changeSign() }
        binding.enter.setOnClickListener      { calculator.addToStack() }

        // handle onFling
        val screen: ConstraintLayout = findViewById(R.id.screen)
        screen.setOnTouchListener(object: OnSwipe(this@MainActivity) {
            override fun onSwipeRight() {
                calculator.undo()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == calculator.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                calculator.saveSettings(data.getStringArrayListExtra("options")!!)
            }
        }
    }
}