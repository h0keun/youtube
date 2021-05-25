package com.youtube.service

import com.youtube.dto.VideoDto
import retrofit2.Call
import retrofit2.http.GET

interface VideoService {

    @GET("/v3/566f8888-52b3-4125-beeb-aeede26549de")
    fun listVideos(): Call<VideoDto>
}