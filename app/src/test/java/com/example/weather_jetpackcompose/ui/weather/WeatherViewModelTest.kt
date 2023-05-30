import android.content.Context
import com.example.weather_jetpackcompose.data.model.errorhandling.ResultOf
import com.example.weather_jetpackcompose.data.model.location.GeoModel
import com.example.weather_jetpackcompose.data.model.weather.CloudsModel
import com.example.weather_jetpackcompose.data.model.weather.CoordModel
import com.example.weather_jetpackcompose.data.model.weather.MainModel
import com.example.weather_jetpackcompose.data.model.weather.SysModel
import com.example.weather_jetpackcompose.data.model.weather.WeatherModel
import com.example.weather_jetpackcompose.data.model.weather.WeatherModelModel
import com.example.weather_jetpackcompose.data.model.weather.WindModel
import com.example.weather_jetpackcompose.data.repository.Repository
import com.example.weather_jetpackcompose.ui.weather.WeatherViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

@ExperimentalCoroutinesApi
class WeatherViewModelTest {

    @Mock
    private lateinit var repository: Repository

    @Mock
    private lateinit var context: Context

    private lateinit var viewModel: WeatherViewModel
    // Add a companion object to provide the TestCoroutineDispatcher
    companion object {
        @ObsoleteCoroutinesApi
        @JvmField

        val testDispatcher = TestCoroutineDispatcher()
    }

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        viewModel = WeatherViewModel(repository)

    }


    @Test
    fun `getWeather - success`() = runBlockingTest {
        // Given
        Dispatchers.setMain(TestCoroutineDispatcher()) // Mock the Main dispatcher
        val city = "London"
        val weatherModel = WeatherModelModel(
            base = "",
            clouds = CloudsModel(all = 75),
            cod = 200,
            coord = CoordModel(lat = 51.5085, lon = -0.1257),
            dt = 1685036251,
            id = 2643743,
            main = MainModel(
                feelsLike = 289.21,
                humidity = 60,
                pressure = 1029,
                temp = 289.91,
                tempMax = 291.55,
                tempMin = 287.51
            ),
            name = "london",
            sys = SysModel(
                country = "GB",
                id = 268730,
                sunrise = 1684986974,
                sunset = 1685044733,
                type = 2
            ),
            timezone = 3600,
            visibility = 10000,
            weather = listOf(
                WeatherModel(
                    description = "broken clouds",
                    icon = "04d",
                    id = 803,
                    main = "Clouds"
                )
            ),
            wind = WindModel(speed = 4.12, deg = 50)
        )
        `when`(repository.getWeather(city)).thenReturn(weatherModel)

        // When
        viewModel.getWeather(city, context)

        // Then
        assertEquals(ResultOf.Success(weatherModel), viewModel.weatherDetails.value)
    }

    @Test
    fun `getWeather - IO exception`() = runBlockingTest {
        val cityName = "London"
        val expectedException = IOException("Network error")
        `when`(repository.getWeather(cityName)).thenAnswer { throw expectedException }

        // Initialize the Main dispatcher
        Dispatchers.setMain(testDispatcher)

        // Act
        viewModel.getWeather(cityName, context)

        // Assert
        val result = viewModel.weatherDetails.first()
        assertTrue(result is ResultOf.Failure)
        assertEquals(expectedException, (result as ResultOf.Failure).throwable)

        // Reset the Main dispatcher
        Dispatchers.resetMain()
    }

    @Test
    fun `getWeather - HTTP exception`() = runBlockingTest {
        // Arrange
        val cityName = "London"
        val expectedException = HttpException(Response.error<Any>(404, "".toResponseBody()))
        `when`(repository.getWeather(cityName)).thenThrow(expectedException)

        // Initialize the Main dispatcher
        Dispatchers.setMain(testDispatcher)

        // Act
        viewModel.getWeather(cityName, context)

        // Assert
        val result = viewModel.weatherDetails.first()
        assertTrue(result is ResultOf.Failure)
        assertEquals(expectedException, (result as ResultOf.Failure).throwable)

        // Reset the Main dispatcher
        Dispatchers.resetMain()
    }

    @Test
    fun `getWeather - unknown exception`() = runBlockingTest {
        // Arrange
        val cityName = "London"
        val expectedException = RuntimeException("Unknown error")
        `when`(repository.getWeather(cityName)).thenThrow(expectedException)

        // Initialize the Main dispatcher
        Dispatchers.setMain(testDispatcher)

        // Act
        viewModel.getWeather(cityName, context)

        // Assert
        val result = viewModel.weatherDetails.first()
        assertTrue(result is ResultOf.Failure)
        assertEquals(expectedException, (result as ResultOf.Failure).throwable)

        // Reset the Main dispatcher
        Dispatchers.resetMain()
    }





}
