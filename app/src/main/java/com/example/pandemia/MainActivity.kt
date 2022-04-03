package com.example.pandemia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
import com.example.pandemia.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBuscar.setOnClickListener {
            buscarDatosPandemia()
        }
    }

    fun getRetrofit(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("https://disease.sh/v3/covid-19/countries")
            .client(OkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun buscarDatosPandemia(){
        CoroutineScope(Dispatchers.Main).launch {
            try{
                val txtFiltro: String = binding.txtBuscar.text.toString()
                val call = getRetrofit().create(ApiService::class.java).getDatosPandemia(txtFiltro)
                if(call.isSuccessful){
                    val pais:String = call.body()?.country.toString()
                    val poblacion:String = call.body()?.population.toString()
                    val casos:String = call.body()?.cases.toString()
                    val recuperados: String = call.body()?.recovered.toString()

                    binding.txtPais.text = "Pais: $pais"
                    binding.txtPoblacion.text = "Poblacion: $poblacion"
                    binding.txtCasos.text = "Casos: $casos"
                    binding.txtRecuperados.text = "Recuperados: $recuperados"
                }
            } catch(ex: Exception){
                val msn = Toast.makeText(this@MainActivity, "Error de conexion", Toast.LENGTH_LONG)
                msn.setGravity(Gravity.CENTER, 0, 0)
                msn.show()
            }
        }
    }
}