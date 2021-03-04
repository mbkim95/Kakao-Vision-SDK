package com.example.vision

import com.example.vision.network.ApiFactory
import com.example.vision.network.VisionApi
import org.junit.Test

class VisionApiClientTest {
    var visionApi: VisionApi = ApiFactory.vapi.create(VisionApi::class.java)

    @Test
    fun getOcrTest(){
        val result = visionApi.getOcr("https://www.google.com").execute()

        if(result.isSuccessful){
            println("${result.body()?.boxes}")
        }else{
            println("${result.errorBody()} ${result.code()}")
        }
    }
}