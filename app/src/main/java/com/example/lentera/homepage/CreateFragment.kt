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
import android.widget.EditText
import android.widget.TextView

class CreateFragment : Fragment() {

    private val rows = 10
    private val columns = 10
    private lateinit var ledGrid: Array<Array<LedInput>>
    private var currentMode = 0
    private var savedColor: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_create, container, false)

        initLedGrid(rootView)
        setupColorInput(rootView)
        setupChangeInputModeButton(rootView)
        setupReadGridButton(rootView) // Set up the read grid button
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
            if (isValidHexColor(colorCode)) {
                savedColor = colorCode
                Toast.makeText(requireContext(), "Color saved: $savedColor", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Invalid color code", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Step 3: Handle button press
    private fun onButtonPress(row: Int, col: Int, button: Button) {
        val led = ledGrid[row][col]

        // Check the current mode of the button
        if (led.mode == 2) {
            // If mode is 2, update the color only if a saved color exists
            if (savedColor != null) {
                led.color = savedColor!!
                button.setBackgroundColor(Color.parseColor(led.color))
                Toast.makeText(
                    requireContext(),
                    "LED color updated to ${led.color} at ($row, $col)",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "No color saved. Please input and save a color first.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            // Change the mode and update the button text
            led.mode = currentMode
            button.text = when (led.mode) {
                0 -> "IN"
                1 -> "OUT"
                else -> "$row,$col" // Default text if mode is not 0 or 1
            }

            Toast.makeText(
                requireContext(),
                "LED mode updated to ${led.mode} at ($row, $col)",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
    private fun processLedInput(): String {
        val colors = mutableListOf<String>()

        // Check for horizontal reading (left-to-right or right-to-left)
        for (row in 0 until rows) {
            val rowStartIndex = ledGrid[row].indexOfFirst { it.mode == 0 } // Find "IN"
            val rowEndIndex = ledGrid[row].indexOfLast { it.mode == 1 } // Find "OUT"

            if (rowStartIndex != -1 && rowEndIndex != -1 && rowStartIndex < rowEndIndex) {
                // Left-to-right
                println("Detected horizontal direction: IN at Row $row from $rowStartIndex to $rowEndIndex")
                val rowColors = ledGrid[row]
                    .slice(rowStartIndex..rowEndIndex) // Extract the range
                    .filter { it.mode == 2 } // Include only LEDs with mode == 2
                    .map { it.color }
                colors.addAll(rowColors)
            } else if (rowStartIndex != -1 && rowEndIndex != -1 && rowStartIndex > rowEndIndex) {
                // Right-to-left
                println("Detected horizontal direction: OUT at Row $row from $rowEndIndex to $rowStartIndex")
                val rowColors = ledGrid[row]
                    .slice(rowEndIndex..rowStartIndex) // Extract the range
                    .reversed() // Reverse for OUT
                    .filter { it.mode == 2 } // Include only LEDs with mode == 2
                    .map { it.color }
                colors.addAll(rowColors)
            }
        }

        // If no valid horizontal direction is found
        if (colors.isEmpty()) {
            println("No valid horizontal direction detected.")
            val outputTextView = requireView().findViewById<TextView>(R.id.tvOutput)
            outputTextView.text = "No valid horizontal direction found."
            return "No valid horizontal direction found."
        }

        // Format the colors as a readable string
        val formattedColors = colors.joinToString(" ")

        // Update the TextView with the filtered output
        val outputTextView = requireView().findViewById<TextView>(R.id.tvOutput)
        outputTextView.text = "Processed Colors: $formattedColors"

        return formattedColors
    }




    private fun setupReadGridButton(rootView: View) {
        val readGridButton = rootView.findViewById<Button>(R.id.btnReadGrid)
        readGridButton.setOnClickListener {
            processLedInput()
        }
    }


}

