package com.example.personalityquiz

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var chronometer: Chronometer
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var radioGroup: RadioGroup
    private lateinit var checkBoxQuiet: CheckBox
    private lateinit var checkBoxParty: CheckBox
    private lateinit var seekBar: SeekBar
    private lateinit var spinner: Spinner
    private lateinit var buttonDate: Button
    private lateinit var buttonEnd: Button

    private var chosenDate: String = ""
    private var chosenTime: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        chronometer = findViewById(R.id.chronometer)
        radioGroup = findViewById(R.id.radioGroup)
        checkBoxQuiet = findViewById(R.id.checkBoxQuiet)
        checkBoxParty = findViewById(R.id.checkBoxParty)
        seekBar = findViewById(R.id.seekBar)
        spinner = findViewById(R.id.spinner)
        buttonDate = findViewById(R.id.buttonDate)
        buttonEnd = findViewById(R.id.buttonEnd)

        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()

        val colors = arrayOf("Niebieski", "Czerwony", "Zielony", "Żółty", "Fioletowy")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, colors)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        countDownTimer = object : CountDownTimer(180000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                Toast.makeText(this@MainActivity, "Czas minął!", Toast.LENGTH_SHORT).show()
                buttonEnd.performClick()
            }
        }
        countDownTimer.start()

        buttonDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)




            val datePicker = DatePickerDialog(this, { _, y, m, d ->
                chosenDate = "$d/${m + 1}/$y"
                val timePicker = TimePickerDialog(this, { _, hour, minute ->
                    chosenTime = String.format("%02d:%02d", hour, minute)
                    Toast.makeText(this, "Wybrano: $chosenDate $chosenTime", Toast.LENGTH_SHORT).show()
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true)
                timePicker.show()
            }, year, month, day)
            datePicker.show()
        }

        buttonEnd.setOnClickListener {
            countDownTimer.cancel()
            chronometer.stop()

            val selectedRadioId = radioGroup.checkedRadioButtonId
            val radioAnswer = when (selectedRadioId) {
                R.id.radioHome -> "Dom"
                R.id.radioCrowd -> "Ludzie"
                else -> ""
            }

            val checkAnswer = mutableListOf<String>()
            if (checkBoxQuiet.isChecked) checkAnswer.add("Cisza")
            if (checkBoxParty.isChecked) checkAnswer.add("Imprezy")

            val seekValue = seekBar.progress

            val spinnerValue = spinner.selectedItem.toString()

            val score = seekValue + if (radioAnswer == "Ludzie") 30 else 0 + if (checkBoxParty.isChecked) 20 else 0
            val personalityType = when {
                score >= 50 -> "Ekstrawertyk"
                score >= 20 -> "Ambiwertyk"
                else -> "Introwertyk"
            }
            val elapsedMillis = SystemClock.elapsedRealtime() - chronometer.base
            val elapsedSeconds = (elapsedMillis / 1000) % 60
            val elapsedMinutes = (elapsedMillis / 1000) / 60

            val timeTaken = String.format("%02d:%02d", elapsedMinutes, elapsedSeconds)


            val resultText = """
                Typ osobowości: $personalityType
                Preferencje: $radioAnswer
                Lubi: ${checkAnswer.joinToString(", ")}
                Towarzyskość: $seekValue
                Ulubiony kolor: $spinnerValue
                Data i czas quizu: $chosenDate $chosenTime
                Czas ukończenia quizu: $timeTaken (mm:ss)
            """.trimIndent()

            val intent = Intent(this, SummaryActivity::class.java)
            intent.putExtra("resultText", resultText)
            intent.putExtra("personalityType", personalityType)
            startActivity(intent)
        }
    }
}
