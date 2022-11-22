package com.mavino.aws_s3_upload.aws

interface S3UploadListener {

    fun onSuccess(response: String?)

    fun onError(response: String?)
}