# Variant 17: XML -> CSV Среда
# With Libraries, without re
# ----------
# Matthew Ivanov P3115

import sys
import time
import xml.etree.ElementTree as ET

input_file = sys.argv[1] if len(sys.argv) >= 2 else "files/my_day.xml"
output_file = sys.argv[2] if len(sys.argv) >= 3 else "files/my_day_4.csv"
time_file = sys.argv[3] if len(sys.argv) >= 4 else "files/check_time.txt"


def process(root):
    # Get tbody node
    tbody = root.find("table").find("tgroup").find("tbody")

    headers = []
    lessons = []

    for lesson in tbody:
        lesson_info = []

        for current_part in lesson:
            for cur in current_part:
                # Set teg like "parent.child"
                tag = current_part.tag + "." + cur.tag
                # Set headers if they not set
                if tag not in headers:
                    headers.append(tag)

                text = cur.text.strip() if cur.text else ''

                # If there is comma in text, set quoters
                if "," in text:
                    text = '"' + text + '"'

                lesson_info.append(text)

        # Join all info comma separated and push to array
        lessons.append(",".join(lesson_info))

    # Generate output file

    text = ",".join(headers) + "\n"
    text += "\n".join(lessons)

    with open(output_file, "w") as f:
        f.write(text)


if __name__ == '__main__':
    start_time = time.time()

    for _ in range(10):
        tree = ET.parse(input_file)
        root = tree.getroot()

        process(root)

    with open(time_file, "a") as file:
        file.write(
            "---- Test Four (parsing to CSV) ----\n%s sec\n" % (
                time.time() - start_time
            )
        )
