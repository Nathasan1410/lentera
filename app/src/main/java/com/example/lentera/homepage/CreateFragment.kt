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
import android.util.Patterns
import android.widget.EditText

class CreateFragment : Fragment() {

    private val rows = 10
    private val columns = 10
    private lateinit var ledGrid: Array<Array<LedInput>>
    private var currentMode = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_create, container, false)

        // Initialize the grid and LED inputs
        initLedGrid(rootView)

        setupColorInput(rootView)

        // Set up the button to change input mode
        setupChangeInputModeButton(rootView)

        return rootView
    }

    private fun initLedGrid(rootView: View) {
        val buttonSizeInDp = 48
        val marginInDp = 4
        val sizeInPixels = (buttonSizeInDp * resources.displayMetrics.density).toInt()
        val marginInPixels = (marginInDp * resources.displayMetrics.density).toInt()

        // Step 1: Initialize the 10x10 grid of LedInput objects
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

    private fun setupChangeInputModeButton(rootView: View) {
        val changeModeButton = rootView.findViewById<Button>(R.id.btnChangeInputMode)
        changeModeButton.setOnClickListener {
            currentMode = when (currentMode) {
                0 -> 1
                1 -> 2
                2 -> 5
                else -> 0
            }
            Toast.makeText(requireContext(), "Input mode changed to $currentMode", Toast.LENGTH_SHORT).show()
        }
    }

    // Step 3: Handle button press
    private fun onButtonPress(row: Int, col: Int, button: Button) {
        val led = ledGrid[row][col]

        // Update the LED object with the current mode and a sample color
        led.mode = currentMode
        led.color = when (currentMode) {
            0 -> "#000000" // Black for off
            1 -> "#00FF00" // Green for some mode
            2 -> "#FF0000" // Red for LED
            5 -> "#0000FF" // Blue for another mode
            else -> "#FFFFFF" // Default white
        }

        // Update the button's background color
        button.setBackgroundColor(Color.parseColor(led.color))

        // Provide feedback
        Toast.makeText(
            requireContext(),
            "Updated LED at ($row, $col): Mode=${led.mode}, Color=${led.color}",
            Toast.LENGTH_SHORT
        ).show()
    }
    // Function to update color with hex code
    fun updateLedColor(row: Int, col: Int, hexColor: String) {
        if (!isValidHexColor(hexColor)) {
            Toast.makeText(requireContext(), "Invalid color code", Toast.LENGTH_SHORT).show()
            return
        }

        // Update the LED object and button color
        val led = ledGrid[row][col]
        led.color = hexColor
        led.mode = 2 // Assuming mode 2 is for LED on with custom color

        // Find the button and update its background color
        val gridLayout = requireView().findViewById<GridLayout>(R.id.ledGrid)
        val button = gridLayout.getChildAt(row * columns + col) as Button
        button.setBackgroundColor(Color.parseColor(hexColor))

        // Provide feedback
        Toast.makeText(
            requireContext(),
            "Updated LED at ($row, $col) with color $hexColor",
            Toast.LENGTH_SHORT
        ).show()
    }

    // Helper function to validate hex color codes
    private fun isValidHexColor(color: String): Boolean {
        return color.matches(Regex("^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{8})$"))
    }

    private fun setupColorInput(rootView: View) {
        val colorInput = rootView.findViewById<EditText>(R.id.colorInput)
        val applyColorButton = rootView.findViewById<Button>(R.id.btnApplyColor)

        applyColorButton.setOnClickListener {
            val colorCode = colorInput.text.toString().trim()
            val row = 0 // Replace with actual row selection logic
            val col = 0 // Replace with actual column selection logic

            // Call updateLedColor with user input
            updateLedColor(row, col, colorCode)
        }
    }
}

