package com.example.ghostpolice

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.setMargins
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
        binding.changePos.isEnabled = false
        row = intent.getIntExtra("row", 2)
        col = intent.getIntExtra("col", 2)
        setupGrid()
        placeGhost()
        placePolice()

        binding.changePos.setOnClickListener {
            it.isEnabled = false
            updateTextAtRowAndColumn(
                curPolicePosition.first,
                curPolicePosition.second,
                "",
                isGhost = false,
                isReset = true
            )
            updateTextAtRowAndColumn(
                curGhostPosition.first,
                curGhostPosition.second,
                "",
                isGhost = false,
                isReset = true
            )
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
                isGhost = false,
                isReset = false
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
                ghostPosition.second, "Ghost",
                isGhost = true, isReset = false
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
                textView.width = GridLayout.LayoutParams.MATCH_PARENT
                textView.height = GridLayout.LayoutParams.MATCH_PARENT
                textView.background = AppCompatResources.getDrawable(
                    this, R.drawable.grid_item_border
                )
                textView.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.black
                    )
                )

                val params = GridLayout.LayoutParams()
                params.rowSpec = GridLayout.spec(row, 1f)
                params.columnSpec = GridLayout.spec(col, 1f)
                params.setMargins(30)
                textView.layoutParams = params

                binding.gridLayout.addView(textView)
            }
        }
    }

    private fun updateTextAtRowAndColumn(
        row: Int, column: Int, newText: String,
        isGhost: Boolean,
        isReset: Boolean
    ) {
        if (isReset) {
            if (row == -1 || column == -1) return
        }
        val rowIndex = row - 1
        val cellIndex = rowIndex * binding.gridLayout.columnCount + column - 1

        if (cellIndex >= 0 && cellIndex < binding.gridLayout.childCount) {
            val childView: View = binding.gridLayout.getChildAt(cellIndex)

            if (childView is TextView) {
                if (!isReset)
                    if (isGhost) {
                        curGhostPosition = Pair(row, column)
                    } else {
                        curPolicePosition = Pair(row, column)
                    }

                childView.text = newText
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
