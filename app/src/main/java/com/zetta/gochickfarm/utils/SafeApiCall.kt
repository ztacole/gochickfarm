import com.zetta.gochickfarm.network.BaseMessageResponse
import io.ktor.client.call.*
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

suspend inline fun <reified T> safeApiCall(
    crossinline apiCall: suspend () -> HttpResponse
): Result<T> {
    return try {
        val response = apiCall()

        if (response.status.isSuccess()) {
            val data = response.body<T>()
            Result.success(data)
        } else {
            val errorBody = try {
                response.body<BaseMessageResponse>().message
            } catch (_: Exception) {
                "Server error: ${response.status}"
            }
            Result.failure(Exception(errorBody))
        }
    } catch (e: Exception) {
        // Handle exceptions like network errors or parsing errors
        Result.failure(e)
    }
}
