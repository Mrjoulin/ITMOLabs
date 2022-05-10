package commands.interfaces

import entities.Route
import entities.User
import manager.interfaces.CollectionManagerInterface
import network.Request
import network.Response
import network.USER_NOT_FOUND_RESPONSE
import utils.CreateEntityFromMap
import utils.logger
import java.lang.Exception
import java.util.*

/**
 * Interface that describes executable command.
 * Also implements several methods that can be helpful for heirs.
 *
 * @see CollectionManagerInterface
 * @see Route
 * @see User
 * @see Request
 * @see Response
 *
 * @author Matthew I.
 */
abstract class Command(private val collectionManager: CollectionManagerInterface<Route>) {
    /**
     * Execute command with given args
     *
     * @param request Request from client
     * @return Success of execution
     *
     * @author Matthew I.
     */
    abstract fun execute(request: Request) : Response

    fun getUser(request: Request): User? {
        if (request.token.isEmpty()) return null

        val user = collectionManager.getDBWorker().getUserByToken(request.token) ?: return null

        if (user.tokenExpires < Date()) return null

        return user
    }

    fun getEntityFromRequest(request: Request, previousObject: Route? = null, user: User? = null): Pair<Boolean, Any> {
        val curUser = user ?: (getUser(request) ?: return Pair(false, USER_NOT_FOUND_RESPONSE))

        if (request.entityObjectMap == null) {
            return Pair(
                false, Response(
                    success = true, message = "Object is needed",
                    isObjectNeeded = true, routeToUpdate = previousObject
                )
            )
        }

        try {
            val routeStatus = CreateEntityFromMap(collectionManager, curUser).getObjectInstanceFromMap(
                Route::class.java, request.entityObjectMap!!
            )

            if (routeStatus.first) {
                val route = routeStatus.second as Route

                return Pair(true, route)
            }

            return Pair(false, Response(success = false, message = routeStatus.second as String))
        } catch (e: Exception) {
            logger.error("Got error while creating entity object from map: ", e)

            return Pair(false, Response(success = false, message = "Can't create object from given map"))
        }
    }
}
