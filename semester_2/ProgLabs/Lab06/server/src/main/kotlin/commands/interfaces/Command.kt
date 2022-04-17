package commands.interfaces

import entities.Route
import manager.interfaces.CollectionManagerInterface
import network.Request
import network.Response
import utils.CreateEntityFromMap
import utils.logger
import java.lang.Exception

/**
 * Interface that describes executable command
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

    fun getEntityFromRequest(request: Request, previousObject: Route? = null): Pair<Boolean, Any> {
        if (request.entityObjectMap == null) {
            return Pair(
                false,
                Response(
                    success = true,
                    message = "Object is needed",
                    isObjectNeeded = true,
                    routeToUpdate = previousObject
                )
            )
        }


        try {
            val createEntityFromMap = CreateEntityFromMap(collectionManager)

            val routeStatus = createEntityFromMap.getObjectInstanceFromMap(
                Route::class.java, request.entityObjectMap!!
            )

            if (routeStatus.first) {
                val route = routeStatus.second as Route

                return Pair(true, route)
            }

            return Pair(
                false,
                Response(
                    success = false,
                    message = routeStatus.second as String
                )
            )
        } catch (e: Exception) {
            logger.error("Got error while creating entity object from map: ", e)

            return Pair(
                false,
                Response(
                    success = false,
                    message = "Can't create object from given map"
                )
            )
        }
    }
}
