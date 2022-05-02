package network

val INVALID_USER_CREDENTIALS_RESPONSE = Response(success = false, message = "Incorrect username or password!")
val USER_NOT_FOUND_RESPONSE = Response(success = false, message = "User not found!")
val DB_PROBLEM_RESPONSE = Response(success = false, message = "There are some problems with the database, please try again later!")
val HAS_NO_ACCESS_RESPONSE = Response(success = false, message = "You don't have access to this entity")
