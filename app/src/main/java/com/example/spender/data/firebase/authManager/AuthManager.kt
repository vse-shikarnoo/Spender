import com.example.spender.data.firebase.Result

interface AuthManager {
    suspend fun signIn(email: String, password: String): Result<Boolean>
    suspend fun signUp(email: String, password: String): Result<Boolean>
    suspend fun signOut(): Result<Boolean>
    suspend fun verifyEmail(): Result<Boolean>
    suspend fun isEmailVerified(): Result<Boolean>
    suspend fun resetPassword(): Result<Boolean>
}