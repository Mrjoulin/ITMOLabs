"use strict";

class Application {
	components = {
		x: document.getElementById("x-value"),
		y: document.getElementById("y-value"),
        r: document.getElementById("r-value"),
		submit: document.getElementById("submit-button")
	}

    resultsTable = document.getElementById("results-content");

    animations = new AnimationProcessor();

    constructor() {
		this.initXValue();
        this.setRValue(null);

        var x_buttons = document.getElementsByClassName("x"),
            r_checkboxes = document.getElementsByClassName("r"),
            form = document.getElementById("user-request");

		Array.from(x_buttons).forEach(btn =>
			btn.addEventListener("click", this.xButtonHandler.bind(this))
		);
        Array.from(r_checkboxes).forEach(cbx =>
            cbx.addEventListener("change", this.setRValue.bind(this))
        );

		form.addEventListener("submit", this.formSubmitHandler.bind(this));

        $('#close-error-button').click(this.hideErrorMessage);
	}

    initXValue(){
        const selectedXButton = document.getElementById(`x${this.components.x.value}`);

		if (selectedXButton) {
			selectedXButton.classList.add("choosen");
		} else {
			this.components.x.value = "";
		}
    }

    setRValue(e){
        if (!e) {
            this.components.r.value = "";
            return;
        }

        const selectedRCheckboxes = document.querySelectorAll('input[class="r"]:checked');

        this.components.r.value = e.target.value;
        selectedRCheckboxes.forEach(cbx => {
            if (cbx.id != e.target.id) { cbx.checked = false }
        });

        this.components.r.dispatchEvent(new Event('change'));
    }

	xButtonHandler(e) {
		const choosenButtons = document.getElementsByClassName("choosen");
		Array.from(choosenButtons).forEach(btn =>
			btn.classList.remove("choosen")
		);
		e.target.classList.add("choosen");

		this.components.x.value = e.target.textContent;
	}

    validateX(x) {
        const xValues = [-3, -2, -1, 0, 1, 2, 3, 4, 5];

        let parsedX = parseInt(x);
        if (isNaN(x.trim()) || isNaN(parsedX) || !xValues.includes(parsedX)) {
            this.showErrorMessage("Please, choose correct button");
            return null;
        }

        return parsedX
    }

    validateY(y) {
        const yMin = -5., yMax = 3.;

        let parsedY = parseFloat(y.replace(",", "."));
        if (isNaN(y.trim()) || isNaN(parsedY) || yMin >= parsedY || parsedY >= yMax) {
            this.showErrorMessage("Please, input correct Y value (from -5 to 3)");
            return null;
        }

        return parsedY;
    }

    validateR(r) {
        const rValues = [1., 1.5, 2., 2.5, 3.];

        let parsedR = parseFloat(r);
        if (isNaN(r.trim()) || isNaN(parsedR) || !rValues.includes(parsedR)) {
            this.showErrorMessage("Please, choose one checkbox!");
            return null;
        }

        return parsedR;
    }

	validateAndParse(x, y, r) {
		let parsedX, parsedY, parsedR;

        console.log(x, y, r);

        parsedX = this.validateX(x)
        if (parsedX == null) return [null, null, null]

		parsedY = this.validateY(y);
		if (parsedY == null) return [null, null, null]

		parsedR = this.validateR(r);
		if (parsedR == null) return [null, null, null]

		return [parsedX, parsedY, parsedR];
	}

	async formSubmitHandler(e) {
		e.preventDefault();
		this.components.submit.textContent = "Checking...";
		this.components.submit.disabled = true

		const [x, y, r] = this.validateAndParse(this.components.x.value, this.components.y.value, this.components.r.value);
		if (x !== null && y !== null && r !== null) {
			await this.makeRequest(x, y, r)
		}
		this.components.submit.disabled = false;
		this.components.submit.textContent = "Check";
	}

	async makeRequest(x, y, r) {
	    this.hideErrorMessage();

	    try {
            const response = await fetch("./controller?" + new URLSearchParams({x: x, y: y, r: r}));
            const json = await response.json();

            if (response.status == 200) {
                const hitData = json.result[0];

                if (!document.getElementById("disable-video").checked) {
                    await this.animations.shoot(x, y, r)
                }

                var data = [hitData.x, hitData.y, hitData.r, hitData.currentTime, hitData.executionTime, hitData.result];
                this.addTableResults(data);
                // Add dot to board
                create_point(hitData.x, hitData.y);
            } else {
                this.showErrorMessage(json.error);
            }

            this.setRequestsCounter(json.requestsCount);
        } catch (error) {
            console.log(error);
            this.showErrorMessage("Server unreachable :(\nTry again later");
        }
	}

    addTableResults(rowData) {
        var row = this.resultsTable.insertRow(0);

        // document.querySelectorAll('td[style="color: greenyellow;"]').forEach(cell => cell.removeAttribute("style"));
        // document.querySelectorAll('td[style="color: red;"]').forEach(cell => cell.removeAttribute("style"));

        rowData.forEach(cellData => {
            var cell = row.insertCell();

            if (cellData === "true") {
                cell.innerHTML = "area";
                cell.style = "color: greenyellow;";
            } else if (cellData === "false") {
                cell.innerHTML = "miss";
                cell.style = "color: red;";
            } else {
                cell.innerHTML = cellData;
            }
        });
    }

    async clearResults() {
        try {
            const response = await fetch("./controller?" + new URLSearchParams({session: "clear"}));
            const json = await response.json();

            if (response.status == 200) {
                // Remove results from table
                $("#results-table tbody tr").remove();
                // Recreate board
                board = initBoard();
                // Draw current figures
                if (this.components.r.value) {
                    this.components.r.dispatchEvent(new Event('change'));
                }
            } else {
                this.showErrorMessage(json.error);
            }

            this.setRequestsCounter(json.requestsCount);
        } catch (error) {
            console.log(error);
            this.showErrorMessage("Server unreachable :(\nTry again later");
        }
    }

    setRequestsCounter(newValue) {
        $("#requests-counter").text(newValue);
    }

    showErrorMessage(message) {
        $("#error-message-block").show();
        $("#error-message").text(message);
    }

    hideErrorMessage() {
        $("#error-message-block").hide();
    }
}

const app = new Application();