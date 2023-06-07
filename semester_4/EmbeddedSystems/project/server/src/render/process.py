import numpy as np
import json
import cv2
import os


CONFIG_PATH = './config.json'
with open(CONFIG_PATH, 'r') as f:
    CONFIG = json.load(f)


# Configurations to save video
OUTPUT_FILENAME = 'meow.avi'
FRAME_SIZE = (1280, 720)
FPS = 15

# Model configuration
ROOT = os.path.dirname(os.path.abspath(__file__))
MODEL_PATH = os.path.join(ROOT, CONFIG["model_config"]["input_dir"], CONFIG["model_config"]["model_name"])
RESIZED_DIMENSIONS = tuple(CONFIG["run_config"]["frame_size"])
IMG_NORM_RATIO = CONFIG["run_config"]["img_norm_ratio"]
WARMUP_IMG = os.path.join(ROOT, "person.jpeg")

# Detection configuration
CONFIDENCE_LEVEL = CONFIG["run_config"]["percent_detection"]
PERSON_CLASS_ID = CONFIG["run_config"]["target_class_id"]

# Boxes configuration
BOXES_COLOR = (55, 235, 55)
LABELS_OFFSET = 15
MEAN_PEOPLE_FOR_SECONDS = 5

# Load the pre-trained neural network
neural_network = cv2.dnn.readNetFromCaffe(f'{MODEL_PATH}.prototxt.txt', f'{MODEL_PATH}.caffemodel')


class ProcessFrame:
    def __init__(self):
        self.warmup()

    def warmup(self):
        img = cv2.imread(WARMUP_IMG)
        num_people, _ = self.process(img)

        assert num_people == 1

    def process(self, frame):
        frame_blob = cv2.dnn.blobFromImage(
            cv2.resize(frame, RESIZED_DIMENSIONS),
            IMG_NORM_RATIO, RESIZED_DIMENSIONS, 127.5
        )
        # Set the input for the neural network
        neural_network.setInput(frame_blob)
        # Predict the objects in the image
        detected_objects = neural_network.forward()[0, 0]
        detected_objects = detected_objects[
            (detected_objects[:, 1] == PERSON_CLASS_ID) & (detected_objects[:, 2] > CONFIDENCE_LEVEL)
        ]

        return len(detected_objects), detected_objects

    def draw_boxes(self, frame, detected_objects):
        boxes = (detected_objects[:, 3:7] * np.array(frame.shape[1::-1] * 2)).astype("int")

        for i, box in enumerate(boxes):
            # Draw box
            cv2.rectangle(frame, box[:2], box[2:], BOXES_COLOR, 2)
            # Draw label
            label = f"Person: {detected_objects[i, 2] * 100:.2f}%"
            label_y = box[1] - LABELS_OFFSET if box[1] > LABELS_OFFSET * 2 else box[1] + LABELS_OFFSET
            cv2.putText(frame, label, (box[0], label_y), cv2.FONT_HERSHEY_SIMPLEX, 0.5, BOXES_COLOR, 2)


FRAME_PROCESS = ProcessFrame()


def process_frame(frame):
    # Create a blob for neural network
    frame_blob = cv2.dnn.blobFromImage(
        cv2.resize(frame, RESIZED_DIMENSIONS),
        IMG_NORM_RATIO, RESIZED_DIMENSIONS, 127.5
    )
    # Set the input for the neural network
    neural_network.setInput(frame_blob)
    # Predict the objects in the image
    detected_objects = neural_network.forward()[0, 0]
    detected_objects = detected_objects[
        (detected_objects[:, 1] == PERSON_CLASS_ID) & (detected_objects[:, 2] > CONFIDENCE_LEVEL)
        ]

    # Draw boxes
    boxes = (detected_objects[:, 3:7] * np.array(frame.shape[1::-1] * 2)).astype("int")

    for i, box in enumerate(boxes):
        # Draw box
        cv2.rectangle(frame, box[:2], box[2:], BOXES_COLOR, 2)
        # Draw label
        label = f"Person: {detected_objects[i, 2] * 100:.2f}%"
        label_y = box[1] - LABELS_OFFSET if box[1] > LABELS_OFFSET * 2 else box[1] + LABELS_OFFSET
        cv2.putText(frame, label, (box[0], label_y), cv2.FONT_HERSHEY_SIMPLEX, 0.5, BOXES_COLOR, 2)

    num_people = len(detected_objects)

    count_text = f"People found: {num_people}."
    cv2.putText(
        frame, count_text, (LABELS_OFFSET, frame.shape[0] - LABELS_OFFSET),
        cv2.FONT_HERSHEY_SIMPLEX, 0.8, 0, 2, cv2.LINE_AA
    )

    return frame, num_people


def process(filename: str = None):
    # Load video if given, else use camera
    cap = cv2.VideoCapture(filename or 0)

    # Create a VideoWriter object so we can save the video output
    result = cv2.VideoWriter(OUTPUT_FILENAME, cv2.VideoWriter_fourcc(*'MJPG'), FPS, FRAME_SIZE)

    count_people_res = []

    # Process the video
    while cap.isOpened():
        # Read video frame
        success, frame = cap.read()

        if not success:
            break

        frame, num_people = process_frame(frame)

        count_people_res = count_people_res[1 - FPS * MEAN_PEOPLE_FOR_SECONDS:] + [num_people]
        count_text = f"People found: {num_people}. " \
                     f"Mean for {MEAN_PEOPLE_FOR_SECONDS} sec: {round(np.mean(count_people_res), 2)}"

        # Write result to output file and show
        result.write(frame)
        cv2.imshow('Detection lol', frame)

        if cv2.waitKey(10) & 0xFF == ord('q'):
            break

    # Stop when the video is finished
    cap.release()
    # Release the video recording
    result.release()

    cv2.destroyAllWindows()


if __name__ == '__main__':
    process()
