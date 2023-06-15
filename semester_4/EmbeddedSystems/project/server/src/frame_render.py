import cv2
import time
import json
import logging
import numpy as np
from av import VideoFrame
from aiortc import VideoStreamTrack
from aiortc.mediastreams import MediaStreamError

from .render.process import FRAME_PROCESS

CONFIG_PATH = './config.json'
with open(CONFIG_PATH, 'r') as f:
    CONFIG = json.load(f)
    logging.info('Configuration project:\n' + str(json.dumps(CONFIG, sort_keys=True, indent=4)))

MAX_FPS = 30


class VideoTransformTrack(VideoStreamTrack):
    def __init__(self, track):
        super().__init__()
        self.track = track
        self.transform = StreamDetection(track)

    async def recv(self):
        frame = await self.track.recv()

        img = self.transform.detect(frame)

        if img is None:
            return frame

        new_frame = VideoFrame.from_ndarray(img, format="bgr24")
        new_frame.pts = frame.pts
        new_frame.time_base = frame.time_base

        return new_frame

    def get_fps(self):
        return self.transform.get_fps()

    def get_average_people_num(self):
        return self.transform.get_mode_people_num()


class StreamDetection:
    def __init__(self, track):
        self.track = track
        self.render = FRAME_PROCESS
        self.args = CONFIG["run_config"]
        self.frame_size = tuple(self.args['frame_size'])  # [<width>, <height>]
        self.input_fps = 30
        self.iteration = 0

        self.frames_time = [2 / MAX_FPS]
        self.people_num = []
        self.last_timestamp = 0

        self.is_working = True

    async def process(self):
        try:
            while self.is_working:
                frame = await self.track.recv()
                self.detect(frame)
        except MediaStreamError:
            logging.info("Stream ended!")
            self.is_working = False

    def detect(self, frame):
        start_time = time.time()

        self.iteration += 1
        if self.iteration % self.get_skip_frames_rate() > 0:
            logging.debug('Skip frame in %.5f sec' % (time.time() - start_time))
            return None

        img = frame.to_ndarray(format="yuv420p")
        img = cv2.cvtColor(img, cv2.COLOR_YUV420P2RGB)

        num_people, detected_objects = self.render.process(frame=img)
        self.render.draw_boxes(img, detected_objects)

        render_time = (time.time() - start_time)
        self.frames_time = self.frames_time[-MAX_FPS:] + [render_time]

        offset = self.get_fps() * self.args['seconds_to_find_mean']
        self.people_num = self.people_num[1 - offset:] + [num_people]

        logging.debug('Process frame in %.5f sec' % (time.time() - start_time))

        if time.time() - self.last_timestamp > 1:
            self.last_timestamp = time.time()
            self.log_average()

        return img

    def log_average(self):
        logging.info(
            'Average time: %s (FPS %s); Most common people number: %s' % (
                self.get_average_time_render(),
                self.get_fps(),
                self.get_mode_people_num(),
            )
        )

    def clear_people(self):
        self.people_num = []

    def get_average_time_render(self):
        return np.mean(self.frames_time)

    def get_fps(self):
        return min(max(int(1 / self.get_average_time_render() - 0.5), 1), MAX_FPS)

    def get_mode_people_num(self):
        return max(set(self.people_num), key=self.people_num.count) if self.people_num else 0

    def get_skip_frames_rate(self):
        return int(self.input_fps / self.get_fps()) + 1
