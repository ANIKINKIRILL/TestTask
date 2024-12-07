package com.anikinkirill.tapyou

import android.os.Looper
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.anikinkirill.tapyou.domain.Resource
import com.anikinkirill.tapyou.domain.Text
import com.anikinkirill.tapyou.domain.model.TapYouPoints
import com.anikinkirill.tapyou.domain.usecase.GetTapYouPointsUseCase
import com.anikinkirill.tapyou.presentation.chart.TapYouPointsChartViewModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class TapYouPointsChartViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val scheduler = TestCoroutineScheduler()
    private val testDispatcher = UnconfinedTestDispatcher(scheduler)

    private val useCase = mockk<GetTapYouPointsUseCase>()
    private val viewModel = TapYouPointsChartViewModel(useCase = useCase)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Looper::class)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test load points success`() = runTest {
        scheduler.advanceUntilIdle()

        coEvery {
            useCase.execute(amount = AMOUNT)
        } returns SUCCESS_RESULT

        viewModel.loadPoints(amount = AMOUNT)

        assert(viewModel.firstOrNull()?.points?.size == AMOUNT)
        assert(viewModel.firstOrNull()?.isLoading == false)
        assert(viewModel.firstOrNull()?.errorState == null)
    }

    @Test
    fun `test load points error`() = runTest {
        scheduler.advanceUntilIdle()

        coEvery {
            useCase.execute(amount = -1)
        } returns ERROR_RESULT

        viewModel.loadPoints(amount = -1)

        assert(viewModel.firstOrNull()?.points.isNullOrEmpty())
        assert(viewModel.firstOrNull()?.isLoading == false)
        assert(viewModel.firstOrNull()?.errorState != null)
    }

    private companion object {
        const val AMOUNT = 10
        val SUCCESS_RESULT = Resource.Success(
            data = List(AMOUNT) {
                TapYouPoints(x = 1.0, y = 1.0)
            }
        )

        val ERROR_RESULT = Resource.Error<List<TapYouPoints>>(
            data = null,
            message = Text.simple("Error message")
        )
    }
}