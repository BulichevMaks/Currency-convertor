package com.example.converter_3.presentation

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.converter_3.R
import com.example.converter_3.domian.models.*

class MainActivity : AppCompatActivity() {


    private lateinit var buttonToFavourite: ImageView
    private lateinit var buttonToFavouriteOff: ImageView
    private lateinit var recyclerList: RecyclerView

    //   private lateinit var buttonRefresh: ImageView
    private lateinit var inputAmount: EditText
    private lateinit var placeholder: View

    //  private lateinit var apiLayerService: ApiLayerApi
    private lateinit var adapter: RateAdapter
    private lateinit var inputEditText: EditText
    private var input = "1"
    private lateinit var inputAmountView: LinearLayout
    var listOfFavouritesRates = ListOfFavouritesRates.listOfFavouritesRates


    private fun getRatesRub(): List<Favourites> {
        val listOfFavouritesRates = mutableListOf<Favourites>()
        for (name in RatesFrom.rateFromRUB) {
            val item = Favourites(name = name.first, rate = name.second)
            listOfFavouritesRates.add(item)
        }
        return listOfFavouritesRates
    }

    private fun getRates(): List<Rates> {
        val listOfRates = mutableListOf<Rates>()
        for (name in RatesFrom.rateFromEUR) {
            val item = Rates(name = name.first, rate = name.second)
            listOfRates.add(item)
        }
        return listOfRates
    }

    private fun getRatesFromApi(): List<Rates> {

        val listOfRates = mutableListOf<Rates>()
        for (name in RatesActual.rateFromEUR!!) {
            val item = Rates(name = name.first, rate = name.second)
            listOfRates.add(item)
        }
        return listOfRates
    }


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        buttonToFavourite = findViewById(R.id.favourites_on)
        inputAmount = findViewById(R.id.inputAmount)
        buttonToFavouriteOff = findViewById(R.id.favourites_off)
        inputEditText = findViewById(R.id.inputEditText)
        inputAmountView = findViewById(R.id.input_amount_layout)

        val ratesFromApi = getRatesFromApi()
        adapter = RateAdapter(this).apply {
          //  this.items = getRates()
            this.items = ratesFromApi
        }

        val rootLayout = findViewById<LinearLayout>(R.id.root)
        val parentViewGroup = rootLayout.parent as ViewGroup

        val problem = mutableListOf(Problem())
        val spinner = findViewById<Spinner>(R.id.currency_spinner)
        val spinnerAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.currencies,
            R.layout.custom_spinner_dropdown_item
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter
        spinner.onItemSelectedListener = object : AdapterView
        .OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if (listOfFavouritesRates.isNotEmpty()) {
                    when (spinner.selectedItem) {
                        "RUB" -> search(RatesActual.rateFromRUB!!)
                        "EUR" -> search(RatesActual.rateFromEUR!!)
                        "RSD" -> search(RatesActual.rateFromRSD!!)
                        "USD" -> search(RatesActual.rateFromUSD!!)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Another interface callback
            }
        }



        inputAmount.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                when (spinner.selectedItem) {
                    "RUB" -> search(RatesActual.rateFromRUB!!)
                    "EUR" -> search(RatesActual.rateFromEUR!!)
                    "RSD" -> search(RatesActual.rateFromRSD!!)
                    "USD" -> search(RatesActual.rateFromUSD!!)
                }

                true
            }
            false
        }


//        val interceptor = HttpLoggingInterceptor()
//        interceptor.level = HttpLoggingInterceptor.Level.BODY
//        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()


        recyclerList = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerList.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerList.adapter = adapter



        buttonToFavourite.setOnClickListener {

            if (listOfFavouritesRates.isNotEmpty()) {
                inputAmountView.visibility = View.VISIBLE
                adapter.items = listOfFavouritesRates
                adapter.notifyDataSetChanged()
            } else {
                inputAmountView.visibility = View.GONE
            }
        }
        buttonToFavouriteOff.setOnClickListener {
            inputAmountView.visibility = View.GONE
          //  adapter.items = getRates()
            adapter.items = ratesFromApi
            adapter.notifyDataSetChanged()
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
        inputAmount.addTextChangedListener(searchTextWatcher)

    }

    private fun search(list: List<Pair<String, Double>>) {

        for (itemF in listOfFavouritesRates) {
            for (itemL in list) {

                if (itemF.name == itemL.first) {
                    itemF.rate = itemL.second * input.toDouble()
                }
            }
        }


        //     val updatedRates = list.map { it.name to it.rate * input.toDouble() }
        adapter.items = listOfFavouritesRates
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
