import kotlin.math.pow

fun main() {
    val password = "MySecurePassword123!"
    val strengthAndCrackTime = getPasswordStrengthAndCrackTime(password)
    println("Password strength: ${strengthAndCrackTime.first}")
    println("Estimated time to crack: ${strengthAndCrackTime.second}")
}

fun getPasswordStrength(password: String): String {
    // Check password length
    if (password.length < 8) {
        return "Weak: Password should be at least 8 characters long."
    }

    // Check for uppercase, lowercase, digits, and special characters
    var hasUppercase = false
    var hasLowercase = false
    var hasDigit = false
    var hasSpecialChar = false

    for (char in password) {
        when {
            char.isUpperCase() -> hasUppercase = true
            char.isLowerCase() -> hasLowercase = true
            char.isDigit() -> hasDigit = true
            char.isWhitespace() || !char.isLetterOrDigit() -> hasSpecialChar = true
        }
    }

    // Evaluate password strength based on criteria
    val strength = when {
        !hasUppercase || !hasLowercase || !hasDigit || !hasSpecialChar -> "Weak: Password should include uppercase, lowercase, digits, and special characters."
        password.length < 12 -> "Moderate: Password length is good, but consider adding more characters."
        else -> "Strong: Password meets all criteria."
    }

    return strength
}

fun getPasswordStrength(password: String): String {
    // Check password strength criteria (same as previous example)
    // ...

    // Return password strength message
    // ...
}

fun estimateCrackTime(password: String): String {
    val possibleCharacters = 72  // Example: uppercase, lowercase, digits, and common special characters
    val averageTimePerGuess = 0.005  // Example: time in seconds for a system to try one password guess

    val passwordLength = password.length
    val combinations = possibleCharacters.toDouble().pow(passwordLength.toDouble())

    val secondsToCrack = combinations * averageTimePerGuess
    val crackTime = formatTime(secondsToCrack)

    return crackTime
}

fun formatTime(seconds: Double): String {
    val minute = 60
    val hour = minute * 60
    val day = hour * 24
    val year = day * 365

    return when {
        seconds < minute -> "${seconds.toInt()} seconds"
        seconds < hour -> "${(seconds / minute).toInt()} minutes"
        seconds < day -> "${(seconds / hour).toInt()} hours"
        seconds < year -> "${(seconds / day).toInt()} days"
        else -> "${(seconds / year).toInt()} years"
    }
}

