package com.niklamix.composition.data

import com.niklamix.composition.domain.entity.GameSettings
import com.niklamix.composition.domain.entity.Level
import com.niklamix.composition.domain.entity.Question
import com.niklamix.composition.domain.repository.GameRepository
import kotlin.math.max
import kotlin.math.min

import kotlin.random.Random

object GameRepositoryImpl: GameRepository {

    private const val MIN_SUM_VALUE = 2
    private const val MIN_ANSWER_VALUE = 1

    override fun generateQuestion(maxSumValue: Int, countOfOption: Int): Question {
        val sum = Random.nextInt(MIN_ANSWER_VALUE, maxSumValue.plus(1))
        val visibleNumber = Random.nextInt(MIN_ANSWER_VALUE, sum)
        val options = HashSet<Int>()
        val rightAnswer = sum - visibleNumber
        options.add(rightAnswer)
        val from = max(rightAnswer - countOfOption, MIN_ANSWER_VALUE)
        val to = min(maxSumValue, rightAnswer + countOfOption)
        while (options.size < countOfOption) {
            options.add(Random.nextInt(from, to))
        }
        return Question(sum, visibleNumber, options.toList())
    }

    override fun getGameSettings(level: Level): GameSettings {
       return when (level) {
           Level.TEST -> {
               GameSettings(
                   maxSumValue = 10,
                   maxCountOfRightAnswers = 3,
                   maxPercentOfRightAnswer = 50,
                   gameTimeInSeconds = 8
               )
           }
           Level.EASY -> {
               GameSettings(
                   maxSumValue = 10,
                   maxCountOfRightAnswers = 10,
                   maxPercentOfRightAnswer = 60,
                   gameTimeInSeconds = 60
               )
           }
           Level.NORMAL -> {
               GameSettings(
                   maxSumValue = 30,
                   maxCountOfRightAnswers = 15,
                   maxPercentOfRightAnswer = 70,
                   gameTimeInSeconds = 50
               )
           }
           Level.HARD -> {
               GameSettings(
                   maxSumValue = 50,
                   maxCountOfRightAnswers = 20,
                   maxPercentOfRightAnswer = 80,
                   gameTimeInSeconds = 40
               )
           }
       }
    }
}