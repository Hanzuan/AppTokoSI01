package com.aplikasi.apptokosi01

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aplikasi.apptokosi01.adapter.TransaksiAdapter
import com.aplikasi.apptokosi01.api.BaseRetrofit
import com.aplikasi.apptokosi01.response.cart.Cart
import com.aplikasi.apptokosi01.response.produk.ProdukResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class BeliFragment : Fragment() {
    private val api by lazy { BaseRetrofit().endpoint }
    private lateinit var my_cart : ArrayList<Cart>
    private lateinit var total_bayar : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_beli, container, false)

        getProduk(view)

        val btnBayar = view.findViewById<Button>(R.id.btnBeli)
        btnBayar.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelableArrayList("MY_CART",my_cart)
            bundle.putString("TOTAL",total_bayar)
            findNavController().navigate(R.id.bayarFragment,bundle)
        }

        return view
    }

    fun getProduk(view:View){
        val token = LoginActivity.sessionManager.getString("TOKEN")

        api.getProduk(token.toString()).enqueue(object : Callback<ProdukResponse> {
            override fun onResponse(
                call: Call<ProdukResponse>,
                response: Response<ProdukResponse>
            ) {
                Log.d("DataBeli",response.body().toString())

                val rv = view.findViewById(R.id.rv_beli) as RecyclerView

                rv.setHasFixedSize(true)
                rv.layoutManager = LinearLayoutManager(activity)
                val rvAdapter = TransaksiAdapter(response.body()!!.data.produk)
                rv.adapter = rvAdapter

                rvAdapter.callbackInterface = object : CallbackInterface{
                    override fun passResultCallback(total: String, cart: ArrayList<Cart>) {
                        val txtTotalBeli = activity?.findViewById<TextView>(R.id.txtTotalBeli)

                        val localeID = Locale("in", "ID")
                        val numberFormat = NumberFormat.getCurrencyInstance(localeID)

                        txtTotalBeli?.setText(numberFormat.format(total.toDouble()).toString())

                        total_bayar = total
                        my_cart = cart
                        Log.d("myCart", cart.toString())
                    }

                }
            }

            override fun onFailure(call: Call<ProdukResponse>, t: Throwable) {
                Log.e("ProdukError",t.toString())
            }
        })
    }
}