package com.example.feedme

import android.content.Context
import android.util.Log
import org.json.JSONObject
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "JSON_HANDLER"

class JsonHandler(){

    val mochiInfo = MochiInfo("test", SimpleDateFormat("MM/dd/yyyy").format(Calendar.getInstance().time))


    fun createMochiInfoFile(context: Context){
        val jsonObject = JSONObject()
        jsonObject.put("Name", mochiInfo.name)
        jsonObject.put("Birthday", mochiInfo.birthday)
        val mochiInfoString = jsonObject.toString()
        val file = File(context.filesDir, "MochiInfo.json")
        val filewriter = FileWriter(file)
        val bufferedWriter = BufferedWriter(filewriter)
        bufferedWriter.write(mochiInfoString)
        bufferedWriter.close()
        Log.d(TAG, "creating info file: $mochiInfoString")
    }

    fun readMochiInfoFile(context: Context){
        val file = File(context.filesDir, "MochiInfo.json")
        val fileReader = FileReader(file)
        val bufferedReader = BufferedReader(fileReader)
        var line: String = bufferedReader.readLine()
        Log.d(TAG, "getting info file: $line")
        bufferedReader.close()
        val jsonObject = JSONObject(line)
        mochiInfo.name = jsonObject.get("Name").toString()
        mochiInfo.birthday = jsonObject.get("Birthday").toString()
    }






}