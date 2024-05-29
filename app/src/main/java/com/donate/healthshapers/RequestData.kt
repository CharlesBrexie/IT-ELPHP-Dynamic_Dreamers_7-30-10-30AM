package com.donate.healthshapers

data class RequestData(
    val ngoId: String,
    val donationId: String,
    val username: String,
    val userType: String,
    val phoneNumber: String,
    val status: String? = "Pending" // Default: "Pending"
)
{
    // No-argument constructor required by Firebase
    constructor() : this("", "", "", "", "", "Pending")
}