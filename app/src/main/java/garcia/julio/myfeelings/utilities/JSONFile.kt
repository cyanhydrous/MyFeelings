package garcia.julio.myfeelings.utilities

import android.content.Context
import android.util.Log
import java.lang.Exception

class JSONFile {
    val MY_FEELINGS = "data.json"

    constructor(){

    }

    fun saveData(context: Context, json: String){
        try {
            context.openFileOutput(MY_FEELINGS, Context.MODE_PRIVATE).use {
                it.write(json.toByteArray())
            }
        } catch (e: Exception){
            Log.e("Guardar", "Error in writing"+ e.localizedMessage)
        }
    }

    fun getData(context: Context):String{
        try {
            return context.openFileInput(MY_FEELINGS).bufferedReader().readLine()
        } catch (e: Exception){
            Log.e("OBTENER", "Error in fetching data"+e.localizedMessage)
            return ""
        }
    }
}