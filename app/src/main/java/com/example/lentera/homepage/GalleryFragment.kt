package com.example.lentera.homepage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lentera.R

class GalleryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GalleryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_gallery, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.gallery_recycler_view)
        recyclerView.layoutManager = GridLayoutManager(context, 3)

        // Initialize data and adapter
        adapter = GalleryAdapter(sampleItems)
        recyclerView.adapter = adapter

        return view
    }

    // Generate sample data (Replace with actual data)
    private val sampleItems: List<GalleryItem>
        get() = listOf(
            GalleryItem(R.drawable.bluetooth_icon, "Image 1", "01/01/2024"),
            GalleryItem(R.drawable.bluetooth_icon, "Image 2", "02/01/2024"),
            GalleryItem(R.drawable.bluetooth_icon, "Image 3", "03/01/2024"),
            GalleryItem(R.drawable.bluetooth_icon, "Image 4", "04/01/2024"),
            GalleryItem(R.drawable.bluetooth_icon, "Image 5", "05/01/2024"),
            GalleryItem(R.drawable.bluetooth_icon, "Image 6", "06/01/2024")
        )
}
