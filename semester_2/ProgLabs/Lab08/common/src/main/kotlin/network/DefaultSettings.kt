package network

import java.util.regex.Pattern

// Default setting
const val DEFAULT_PACKAGE_SIZE = 4096
const val DEFAULT_SERVER_PORT = 5000
const val SERVER_TIMEOUT_SEC = 5
const val DEFAULT_DATABASE_LOGIN = "s335105"
const val DEFAULT_DATABASE_PASSWORD = "password"
const val DEFAULT_DATABASE_HOST = "localhost:5432"
const val DEFAULT_DATABASE_NAME = "studs"

// Environment variables names
const val HOST_ENV_NAME = "HOST"
const val PORT_ENV_NAME = "PORT"
const val TOKEN_ENV_NAME = "CLIENT_TOKEN"
const val DATABASE_LOGIN_ENV = "DB_LOGIN"
const val DATABASE_PASSWORD_ENV = "DB_PASSWORD"
const val DATABASE_HOST_ENV = "DB_HOST"
const val DATABASE_NAME_ENV = "DB_NAME"

// Authorization and user credentials
val AUTHORIZATION_COMMANDS = arrayOf("login", "sign_up")
const val MIN_USERNAME_LENGTH = 4
const val MAX_USERNAME_LENGTH = 40
const val MIN_PASSWORD_LENGTH = 6
const val MAX_PASSWORD_LENGTH = 60
val CORRECT_LOGIN_RE: Pattern = Pattern.compile("^[a-zA-Z0-9_.]{$MIN_USERNAME_LENGTH,$MAX_USERNAME_LENGTH}$")
val CORRECT_PASSWORD_RE: Pattern = Pattern.compile("[a-zA-Z0-9!#$%_.,?:;*]{$MIN_PASSWORD_LENGTH,$MAX_PASSWORD_LENGTH}")

const val DECODE_TOKEN_LENGTH = 128
const val TOKEN_EXPIRES_MILS: Long = 7 * 24 * 60 * 60 * 1000 // week
