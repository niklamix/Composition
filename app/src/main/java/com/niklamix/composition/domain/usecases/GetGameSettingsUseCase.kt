package com.niklamix.composition.domain.usecases

import com.niklamix.composition.domain.entity.GameSettings
import com.niklamix.composition.domain.entity.Level
import com.niklamix.composition.domain.repository.GameRepository

class GetGameSettingsUseCase(private val repository: GameRepository) {

    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }
}