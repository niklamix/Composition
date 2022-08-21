package com.niklamix.composition.domain.entity

data class GameSettings(
    val maxSumValue: Int,
    val maxCountOfRightAnswers: Int,
    val maxPercentOfRightAnswer: Int,
    val gameTimeInSeconds: Int
)
