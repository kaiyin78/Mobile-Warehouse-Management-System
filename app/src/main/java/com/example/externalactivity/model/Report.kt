package com.example.externalactivity.model

data class Report(
    var partNo: String? = null,
    var productQuantity: String? = null,
    var productSerialNo: String? = null,
    var productStatus: String? = null,
    var rackID: String? = null,
    var rackInBy: String? = null,
    var rackInDate: String? = null,
    var receiveBy: String? = null,
    var receiveDate: String? = null,
)
