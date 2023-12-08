package com.example.ghostpolice

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ghostpolice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupUI()

    }

    private fun setupUI() {
        binding.apply {
            generateBtn.setOnClickListener {
                val row = rowInputEditText.text.toString().toInt()
                val col = colInputEditText.text.toString().toInt()
                if (row < 2 || col < 2) {
                    Toast.makeText(
                        this@MainActivity,
                        "enter values greater than or equal to 2",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Intent(this@MainActivity, GridActivity::class.java).also { intent ->
                        intent.apply {
                            putExtra("row", row)
                            putExtra("col", col)
                            startActivity(this)
                        }
                    }
                }
            }
        }
    }
}