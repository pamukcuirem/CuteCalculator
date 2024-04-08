package com.irempamukcu.cutecalculator

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import com.irempamukcu.cutecalculator.databinding.ActivityMainBinding
import java.lang.Exception
import java.lang.NumberFormatException



class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("currentTheme", MODE_PRIVATE)
        var currentTheme = sharedPreferences.getInt("Key", R.drawable.img)
        binding.root.setBackgroundResource(currentTheme)

        //declaring buttons
        val buttons = listOf<Button>(
            binding.zero,
            binding.one,
            binding.two,
            binding.three,
            binding.four,
            binding.five,
            binding.six,
            binding.seven,
            binding.eight,
            binding.nine,
            binding.plus,
            binding.minus,
            binding.divide,
            binding.multiply,
            binding.point,
            binding.equal,
            binding.clear
        )

        //process of all buttons
        for(button in buttons){
          button.setOnClickListener {
              val buttonText = button.text.toString()
              handleButtonClick(buttonText)
          }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.themes,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        sharedPreferences = getSharedPreferences("currentTheme", MODE_PRIVATE)

        return when(item.itemId){
            R.id.theme_option1 -> {
                binding.root.setBackgroundResource(R.drawable.img)
                sharedPreferences.edit().putInt("Key",R.drawable.img).apply()
                true
            }
            R.id.theme_option2 -> {
                binding.root.setBackgroundResource(R.drawable.img2)
                sharedPreferences.edit().putInt("Key",R.drawable.img2).apply()
                true
            }
            R.id.theme_option3 -> {
                binding.root.setBackgroundResource(R.drawable.img3)
                sharedPreferences.edit().putInt("Key",R.drawable.img3).apply()
                true
            }
            R.id.theme_option4 -> {
                binding.root.setBackgroundResource(R.drawable.img4)
                sharedPreferences.edit().putInt("Key",R.drawable.img4).apply()
                true
            }
            R.id.theme_option5 -> {
                binding.root.setBackgroundResource(R.drawable.img5)
                sharedPreferences.edit().putInt("Key",R.drawable.img5).apply()
                true
            }
            else -> false
        }
    }


    private fun handleButtonClick(buttonText : String){
        val currentText = binding.tvResult.text.toString()

        when(buttonText){
            "=" -> evaluateExpression(currentText)
            "C" -> binding.tvResult.text = ""
            else -> binding.tvResult.text = currentText + buttonText
        }

    }

    private fun evaluateExpression(expression : String){
        try{
            val result = eval(expression)
            binding.tvResult.text = result.toString()

        }catch (e : Exception){
            binding.tvResult.text = "Error"
        }
    }

    private fun eval(expression: String) : Double{
        try{
            val tokens = tokenize(expression)
            return evaluate(tokens)
        }catch (e : Exception){
            Log.e("Calculator","Error evaluating expression", e)
            return Double.NaN
        }
    }

    private fun tokenize(expression: String) : List<String>{
        val tokens = mutableListOf<String>()
        var numBuffer = StringBuilder()
        for(char in expression){
            if(char.isDigit() || char == '.'){
                numBuffer.append(char)
            }else{
                if(numBuffer.isNotEmpty()){
                    tokens.add(numBuffer.toString())
                    numBuffer = StringBuilder()
                }
                if(char != ' '){
                 tokens.add(char.toString())
                }
            }
        }
        if(numBuffer.isNotEmpty()){
            tokens.add(numBuffer.toString())
        }

        return tokens

    }


    private fun evaluate(tokens: List<String>): Double{
        val numbers = mutableListOf<Double>()
        val operators = mutableListOf<String>()

        for(token in tokens){
            if(token.isNumeric()){
                numbers.add(token.toDouble())
            }else{
                operators.add(token.toString())
            }
        }
        var result = numbers[0]
        for(i in 1 until numbers.size){
            result = when(operators[i-1]){
                "+" -> result + numbers[i]
                "-" -> result - numbers[i]
                "/" -> result / numbers[i]
                "*" -> result * numbers[i]
                else -> throw IllegalArgumentException("Invalid operator")
            }
        }

        return result
    }

    private fun String.isNumeric(): Boolean{
        return try{
            this.toDouble()
            true
        }catch (e: NumberFormatException){
            false
        }
    }
}

