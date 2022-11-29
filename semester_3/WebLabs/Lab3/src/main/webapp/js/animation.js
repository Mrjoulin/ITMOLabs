"use strict";

class AnimationProcessor {
	x = 0;
	y = 0;
	video = document.getElementById("bomb-video");
	bomb = document.getElementById("bomb");
	tmp = document.getElementById("tmp");
	bombCtx = this.bomb.getContext("2d");
	tmpCtx = this.tmp.getContext("2d");

	resizeCanvases() {
		const iw = window.innerWidth + 2 * Math.abs(this.x),
			vw = this.video.videoWidth,
			ih = window.innerHeight + Math.abs(2 * this.y),
			vh = this.video.videoHeight;
		const sx = iw / vw;
		const sy = ih / vh;
		const left = Math.min(2 * this.x, 0);
		const top = Math.min(-2 * this.y, 0);
		this.bomb.style.transform = `scale(${sx}, ${sy})`;
		this.bomb.style.left = `${left + (iw - vw) / 2}`;
		this.bomb.style.top = `${top + (ih - vh) / 2}`;
	}

	nextFrame() {
		if (this.video.paused || this.video.ended) {
			this.clearVideo();
		} else {
			this.resizeCanvases();
			this.drawVideoFrame();
		}
		window.requestAnimationFrame(this.nextFrame.bind(this));
	}

	clearVideo() {
		this.bombCtx.clearRect(0, 0, this.video.videoWidth, this.video.videoHeight);
	}

	drawVideoFrame() {
		let [w, h] = [this.video.videoWidth, this.video.videoHeight];
		this.tmpCtx.drawImage(this.video, 0, 0, w, h);

		const frame = this.tmpCtx.getImageData(0, 0, w, h);
		const length = frame.data.length;
		const data = frame.data;

		for (let i = 0; i < length; i += 4) {
			const red = data[i + 0];
			const green = data[i + 1];
			const blue = data[i + 2];
			if ((blue + red < green)) {
				data[i + 1] = 0;
				data[i + 3] = (255 - green);
			}
		}
		this.bombCtx.putImageData(frame, 0, 0);
	}

	async shoot(x, y, r) {
		this.x = 80 * x / r;
		this.y = 80 * y / r;
		await this.video.play();
		window.requestAnimationFrame(this.nextFrame.bind(this));
		await new Promise(res => setTimeout(res, 900));
	}
}