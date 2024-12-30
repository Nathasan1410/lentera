package com.example.lentera.homepage

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.lentera.R
import ir.kotlin.kavehcolorpicker.KavehColorPicker
import ir.kotlin.kavehcolorpicker.KavehHueSlider

class CreateFragment : Fragment() {

    private val rows = 10
    private val columns = 10
    private lateinit var ledGrid: Array<Array<LedConfiguration>>
    private lateinit var colorPicker: KavehColorPicker
    private lateinit var hueSlider: KavehHueSlider
    private lateinit var outputTextView: TextView
    private var isInputModeActive = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_create, container, false)

        initLedGrid(rootView)
        setupColorPicker(rootView)
        setupHueSlider(rootView)
        setupApplyColorButton(rootView)
        setupResetButton(rootView)
        setupReadGridButton(rootView)
        setupChangeInputModeButton(rootView)

        return rootView
    }

    private fun initLedGrid(rootView: View) {
        val gridLayout = rootView.findViewById<androidx.gridlayout.widget.GridLayout>(R.id.ledGrid)
        gridLayout.rowCount = rows
        gridLayout.columnCount = columns

        ledGrid = Array(rows) { row ->
            Array(columns) { col ->
                LedConfiguration(
                    id = row * columns + col,
                    color = "#000000"  // Default color black
                )
            }
        }

        for (row in 0 until rows) {
            for (col in 0 until columns) {
                val button = Button(requireContext()).apply {
                    id = View.generateViewId()
                    text = "$row,$col"
                    setBackgroundColor(Color.BLACK)
                    setTextColor(Color.WHITE)

                    val layoutParams = GridLayout.LayoutParams().apply {
                        width = resources.getDimensionPixelSize(R.dimen.button_size)
                        height = resources.getDimensionPixelSize(R.dimen.button_size)
                        setMargins(
                            resources.getDimensionPixelSize(R.dimen.margin),
                            resources.getDimensionPixelSize(R.dimen.margin),
                            resources.getDimensionPixelSize(R.dimen.margin),
                            resources.getDimensionPixelSize(R.dimen.margin)
                        )
                    }
                    this.layoutParams = layoutParams

                    setOnClickListener {
                        if (isInputModeActive) {
                            // Toggle input mode: IN -> OUT -> Coordinate
                            when (text) {
                                "IN" -> text = "OUT"
                                "OUT" -> text = "$row,$col"
                                else -> text = "IN"
                            }
                        } else {
                            // Update color of the button with selected color from the color picker
                            val selectedColor = colorPicker.color
                            val hexColor = String.format("#%06X", 0xFFFFFF and selectedColor)
                            setBackgroundColor(selectedColor)
                            ledGrid[row][col].color = hexColor
                        }
                    }
                }
                gridLayout.addView(button)
            }
        }
    }

    private fun setupColorPicker(rootView: View) {
        colorPicker = rootView.findViewById(R.id.colorPickerView)
    }

    private fun setupHueSlider(rootView: View) {
        colorPicker = rootView.findViewById(R.id.colorPickerView)
        hueSlider = rootView.findViewById(R.id.hueSlider)

        // Hubungkan slider ke color picker
        colorPicker.hueSliderView = hueSlider
    }



    private fun setupApplyColorButton(rootView: View) {
        val applyButton = rootView.findViewById<Button>(R.id.btnApplyColor)
        applyButton.setOnClickListener {
            // Do nothing
            Toast.makeText(requireContext(), "Apply Color button clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupResetButton(rootView: View) {
        val resetButton = rootView.findViewById<Button>(R.id.btnResetGrid)
        resetButton.setOnClickListener {
            val gridLayout = rootView.findViewById<androidx.gridlayout.widget.GridLayout>(R.id.ledGrid)
            for (row in 0 until rows) {
                for (col in 0 until columns) {
                    ledGrid[row][col].color = "#000000" // Reset to black color
                    val button = gridLayout.getChildAt(row * columns + col) as Button
                    button.setBackgroundColor(Color.BLACK) // Reset button color to black
                    button.text = "$row,$col" // Reset button text to default
                }
            }
            Toast.makeText(requireContext(), "Grid Reset", Toast.LENGTH_SHORT).show()
        }
    }


    private fun setupReadGridButton(rootView: View) {
        val readGridButton = rootView.findViewById<Button>(R.id.btnReadGrid)
        outputTextView = rootView.findViewById(R.id.tvOutput)
        readGridButton.setOnClickListener {
            val colors = mutableSetOf<String>()
            for (row in 0 until rows) {
                for (col in 0 until columns) {
                    colors.add(ledGrid[row][col].color)
                }
            }
            outputTextView.text = "Processed Colors: ${colors.joinToString(", ")}"
            Toast.makeText(requireContext(), "Grid colors read", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupChangeInputModeButton(rootView: View) {
        val changeInputModeButton = rootView.findViewById<Button>(R.id.btnChangeInputMode)
        changeInputModeButton.setOnClickListener {
            isInputModeActive = !isInputModeActive
            val mode = if (isInputModeActive) "Input Mode Active" else "Color Mode Active"
            Toast.makeText(requireContext(), mode, Toast.LENGTH_SHORT).show()
        }
    }
}

data class LedConfiguration(
    val id: Int,
    var color: String
)
