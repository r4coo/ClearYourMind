package com.clearyourmind.diario.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.clearyourmind.diario.data.MoodEntry
import com.clearyourmind.diario.data.MoodEntryDao
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MoodViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: MoodViewModel
    private lateinit var dao: MoodEntryDao

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        dao = mockk(relaxed = true)
        viewModel = MoodViewModel(dao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `saveEntry inserts entry into dao when reflection is valid`() = runTest(testDispatcher) {
        // Given
        val moodScore = 8
        val reflection = "Un día muy productivo y feliz"

        // When
        viewModel.saveEntry(moodScore, reflection)
        testDispatcher.scheduler.advanceUntilIdle() // Esperar a que la corrutina termine

        // Then
        coVerify { dao.insert(any<MoodEntry>()) }
    }

    @Test
    fun `saveEntry does NOT insert entry when reflection is too short`() = runTest(testDispatcher) {
        // Given
        val moodScore = 5
        val reflection = "Corto" // Menos de 10 caracteres

        // When
        viewModel.saveEntry(moodScore, reflection)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify(exactly = 0) { dao.insert(any()) }
    }
}