package com.mavino.aws_s3_upload.aws

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.webkit.MimeTypeMap
import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest
import com.amazonaws.services.s3.model.ResponseHeaderOverrides
import java.io.File
import java.util.*

object S3Utils {

    @JvmStatic
    fun generateS3Url(context: Context?, path: String?): String{
        //create file from path
        val file = File(path)
        val s3Client = AWSUtils.getS3Client(context)

        //build request
        val expiration = Date()
        var sec = expiration.time
        sec += 1000 * 60 * 60.toLong()
        expiration.time = sec

        val overrideHeader = ResponseHeaderOverrides()
        overrideHeader.contentType = getMimeType(path)
        val mediaUrl = file.name
        val generatePresignedUrlRequest = GeneratePresignedUrlRequest(AWSKeys.BUCKET_NAME, mediaUrl)
        generatePresignedUrlRequest.method = HttpMethod.GET
        generatePresignedUrlRequest.expiration = expiration
        generatePresignedUrlRequest.responseHeaders = overrideHeader

        //make request
        val url = s3Client!!.generatePresignedUrl(generatePresignedUrlRequest)

        Log.d(TAG, "generated S3Url: $url")

        return url.toString()
    }

    private fun getMimeType(url: String?): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }
}