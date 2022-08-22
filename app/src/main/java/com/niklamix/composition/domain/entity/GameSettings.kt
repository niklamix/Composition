package com.niklamix.composition.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameSettings(
    val maxSumValue: Int,
    val maxCountOfRightAnswers: Int,
    val maxPercentOfRightAnswer: Int,
    val gameTimeInSeconds: Int
): Parcelable
