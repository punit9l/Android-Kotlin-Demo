package com.t9l.androidkotlindemo.recyclerView

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.t9l.androidkotlindemo.R
import kotlinx.android.synthetic.main.activity_recycler.*

class RecyclerActivity : AppCompatActivity() {

    private lateinit var mAdapter: CustomAdapter
    private val mutableList: MutableList<CustomItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)

        supportActionBar?.title = "RecyclerView Demo"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mAdapter = CustomAdapter(mutableList)

        val layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = layoutManager
    }

    override fun onResume() {
        super.onResume()

        for (index in 1..100) {
            mutableList.add(CustomItem(index, "Item $index"))
            mAdapter.notifyDataSetChanged()
        }
    }
}
