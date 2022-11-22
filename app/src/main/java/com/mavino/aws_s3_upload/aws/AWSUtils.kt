package com.mavino.aws_s3_upload.aws

import android.content.Context
import com.amazonaws.auth.CognitoCachingCredentialsProvider
import com.amazonaws.auth.CognitoCredentialsProvider
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3Client

object AWSUtils {

    private var amazonS3Client: AmazonS3Client? = null
    private var cognitoCredProvider: CognitoCredentialsProvider? = null
    private var transferUtility: TransferUtility? = null


    private fun getCognitoCredProvider(context: Context?): CognitoCredentialsProvider?{
        if (cognitoCredProvider == null){
            cognitoCredProvider = CognitoCachingCredentialsProvider(
                context,
                AWSKeys.COGNITO_POOL_ID,
                AWSKeys.COGNITO_REGION
            )
        }

        return cognitoCredProvider
    }

    fun getS3Client(context: Context?): AmazonS3Client?{
        if (amazonS3Client == null){
            amazonS3Client = AmazonS3Client(
                getCognitoCredProvider(context),
                Region.getRegion(AWSKeys.COGNITO_REGION)
            )
            amazonS3Client!!.setRegion(Region.getRegion(AWSKeys.BUCKET_REGION))
        }

        return amazonS3Client
    }


    fun getTransferUtility(context: Context?): TransferUtility?{
        if (transferUtility == null){
            transferUtility = TransferUtility.builder()
                .s3Client(getS3Client(context))
                .context(context).build()
        }

        return transferUtility
    }
}