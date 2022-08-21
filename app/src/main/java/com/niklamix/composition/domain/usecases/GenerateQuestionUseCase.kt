package com.niklamix.composition.domain.usecases

import com.niklamix.composition.domain.entity.GameSettings
import com.niklamix.composition.domain.entity.Question
import com.niklamix.composition.domain.repository.GameRepository

class GenerateQuestionUseCase(private val repository: GameRepository) {

    operator fun invoke(maxSumValue: Int): Question {
        return repository.generateQuestion(maxSumValue, COUNT_OF_OPTIONS)
    }

    private companion object {
        private const val COUNT_OF_OPTIONS = 6
    }
}