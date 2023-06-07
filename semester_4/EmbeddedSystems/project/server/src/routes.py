import os
import ssl
import uuid
import asyncio
import requests
from aiohttp import web
from aiortc import RTCPeerConnection, RTCSessionDescription

# local modules
from .frame_render import *


ROOT = os.path.dirname(os.path.abspath(__file__))
pcs = set()


async def init(request):
    logging.info('Run init page from IP: %s' % request.remote)
    content = open(os.path.join(ROOT, "templates/test/index.html"), "r").read()
    return web.Response(content_type="text/html", text=content)


async def init_js(request):
    content = open(os.path.join(ROOT, "templates/test/src/client.js"), "r").read()
    return web.Response(content_type="application/javascript", text=content)


async def offer(request):
    params = await request.json()
    offer = RTCSessionDescription(sdp=params["sdp"], type=params["type"])

    pc = RTCPeerConnection()
    pc_id = "PeerConnection(%s)" % uuid.uuid4()
    pcs.add(pc)
    logging.info(pc_id + " " + "Created for %s" % request.remote)

    @pc.on("iceconnectionstatechange")
    async def on_iceconnectionstatechange():
        logging.info("ICE connection state is %s" % pc.iceConnectionState)
        if pc.iceConnectionState == "failed" or pc.iceConnectionState == "closed":
            await pc.close()
            pcs.discard(pc)

    @pc.on("track")
    def on_track(track):
        logging.info("Track {kind} received".format(kind=track.kind))
        local_video = VideoTransformTrack(track)
        pc.addTrack(local_video)

        @pc.on("datachannel")
        def on_datachannel(channel):
            logging.info('Create Data Channel')

            @channel.on("message")
            def on_message(message):
                if isinstance(message, str) and message.startswith("check"):
                    response = {
                        "fps": local_video.get_fps(),
                        "num_people": local_video.get_average_people_num()
                    }

                    channel.send(json.dumps(response))

    # handle offer
    await pc.setRemoteDescription(offer)

    # send answer
    answer = await pc.createAnswer()
    await pc.setLocalDescription(answer)

    return web.Response(
        content_type="application/json",
        headers={
            "Access-Control-Allow-Origin": "*"
        },
        text=json.dumps(
            {"sdp": pc.localDescription.sdp, "type": pc.localDescription.type}
        ),
    )


async def webcam(request):
    pc = RTCPeerConnection()
    pc_id = "PeerConnection(%s)" % uuid.uuid4()
    logging.info(pc_id + " " + "Created for %s" % request.remote)

    @pc.on("iceconnectionstatechange")
    async def on_iceconnectionstatechange():
        logging.info("ICE connection state is %s" % pc.iceConnectionState)
        if pc.iceConnectionState == "failed":
            await pc.close()

    @pc.on("track")
    async def on_track(track: VideoStreamTrack):
        logging.info("Track {kind} received".format(kind=track.kind))
        await StreamDetection(track).process()

    pc.addTransceiver('video', "recvonly")

    rtc_offer = await pc.createOffer()
    await pc.setLocalDescription(rtc_offer)

    wait_until = time.time() + 10
    while time.time() < wait_until and pc.iceGatheringState != "complete":
        time.sleep(0.01)

    if pc.iceGatheringState != "complete":
        logging.error("Ice Gathering State not completed!")
        return web.Response(status=500, text="Ice Gathering State not completed!")

    remote_offer = requests.post(
        "http://0.0.0.0:6500/offer",
        json={
            "sdp": rtc_offer.sdp,
            "type": rtc_offer.type
        }
    ).json()

    await pc.setRemoteDescription(RTCSessionDescription(sdp=remote_offer["sdp"], type=remote_offer["type"]))

    async def detection_time_end():
        await asyncio.sleep(CONFIG["run_config"]["detection_time"])

        logging.info("Close connection!")
        await pc.close()

    asyncio.ensure_future(detection_time_end())

    return web.Response(status=200, text="OK")


async def on_shutdown(app):
    # close peer connections
    coros = [pc.close() for pc in pcs]
    await asyncio.gather(*coros)
    pcs.clear()


def run_app(port=5000, host=None, cert_file=None, key_file=None):
    app = web.Application()
    app.on_shutdown.append(on_shutdown)
    app.router.add_get('/', init)
    app.router.add_get('/src/client.js', init_js)
    app.router.add_post('/offer', offer)
    app.router.add_get('/webcam', webcam)

    if cert_file is not None and key_file is not None:
        ssl_context = ssl.create_default_context(ssl.Purpose.CLIENT_AUTH)
        ssl_context.load_cert_chain(cert_file, key_file)
    else:
        ssl_context = None

    web.run_app(app, access_log=None, port=port, ssl_context=ssl_context, host=host)

