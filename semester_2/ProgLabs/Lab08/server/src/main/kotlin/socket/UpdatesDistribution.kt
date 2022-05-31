package socket

import entities.Route
import manager.interfaces.CollectionManagerInterface
import network.ObjectSerializer
import network.Request
import network.Response
import network.UpdateNotification
import utils.logger
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.SocketAddress

class UpdatesDistribution (
    private val datagramSocket: DatagramSocket,
    private val updateListenersAddresses: ArrayList<SocketAddress>,
    private val collectionManager: CollectionManagerInterface<Route>
): Runnable {
    override fun run() {
        val collectionUpdate = collectionManager.getCollectionUpdate() ?: return

        val data = ObjectSerializer().toByteArray(collectionUpdate)
        var errors = 0

        for (clientAddress in updateListenersAddresses) {
            try {
                val packet = DatagramPacket(data, data.size, clientAddress)

                datagramSocket.send(packet)
            } catch (e: IOException) {
                errors += 1
                logger.error("Can't send update to client update listener: $clientAddress")
            }
        }

        logger.info("Update send to ${updateListenersAddresses.size - errors} clients")

        collectionManager.resetCollectionUpdate()
    }
}