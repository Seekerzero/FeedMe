package com.example.feedme

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import org.json.JSONObject
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "JSON_HANDLER"
private const val NumOfAwards = 9

class JsonHandler(){
    val mochiInfo = MochiInfo("test", SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().time))
    var awardDates = mutableListOf<String>()
    var awardNames = mutableListOf<String>()


    init {
        for(i in 0 until NumOfAwards){
            awardDates.add("00000000")
        }
       print("Finished Initialized award date list with size of ${awardDates.size}\n")
    }


    fun createMochiInfoFile(context: Context){
        val jsonObject = JSONObject()
        jsonObject.put("Name", mochiInfo.name)
        jsonObject.put("Birthday", mochiInfo.birthday)
        for(i in 0 until awardDates.size){
            jsonObject.put("AwardDate${i+1}", awardDates[i])
        }
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
        var newAwardDates = mutableListOf<String>()
        for (i in 0 until  awardDates.size) {
             awardDates[i] = jsonObject.get("AwardDate${i+1}").toString()
        }
    }

    fun getAwardDate(awardName: String):String{
                return when(awardName){
                    "Happy Chomper" -> awardDates[0]
                    "Healthy Chomper" -> awardDates[1]
                    "Strong & Healthy Chomper" -> awardDates[2]
                    "Healthy Heart" -> awardDates[3]
                    "Health Fighter" -> awardDates[4]
                    "Health Warrior" -> awardDates[5]
                    "World Travller" -> awardDates[6]
                    "Extreme Health" -> awardDates[7]
                    "They Grow Up So Fast" -> awardDates[8]
                    else ->"00000000"
                }
    }

    @SuppressLint("SimpleDateFormat")
    fun updateAwardDate(awardName: String, dateString: String){
        when(awardName){
            "Happy Chomper" -> awardDates[0] = dateString
            "Healthy Chomper" -> awardDates[1] = dateString
            "Strong & Healthy Chomper" -> awardDates[2] = dateString
            "Healthy Heart" -> awardDates[3] = dateString
            "Health Fighter" -> awardDates[4] = dateString
            "Health Warrior" -> awardDates[5] = dateString
            "World Travller" -> awardDates[6] = dateString
            "Extreme Health" -> awardDates[7] = dateString
            "They Grow Up So Fast" -> awardDates[8] = dateString
            else -> print("Wrong Award Name provided\n")
        }
    }











}