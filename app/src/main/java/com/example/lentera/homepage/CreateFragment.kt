package com.example.lentera.homepage

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.gridlayout.widget.GridLayout
import com.example.lentera.R

class CreateFragment : Fragment() {

    private val rows = 8
    private val columns = 8
    private lateinit var ledGrid: Array<Array<LedInput>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_create, container, false)

        // Initialize the grid and LED inputs
        initLedGrid(rootView)

        return rootView
    }

    private fun initLedGrid(rootView: View) {
        val buttonSizeInDp = 48
        val marginInDp = 4
        val sizeInPixels = (buttonSizeInDp * resources.displayMetrics.density).toInt()
        val marginInPixels = (marginInDp * resources.displayMetrics.density).toInt()

        // Step 1: Initialize the 8x8 grid of LedInput objects
        ledGrid = Array(rows) { row ->
            Array(columns) { col ->
                LedInput(
                    id = row * columns + col, // Unique ID for each LED
                    mode = 5,                // Default mode: kosong
                    color = "#000000"        // Default color: black
                )
            }
        }

        // Step 2: Dynamically add buttons to the GridLayout
        val gridLayout = rootView.findViewById<GridLayout>(R.id.ledGrid)
        gridLayout.rowCount = rows
        gridLayout.columnCount = columns

        for (row in 0 until rows) {
            for (col in 0 until columns) {
                val button = Button(requireContext()).apply {
                    id = View.generateViewId()
                    text = "$row,$col"
                    setBackgroundColor(Color.BLACK)
                    setTextColor(Color.WHITE)

                    // Set GridLayout-specific layout params
                    val layoutParams = GridLayout.LayoutParams().apply {
                        width = sizeInPixels
                        height = sizeInPixels
                        setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels)
                        rowSpec = GridLayout.spec(row)
                        columnSpec = GridLayout.spec(col)
                    }
                    this.layoutParams = layoutParams

                    setOnClickListener {
                        onButtonPress(row, col, this)
                    }
                }
                gridLayout.addView(button)
            }
        }
    }

    // Step 3: Handle button press
    private fun onButtonPress(row: Int, col: Int, button: Button) {
        val led = ledGrid[row][col]

        // Update the LED object (simulating new values)
        led.mode = 2 // Set mode to LED (just an example)
        led.color = "#FF0000" // Set color to red (example)

        // Update the button's background color
        button.setBackgroundColor(Color.parseColor(led.color))

        // Provide feedback
        Toast.makeText(
            requireContext(),
            "Updated LED at ($row, $col): Mode=${led.mode}, Color=${led.color}",
            Toast.LENGTH_SHORT
        ).show()
    }
}
