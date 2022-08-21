package com.niklamix.composition.domain.repository

import com.niklamix.composition.domain.entity.GameSettings
import com.niklamix.composition.domain.entity.Level
import com.niklamix.composition.domain.entity.Question

interface GameRepository {
    fun generateQuestion(maxSumValue: Int, countOfOption: Int): Question

    fun getGameSettings(level: Level): GameSettings
}