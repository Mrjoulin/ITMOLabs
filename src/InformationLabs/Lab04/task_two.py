# Variant 17: XML -> YAML Среда
# With Libraries, without re
# ----------
# Matthew Ivanov P3115

import sys
import time
from collections import Counter
import xml.etree.ElementTree as ET

num_spaces_in_tab = 2
input_file = sys.argv[1] if len(sys.argv) >= 2 else "files/my_day.xml"
output_file = sys.argv[2] if len(sys.argv) >= 3 else "files/my_day_2.yaml"
time_file = sys.argv[3] if len(sys.argv) >= 4 else "files/check_time.txt"

XML_NODE_CONTENT = "_xml_node_content"


def process(node, result: list, num_spaces=0):
    # Nodes with both content AND nested nodes or attributes
    # have no valid yaml mapping. Add 'content' node for that case
    node_attrs = node.attrib
    children = list(node)
    content = node.text.strip() if node.text else ''

    if node.tag.startswith("~"):
        result.append(" " * num_spaces + "-")
        node.tag = node.tag[1:]

    if content:
        if not node_attrs and not children:
            # Write as just a name value, nothing else nested
            result.append(
                "%s%s: %s" % (
                    " " * num_spaces, node.tag, content or ''
                )
            )

            return result
        else:
            node_attrs[XML_NODE_CONTENT] = node.text

    result.append(" " * num_spaces + node.tag + ":")

    # Indicate difference node attributes and nested nodes
    num_spaces += num_spaces_in_tab

    for key, value in node_attrs.items():
        result.append(
            "%s%s%s: %s" % (
                " " * num_spaces,
                "_" if key != XML_NODE_CONTENT else "",
                key, value
            )
        )

    child_tags = Counter([child.tag for child in children])

    # Write nested nodes
    for child in children:
        if child_tags[child.tag] > 1:
            child.tag = "~" + child.tag

        result = process(child, result, num_spaces)

    return result


if __name__ == '__main__':
    start_time = time.time()

    for _ in range(10):
        tree = ET.parse(input_file)
        result = process(tree.getroot(), [])

        with open(output_file, "w") as yaml_file:
            yaml_file.write("\n".join(result))

    with open(time_file, "a") as file:
        file.write(
            "---- Test Two ----\n%s sec\n" % (
                time.time() - start_time
            )
        )
