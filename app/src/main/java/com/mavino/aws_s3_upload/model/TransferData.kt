package com.mavino.aws_s3_upload.model

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver

data class TransferData(
    val fileName: String,
    val observer: TransferObserver
)
