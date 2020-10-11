package com.example.feedme

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import org.json.JSONObject
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "JSON_HANDLER"

class JsonHandler(){

    val mochiInfo = MochiInfo("test", SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().time))
    var awardDate1: String = "00000000"// for what award
    var awardDate2: String = "00000000"
    var awardDate3: String = "00000000"
    var awardDate4: String = "00000000"
    var awardDate5: String = "00000000"
    var awardDate6: String = "00000000"
    var awardDate7: String = "00000000"
    var awardDate8: String = "00000000"
    var awardDate9: String = "00000000"




    fun createMochiInfoFile(context: Context){
        val jsonObject = JSONObject()
        jsonObject.put("Name", mochiInfo.name)
        jsonObject.put("Birthday", mochiInfo.birthday)
        jsonObject.put("AwardDate1", awardDate1)
        jsonObject.put("AwardDate2", awardDate2)
        jsonObject.put("AwardDate3", awardDate3)
        jsonObject.put("AwardDate4", awardDate4)
        jsonObject.put("AwardDate5", awardDate5)
        jsonObject.put("AwardDate6", awardDate6)
        jsonObject.put("AwardDate7", awardDate7)
        jsonObject.put("AwardDate8", awardDate8)
        jsonObject.put("AwardDate9", awardDate9)
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
        awardDate1 = jsonObject.get("AwardDate1").toString()
        awardDate2 = jsonObject.get("AwardDate2").toString()
        awardDate3 = jsonObject.get("AwardDate3").toString()
        awardDate4 = jsonObject.get("AwardDate4").toString()
        awardDate5 = jsonObject.get("AwardDate5").toString()
        awardDate6 = jsonObject.get("AwardDate6").toString()
        awardDate7 = jsonObject.get("AwardDate7").toString()
        awardDate8 = jsonObject.get("AwardDate8").toString()
        awardDate9 = jsonObject.get("AwardDate9").toString()
    }

    @SuppressLint("SimpleDateFormat")
    private fun convertStr2Date(strDate: String): Date {
        if (strDate == null){
            return SimpleDateFormat("yyyyMMdd").parse("00000000")
        }
        return SimpleDateFormat("yyyyMMdd").parse(strDate)
    }

    fun getDateforAward(awardName: String):Date{
                return when(awardName){
                    "Happy Chomper" -> convertStr2Date(awardDate1)
                    "Healthy Chomper" -> convertStr2Date(awardDate2)
                    "Strong & Healthy Chomper" -> convertStr2Date(awardDate3)
                    "Healthy Heart" -> convertStr2Date(awardDate4)
                    "Health Fighter" -> convertStr2Date(awardDate5)
                    "Health Warrior" -> convertStr2Date(awardDate6)
                    "World Travller" -> convertStr2Date(awardDate7)
                    "Extreme Health" -> convertStr2Date(awardDate8)
                    "They Grow Up So Fast" -> convertStr2Date(awardDate9)
                    else -> convertStr2Date("00000000")
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun updateDateforAward(context: Context, awardName: String, date: Date){
        when(awardName){
            "Happy Chomper" -> awardDate1 = SimpleDateFormat("yyyyMMdd").format(date)
            "Healthy Chomper" -> awardDate2 = SimpleDateFormat("yyyyMMdd").format(date)
            "Strong & Healthy Chomper" -> awardDate3 = SimpleDateFormat("yyyyMMdd").format(date)
            "Healthy Heart" -> awardDate4 = SimpleDateFormat("yyyyMMdd").format(date)
            "Health Fighter" -> awardDate5 = SimpleDateFormat("yyyyMMdd").format(date)
            "Health Warrior" -> awardDate6 = SimpleDateFormat("yyyyMMdd").format(date)
            "World Travller" -> awardDate7 = SimpleDateFormat("yyyyMMdd").format(date)
            "Extreme Health" -> awardDate8 = SimpleDateFormat("yyyyMMdd").format(date)
            "They Grow Up So Fast" -> awardDate9 = SimpleDateFormat("yyyyMMdd").format(date)
        }
        createMochiInfoFile(context)
    }











}