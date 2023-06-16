import os
import ssl
import uuid
import asyncio
import requests
import aiohttp_cors
from aiohttp import web
from aiortc import RTCPeerConnection, RTCSessionDescription

# local modules
from .frame_render import *


ROOT = os.path.dirname(os.path.abspath(__file__))

# uuid - StreamDetection
END_TIME = time.time()
CURRENT_PROCESS = None
pcs = set()


async def init(request):
    logging.info('Run init page from IP: %s' % request.remote)
    content = open(os.path.join(ROOT, "templates/test/index.html"), "r").read()
    return web.Response(content_type="text/html", text=content)


async def init_js(request):
    content = open(os.path.join(ROOT, "templates/test/src/client.js"), "r").read()
    return web.Response(content_type="application/javascript", text=content)


# Offer needed only for testing

async def offer(request):
    params = await request.json()
    offer = RTCSessionDescription(sdp=params["sdp"], type=params["type"])

    pc = RTCPeerConnection()
    pc_id = "PeerConnection(%s)" % uuid.uuid4()
    logging.info(pc_id + " " + "Created for %s" % request.remote)

    @pc.on("iceconnectionstatechange")
    async def on_iceconnectionstatechange():
        logging.info("ICE connection state is %s" % pc.iceConnectionState)
        if pc.iceConnectionState == "failed" or pc.iceConnectionState == "closed":
            await pc.close()

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


# Main method

async def create_new_connection():
    pc = RTCPeerConnection()
    pcs.add(pc)

    # Event listeners

    @pc.on("iceconnectionstatechange")
    async def on_iceconnectionstatechange():
        logging.info("ICE connection state is %s" % pc.iceConnectionState)
        if pc.iceConnectionState == "failed":
            await pc.close()
            pcs.discard(pc)

    @pc.on("track")
    async def on_track(track: VideoStreamTrack):
        global CURRENT_PROCESS

        logging.info("Track {kind} received".format(kind=track.kind))
        CURRENT_PROCESS = StreamDetection(track)
        await CURRENT_PROCESS.process()

    # Create offer and wait until gathering state is complete

    pc.addTransceiver('video', "recvonly")

    rtc_offer = await pc.createOffer()
    await pc.setLocalDescription(rtc_offer)

    wait_until = time.time() + 10
    while time.time() < wait_until and pc.iceGatheringState != "complete":
        time.sleep(0.01)

    if pc.iceGatheringState != "complete":
        logging.error("Ice Gathering State not completed!")
        return web.Response(status=500, text="Ice Gathering State not completed!")

    # Send remove offer to camera

    remote_offer = requests.post(
        f"http://{CONFIG['connection_info']['webcam_url']}:{CONFIG['connection_info']['webcam_port']}/offer",
        json={
            "sdp": rtc_offer.sdp,
            "type": rtc_offer.type
        }
    ).json()

    await pc.setRemoteDescription(RTCSessionDescription(sdp=remote_offer["sdp"], type=remote_offer["type"]))

    # Run async detection time end function (close connection)

    async def detection_time_end():
        global END_TIME

        while time.time() < END_TIME:
            await asyncio.sleep(1)

        logging.info("Close connection!")
        CURRENT_PROCESS.is_working = False
        await pc.close()

    asyncio.ensure_future(detection_time_end())


async def start_counting(request):
    global END_TIME, CURRENT_PROCESS

    END_TIME = round(time.time()) + CONFIG["run_config"]["detection_time"]

    if CURRENT_PROCESS is not None and CURRENT_PROCESS.is_working:
        status = "Use previous"
        CURRENT_PROCESS.clear_people()
    else:
        status = "Create new"
        asyncio.ensure_future(create_new_connection())

    logging.info("Timestamp %s %s for %s" % (END_TIME, status, request.remote))

    return web.Response(
        content_type="application/json",
        text=str(END_TIME)
    )


async def get_people_count(request):
    global END_TIME, CURRENT_PROCESS

    params = await request.json()

    logging.info("New get people count request: %s" % params)

    if 'id' not in params or not isinstance(params['id'], (int, float)):
        return web.Response(status=400, text="Incorrect params")

    if params['id'] < END_TIME:
        return web.Response(status=403, text="Processing")

    count = CURRENT_PROCESS.get_mode_people_num()
    logging.info("Return count: %s" % count)

    return web.Response(
        content_type="application/json",
        text=str(count)
    )


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
    app.router.add_get('/count', start_counting)
    app.router.add_post('/count', get_people_count)

    cors = aiohttp_cors.setup(app, defaults={
        "*": aiohttp_cors.ResourceOptions(
            allow_credentials=True,
            expose_headers="*",
            allow_headers="*"
        )
    })

    for route in list(app.router.routes()):
        cors.add(route)

    if cert_file is not None and key_file is not None:
        ssl_context = ssl.create_default_context(ssl.Purpose.CLIENT_AUTH)
        ssl_context.load_cert_chain(cert_file, key_file)
    else:
        ssl_context = None

    web.run_app(app, access_log=None, port=port, ssl_context=ssl_context, host=host)

