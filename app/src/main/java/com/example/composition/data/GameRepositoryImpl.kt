package com.example.composition.data

import com.example.composition.domain.entity.GameSettings
import com.example.composition.domain.entity.Level
import com.example.composition.domain.entity.Question
import com.example.composition.domain.repository.GameRepository
import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.random.Random

object GameRepositoryImpl : GameRepository {
    private const val MIN_SUM_VALUE = 3
    private const val MIN_ANSWER_VALUE = 1
    override fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question {
        val sum = Random.nextInt(MIN_SUM_VALUE, maxSumValue + 1)
        val visibleNumber = Random.nextInt(MIN_ANSWER_VALUE, sum)
        val answer = sum - visibleNumber
        val from = max(answer - countOfOptions, MIN_ANSWER_VALUE)
        val to = min(answer + countOfOptions, maxSumValue - 1)

        val options = HashSet<Int>()
        options.add(answer)
        while (options.size < countOfOptions) {
            options.add(Random.nextInt(from, to))
        }
        return Question(sum, visibleNumber, options.toList())
    }

    override fun getGameSettings(level: Level): GameSettings {
        return when (level) {
            Level.TEST -> GameSettings(
                10,
                3,
                50,
                10
            )

            Level.EASY -> GameSettings(
                20,
                10,
                70,
                60
            )

            Level.NORMAL -> GameSettings(
                50,
                20,
                80,
                40
            )

            Level.HARD -> GameSettings(
                100,
                25,
                95,
                30
            )


        }
    }

}