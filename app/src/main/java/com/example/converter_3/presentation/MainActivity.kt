package com.example.converter_3.presentation

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.Layout
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.converter_3.R
import com.example.converter_3.domian.models.*

class MainActivity : AppCompatActivity() {


    private lateinit var buttonRefresh: ImageView

    private lateinit var recyclerList: RecyclerView
    private lateinit var placeholder: View

    //  private lateinit var apiLayerService: ApiLayerApi
    private lateinit var adapter: RateAdapter
    private lateinit var inputEditText: EditText
    private var input = ""
    private val listOfRatesFromRUB: List<Pair<String, Double>> = RatesFromRUB.rateFromRUB
    private val listOfRatesFromEUR: List<Pair<String, Double>> = RatesFromEUR.rateFromEUR
    //  private val listOfRatesNames: List<Pair<String, String>> = RatesFromRUB.listOfCurrencyName
    //  private val spinner: Spinner = findViewById<Spinner>(R.id.currency_spinner)


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputEditText = findViewById(R.id.inputEditText)
        adapter = RateAdapter().apply {
            items = listOfRatesFromRUB
        }
        //    placeholder = findViewById(View.generateViewId())
        buttonRefresh = findViewById(R.id.refresh)

        val problem = mutableListOf(Problem())
        val spinner = findViewById<Spinner>(R.id.currency_spinner)
        val spinnerAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.currencies,
            R.layout.custom_spinner_dropdown_item
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

//        val interceptor = HttpLoggingInterceptor()
//        interceptor.level = HttpLoggingInterceptor.Level.BODY
//        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()


        recyclerList = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerList.adapter = adapter

        buttonRefresh.setOnClickListener {
            if (input.isNotEmpty()) {
                when (spinner.selectedItem) {
                    "RUB" -> search(listOfRatesFromRUB)
                    "EUR" -> search(listOfRatesFromEUR)
                }
            }
        }
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                when (spinner.selectedItem) {
                    "RUB" -> search(listOfRatesFromRUB)
                    "EUR" -> search(listOfRatesFromEUR)
                }

                true
            }
            false
        }
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                input = s.toString()
                //  clearButton.visibility = clearButtonVisibility(s)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
        inputEditText.addTextChangedListener(searchTextWatcher)

        spinner.onItemSelectedListener = object : AdapterView
        .OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                // Display the selected item text on text view
//                textView.text = "Spinner selected : " +
//                        "${parent.getItemAtPosition(position)}"
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }

    }

    private fun search(list: List<Pair<String, Double>>) {

        val updatedRates = list.map { it.first to it.second * input.toDouble() }
        adapter.items = updatedRates
        adapter.notifyDataSetChanged()
        showMessage("", Event.SUCCESS)
    }

    private fun showMessage(text: String, event: Event) {

        when (event) {
            Event.SUCCESS -> {

                //   buttonRefresh.visibility = View.GONE
            }
            Event.NOTHING_FOUND -> {
                adapter.items = mutableListOf(Problem())
                //     adapter.notifyDataSetChanged()

            }
            Event.SERVER_ERROR -> {
                adapter.items = mutableListOf(Problem())
            }
//                image = if (isDarkTheme()) {
//                    placeholder.setDrawableTop(R.drawable.error_not_found_dark)
//                    R.drawable.error_not_found_dark
            //               } else {
//                    placeholder.setDrawableTop(R.drawable.error_not_found_light)
//                    R.drawable.error_not_found_light
            else -> {
                adapter.items = mutableListOf(Problem())
            }
        }
    }


}
