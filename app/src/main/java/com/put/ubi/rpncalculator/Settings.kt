package com.put.ubi.rpncalculator

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class Settings : AppCompatActivity() {
    private val options = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val default = arrayOf(2, 0, 1, 2)
        val spinners = arrayOf<Spinner>(
            this.findViewById(R.id.chooseNumberPrecision),
            this.findViewById(R.id.chooseScreenColor),
            this.findViewById(R.id.chooseNormalButtonColor),
            this.findViewById(R.id.chooseSpecialButtonColor)
        )
        for (i in spinners.indices) {
            spinners[i].setSelection(default[i])
        }

        val saveButton: Button = findViewById(R.id.save)

        saveButton.setOnClickListener {

            for (i in spinners.indices) {
                options.add(spinners[i].selectedItem.toString())
            }

            finish()
        }
    }

    override fun finish() {
        val data = Intent()
        data.putStringArrayListExtra("options", options)
        setResult(Activity.RESULT_OK, data)
        super.finish()
    }
}