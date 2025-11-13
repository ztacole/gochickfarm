package com.zetta.gochickfarm.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Summary(
    @SerialName("chicken_count") val chickenCount: Int,
    @SerialName("today_sold_chicken_count") val todaySoldChickenCount: Int,
    @SerialName("goat_count") val goatCount: Int,
    @SerialName("today_sold_goat_count") val todaySoldGoatCount: Int,
    @SerialName("today_income") val todayIncome: Int,
)