package com.mavino.aws_s3_upload.aws

import android.content.Context
import com.amazonaws.mobileconnectors.s3.transferutility.*
import com.amazonaws.services.s3.model.ObjectMetadata
import java.io.File
import java.lang.Exception
import java.util.*

class S3Uploader constructor(val context: Context) {

    private var transferUtility: TransferUtility? = null
    private var s3UploadListener: S3UploadListener? = null
    private var observer: TransferObserver? = null
    private var networkLossHandler: TransferNetworkLossHandler? = null

    init {
        transferUtility = AWSUtils.getTransferUtility(context)
        networkLossHandler = TransferNetworkLossHandler.getInstance(context)
    }

    fun uploadFile(filePath: String, mimeType: String){
        val file = File(filePath)
        val metadata = ObjectMetadata()
        metadata.contentType = mimeType

        val fileName = UUID.randomUUID().toString() + "." + mimeType.split("/")
        observer = transferUtility!!.upload(
            AWSKeys.BUCKET_NAME,
            fileName,
            file
        )

        observer!!.setTransferListener(transferListner())
    }

    private fun transferListner() = object : TransferListener{
            override fun onStateChanged(id: Int, state: TransferState?) {
                if (state == TransferState.COMPLETED){
                    s3UploadListener!!.onSuccess("success")
                }
            }

            override fun onProgressChanged(id: Int, bytesCurrent: Long, bytesTotal: Long) {
                TODO("Not yet implemented")
            }

            override fun onError(id: Int, ex: Exception?) {
                TODO("Not yet implemented")
            }

        }

    fun setOnUploadListener(s3UploadListener: S3UploadListener){
        this.s3UploadListener = s3UploadListener
    }

    fun pauseUpload(id: Int){
        transferUtility!!.pause(id)
    }

    fun network(){
        //todo add resume operations for network states
    }
}