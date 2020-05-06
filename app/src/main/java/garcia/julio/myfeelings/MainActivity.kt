package garcia.julio.myfeelings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import garcia.julio.myfeelings.utilities.CustomBarDrawable
import garcia.julio.myfeelings.utilities.CustomCircleDrawable
import garcia.julio.myfeelings.utilities.Emociones
import garcia.julio.myfeelings.utilities.JSONFile
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    var jsonFile: JSONFile?= null
    var veryhappy = 0.0F
    var happy = 0.0F
    var neutral = 0.0F
    var sad = 0.0F
    var verysad = 0.0F
    var data: Boolean = false
    var lista = ArrayList<Emociones>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        jsonFile = JSONFile()
        fetchingData()
        if(!data){
            var emociones = ArrayList<Emociones>()
            val fondo = CustomCircleDrawable(this, emociones)
            graph.background = fondo
            graphVeryHappy.background = CustomBarDrawable(this,Emociones("Muy feliz",0.0F,R.color.mustard,veryhappy))
            graphHappy.background = CustomBarDrawable(this,Emociones("Feliz",0.0F,R.color.orange,happy))
            graphNeutral.background = CustomBarDrawable(this,Emociones("Neutral",0.0F,R.color.greenie,neutral))
            graphSad.background = CustomBarDrawable(this,Emociones("Triste",0.0F,R.color.blue,sad))
            graphVerySad.background = CustomBarDrawable(this,Emociones("Muy triste",0.0F,R.color.deepBlue,verysad))
        } else {
            actualizarGrafica()
            iconoMayoria()
        }

        guardarButton.setOnClickListener {
            guardar()
        }

        veryHappyButton.setOnClickListener {
            veryhappy++
            iconoMayoria()
            actualizarGrafica()
        }

        happyButton.setOnClickListener {
            happy++
            iconoMayoria()
            actualizarGrafica()
        }

        neutralButton.setOnClickListener {
            neutral++
            iconoMayoria()
            actualizarGrafica()
        }

        sadButton.setOnClickListener {
            sad++
            iconoMayoria()
            actualizarGrafica()
        }

        verySadButton.setOnClickListener {
            verysad++
            iconoMayoria()
            actualizarGrafica()
        }
    }

    fun fetchingData(){
        try {
            var json : String = jsonFile?.getData(this)?:""
            if (json!=""){
                this.data = true
                var jsonArray: JSONArray= JSONArray(json)
                this.lista = parseJson(jsonArray)
                for (i in lista){
                    when(i.nombre){
                        "Muy feliz" -> veryhappy = i.total
                        "Feliz" -> happy = i.total
                        "Neutral" -> neutral = i.total
                        "Triste" -> sad = i.total
                        "Muy triste" -> verysad = i.total
                    }
                }
            } else {
                this.data = false
            }
        } catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun parseJson(jsonArray: JSONArray):ArrayList<Emociones>{
        var lista = ArrayList<Emociones>()
        for (i in 0..jsonArray.length()){
            try {
                val nombre = jsonArray.getJSONObject(i).getString("nombre")
                val porcentaje = jsonArray.getJSONObject(i).getDouble("porcentaje").toFloat()
                val color = jsonArray.getJSONObject(i).getInt("color")
                val total = jsonArray.getJSONObject(i).getDouble("total").toFloat()
                var emocion = Emociones(nombre, porcentaje, color, total)
                lista.add(emocion)
            } catch (exception:JSONException){
                exception.printStackTrace()
            }
        }
        return lista
    }

    fun actualizarGrafica(){
        val total = veryhappy+happy+neutral+sad+verysad
        var pVH: Float = (veryhappy*100/total).toFloat()
        var pH: Float = (happy*100/total).toFloat()
        var pN: Float = (neutral*100/total).toFloat()
        var pS: Float = (sad*100/total).toFloat()
        var pVS: Float = (verysad*100/total).toFloat()

        lista.clear()
        lista.add(Emociones("Muy feliz", pVH, R.color.mustard,veryhappy))
        lista.add(Emociones("Feliz", pH, R.color.orange,happy))
        lista.add(Emociones("Neutral",pN,R.color.greenie, neutral))
        lista.add(Emociones("Triste",pS,R.color.blue,sad))
        lista.add(Emociones("Muy triste",pVS,R.color.deepBlue,verysad))

        val fondo = CustomCircleDrawable(this, lista)
        graphVeryHappy.background = CustomBarDrawable(this,Emociones("Muy feliz",pVH,R.color.mustard,veryhappy))
        graphHappy.background = CustomBarDrawable(this,Emociones("Feliz",pH,R.color.orange,happy))
        graphNeutral.background = CustomBarDrawable(this,Emociones("Neutral",pN,R.color.greenie,neutral))
        graphSad.background = CustomBarDrawable(this,Emociones("Triste",pS,R.color.blue,sad))
        graphVerySad.background = CustomBarDrawable(this,Emociones("Muy triste",pVS,R.color.deepBlue,verysad))

        graph.background = fondo
    }

    fun iconoMayoria(){
        if (happy>veryhappy && happy>neutral && happy>sad && happy>verysad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_happy))
        }

        if (veryhappy>happy && veryhappy>neutral && veryhappy>sad && veryhappy>verysad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_veryhappy))
        }

        if (neutral>veryhappy && neutral>happy && neutral>sad && neutral>verysad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_neutral))
        }

        if (sad>happy && sad>neutral && sad>veryhappy && sad>verysad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_sad))
        }

        if (verysad>happy && verysad>neutral && verysad>sad && veryhappy<verysad){
            icon.setImageDrawable(resources.getDrawable(R.drawable.ic_verysad))
        }
    }

    fun guardar(){
        var jsonArray = JSONArray()
        var o : Int = 0

        for (i in lista){
            var j: JSONObject = JSONObject()
            j.put("nombre",i.nombre)
            j.put("porcentaje", i.porcentaje)
            j.put("color",i.color)
            j.put("total", i.total)
            jsonArray.put(o, j)
            o++
        }

        jsonFile?.saveData(this, jsonArray.toString())
        Toast.makeText(this, "Datos guardados", Toast.LENGTH_SHORT).show()
    }
}
