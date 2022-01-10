package com.riders.thelabback.data.model.order

import kotlinx.serialization.Serializable

@Serializable
data class Order(val number: String, val contents: List<OrderItem>)