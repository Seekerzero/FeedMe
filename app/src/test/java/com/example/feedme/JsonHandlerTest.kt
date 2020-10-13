package com.example.feedme


import org.junit.Before

import org.junit.Assert.*
import org.junit.Test


class JsonHandlerTest {

    private lateinit var jsonHandler: JsonHandler

    @Before
    fun setUp() {
        jsonHandler = JsonHandler()
        jsonHandler.awardDates[0] ="20201010"
        jsonHandler.awardDates[1] ="20201011"
    }

    @Test
    fun testGetAwardDateCorrectInput(){
        assertEquals(jsonHandler.getAwardDate("Happy Chomper"), "20201010")
    }

    @Test
    fun testGetAwardDateInCorrectInput(){
        assertEquals(jsonHandler.getAwardDate("wrong award"), "00000000")
    }

    @Test
    fun testupdateAwardDateCorrectInput(){
        jsonHandler.updateAwardDate("Happy Chomper", "19980802")
        assertEquals(jsonHandler.awardDates[0], "19980802")
    }

    @Test
    fun testupdateAwardDateInorrectInput(){
        jsonHandler.updateAwardDate("wrong award", "19491001")
        assertEquals(jsonHandler.awardDates[0], "20201010")
        assertEquals(jsonHandler.awardDates[1], "20201011")
        for (i in 2 until jsonHandler.awardDates.size){
            assertEquals(jsonHandler.awardDates[i], "00000000")
        }
    }



}