package com.example.food_app.dataModel

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class orderDetails(
    var userUID: String? = null,
    var userName: String? = null,
    var foodNames: MutableList<String> = mutableListOf(),
    var foodImages: MutableList<String> = mutableListOf(),
    var foodPrices: MutableList<String> = mutableListOf(),
    var foodQuantities: MutableList<String> = mutableListOf(),
    var address: String? = null,
    var totalPrice: String? = null,
    var phone: String? = null,
    var orderAccepted: Boolean = false,
    var paymentReceived: Boolean = false,
    var itemPushKey: String? = null,
    var currentTime: Long = 0,
) : Serializable {

    constructor(parcel: Parcel) : this(
        userUID = parcel.readString(),
        userName = parcel.readString(),
        foodNames = parcel.createStringArrayList() ?: mutableListOf(),
        foodImages = parcel.createStringArrayList() ?: mutableListOf(),
        foodPrices = parcel.createStringArrayList() ?: mutableListOf(),
        foodQuantities = parcel.createStringArrayList() ?: mutableListOf(),
        address = parcel.readString(),
        totalPrice = parcel.readString(),
        phone = parcel.readString(),
        orderAccepted = parcel.readByte() != 0.toByte(),
        paymentReceived = parcel.readByte() != 0.toByte(),
        itemPushKey = parcel.readString(),
        currentTime = parcel.readLong()
    )

    fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userUID)
        parcel.writeString(userName)
        parcel.writeStringList(foodNames)
        parcel.writeStringList(foodImages)
        parcel.writeStringList(foodPrices)
        parcel.writeStringList(foodQuantities)
        parcel.writeString(address)
        parcel.writeString(totalPrice)
        parcel.writeString(phone)
        parcel.writeByte(if (orderAccepted) 1 else 0)
        parcel.writeByte(if (paymentReceived) 1 else 0)
        parcel.writeString(itemPushKey)
        parcel.writeLong(currentTime)
    }

    fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<orderDetails> {
        override fun createFromParcel(parcel: Parcel): orderDetails = orderDetails(parcel)
        override fun newArray(size: Int): Array<orderDetails?> = arrayOfNulls(size)
    }

}