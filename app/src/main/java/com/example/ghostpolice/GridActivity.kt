package com.example.ghostpolice

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ghostpolice.databinding.ActivityGridBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GridActivity : AppCompatActivity() {
    private val binding by lazy { ActivityGridBinding.inflate(layoutInflater) }
    private var row = 0
    private var col = 0
    private var ghostPosition: Pair<Int, Int> = Pair(-1, -1)
    private var curGhostPosition: Pair<Int, Int> = Pair(-1, -1)
    private var policePosition: Pair<Int, Int> = Pair(-1, -1)
    private var curPolicePosition: Pair<Int, Int> = Pair(-1, -1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        row = intent.getIntExtra("row", 2)
        col = intent.getIntExtra("col", 2)
        setupGrid()
        placeGhost()
        placePolice()

        binding.changePos.setOnClickListener {
            it.isEnabled = false
            clearPosition(curPolicePosition.first, curPolicePosition.second)
            clearPosition(curGhostPosition.first, curGhostPosition.second)
            placeGhost()
            placePolice()
        }
    }

    private fun placePolice() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            var newPolicePosition: Pair<Int, Int>

            while (true) {
                newPolicePosition = generateRandomCoordinate()
                if (newPolicePosition != ghostPosition &&
                    newPolicePosition.first != ghostPosition.first &&
                    newPolicePosition.second != ghostPosition.second &&
                    newPolicePosition != policePosition
                ) {
                    break
                }
            }
            policePosition = newPolicePosition

            updateTextAtRowAndColumn(
                newPolicePosition.first,
                newPolicePosition.second,
                "Police",
                false
            )
            binding.changePos.isEnabled = true
        }

    }

    private fun placeGhost() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(1000)
            var newGhostPosition: Pair<Int, Int>

            while (true) {
                newGhostPosition = generateRandomCoordinate()
                if (newGhostPosition != ghostPosition) {
                    break
                }
            }
            ghostPosition = newGhostPosition

            updateTextAtRowAndColumn(
                ghostPosition.first,
                ghostPosition.second, "Ghost", true
            )
        }
    }

    private fun setupGrid() {
        for (row in 0 until row) {
            for (col in 0 until col) {

                val textView = TextView(this)
                textView.text = ""
                textView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                textView.gravity = Gravity.CENTER

                val params = GridLayout.LayoutParams()
                params.rowSpec = GridLayout.spec(row, 1f)
                params.columnSpec = GridLayout.spec(col, 1f)
                params.setGravity(Gravity.FILL)
                textView.layoutParams = params

                binding.gridLayout.addView(textView)
            }
        }
    }

    private fun updateTextAtRowAndColumn(row: Int, column: Int, newText: String, isGhost: Boolean) {
        val rowIndex = row - 1
        val cellIndex = rowIndex * binding.gridLayout.columnCount + column - 1

        if (cellIndex >= 0 && cellIndex < binding.gridLayout.childCount) {
            val childView: View = binding.gridLayout.getChildAt(cellIndex)

            if (childView is TextView) {
                if (isGhost) {
                    curGhostPosition = Pair(row, column)
                } else {
                    curPolicePosition = Pair(row, column)
                }

                childView.text = newText
            }
        }
    }

    private fun clearPosition(row: Int, column: Int) {
        if (row == -1 || column == -1) return

        val rowIndex = row - 1
        val cellIndex = rowIndex * binding.gridLayout.columnCount + column - 1

        if (cellIndex >= 0 && cellIndex < binding.gridLayout.childCount) {
            val childView: View = binding.gridLayout.getChildAt(cellIndex)

            if (childView is TextView) {
                childView.text = ""
            }
        }
    }

    private fun generateRandomCoordinate(): Pair<Int, Int> {
        val numRows = row
        val numColumns = col

        val randomRow = (1..numRows).random()
        val randomColumn = (1..numColumns).random()

        return Pair(randomRow, randomColumn)
    }
}
