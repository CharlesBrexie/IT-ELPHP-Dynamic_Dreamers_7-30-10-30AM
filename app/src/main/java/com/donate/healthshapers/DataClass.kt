package com.donate.healthshapers

import java.io.Serializable

data class DataClass(var dataImage:Int, var dataTitle:String, var dataQuantity: Int,
                     var dataLocation: String, var dataPickup: String, var dataDescription: String,
                     var dataTimeSent: String): Serializable