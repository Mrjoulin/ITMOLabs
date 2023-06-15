import os
import shutil
import logging
import argparse
import absl.logging

logger = logging.getLogger()
for handler in logger.handlers[:]:
    logger.removeHandler(handler)

if os.path.exists('./neuralgym_logs'):
    shutil.rmtree('./neuralgym_logs')

logging.root.removeHandler(absl.logging._absl_handler)
absl.logging._warn_preinit_stderr = False
logging.basicConfig(
    format='[%(asctime)s: %(filename)s:%(lineno)s - %(funcName)10s()]%(levelname)s:%(name)s:%(message)s',
    datefmt='%Y-%m-%d %H:%M:%S',
    level=logging.INFO
)

from src.routes import run_app

if __name__ == '__main__':
    parser = argparse.ArgumentParser('Server options')
    parser.add_argument("--host", default=None, help="Host server")
    parser.add_argument("--port", "-p", type=int, default=5500, help="Port server (default: 5500)")
    parser.add_argument("--cert-file", help="SSL certificate file (for HTTPS)")
    parser.add_argument("--key-file", help="SSL key file (for HTTPS)")
    args = parser.parse_args()

    run_app(port=args.port, host=args.host, cert_file=args.cert_file, key_file=args.key_file)
