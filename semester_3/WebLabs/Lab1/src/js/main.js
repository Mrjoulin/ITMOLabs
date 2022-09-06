"use strict";

const resultsDataKey = "results";

class Application {
	components = {
		x: document.getElementById("x-value"),
		y: document.getElementById("y-value"),
        r: document.getElementById("r-value"),
		submit: document.getElementById("submit-button")
	}

    resultsTable = document.getElementById("results-content");
    localStorage = window.localStorage;

    animations = new AnimationProcessor();

    constructor() {
		this.initXValue();
        this.setRValue(null);
        this.initTableResults();

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
        const selectedRCheckboxes = document.querySelectorAll('input[type="checkbox"]:checked');
        
        this.components.r.value = "";
        selectedRCheckboxes.forEach(cbx =>
            this.components.r.value += cbx.value
        );
        console.log(this.components.r.value);
    }

	xButtonHandler(e) {
		const choosenButtons = document.getElementsByClassName("choosen");
		Array.from(choosenButtons).forEach(btn =>
			btn.classList.remove("choosen")
		);
		e.target.classList.add("choosen");
    
		this.components.x.value = e.target.textContent;
	}

	validateAndParse(x, y, r) {
		const xValues = [-4, -3, -2, -1, 0, 1, 2, 3, 4];
		const yMin = -5., yMax = 5.;
		const rValues = [1., 1.5, 2., 2.5, 3.];

		let parsedX, parsedY, parsedR;

        console.log(x, y, r);

        parsedX = parseInt(x);
		if (isNaN(x.trim()) || isNaN(parsedX) || !xValues.includes(parsedX)) {
			alert("Please choose correct button");
			return [null,null,null];
		}

		parsedY = parseFloat(y);
		if (isNaN(y.trim()) || isNaN(parsedY) || yMin >= parsedY || parsedY >= yMax) {
			alert("Please input correct Y value");
			return [null,null,null];
		}

		parsedR = parseFloat(r);
		if (isNaN(r.trim()) || isNaN(parsedR) || !rValues.includes(parsedR)) {
			alert("Choose only one checkbox");
			return [null,null,null];
		}

		return [parsedX, parsedY, parsedR];
	}

	async formSubmitHandler(e) {
		e.preventDefault();
		this.components.submit.textContent = "Checking...";
		this.components.submit.disabled = true
    
		const [x, y, r] = this.validateAndParse(this.components.x.value, this.components.y.value, this.components.r.value);
		if (x !== null && y !== null && r !== null) {
			try {
				const response = await fetch("src/php/process.php", {
					method: "POST",
					headers: {
						"Content-Type": "application/json"
					},
					body: JSON.stringify({x, y, r})
				});

				const json = await response.json();
                if (response.status === 200) {
                    if (!document.getElementById("disable-video").checked) {
                        await this.animations.shoot(x, y, r)
                    }

                    var data = [x, y, r, json.now, json.script_time, json.result];
					this.addTableResults(data);
				} else {
					alert("Server error: " + json.message);
				}
			} catch (error) {
                console.log(error);
				alert("Server unreachable :(\nTry again later");
			}
		}
		this.components.submit.disabled = false;
		this.components.submit.textContent = "Check";
	}

    initTableResults() {
        var data = this.localStorage.getItem(resultsDataKey);

        if (data === null) return;

        data.split(";").forEach(rowData => {
            var row = this.resultsTable.insertRow();
    
            rowData.split(",").forEach(cellData =>
                row.insertCell().innerHTML = cellData
            )
        })
    }

    addTableResults(rowData) {
        var row = this.resultsTable.insertRow(0);

        document.querySelectorAll('td[style="color: greenyellow;"]').forEach(cell => cell.removeAttribute("style"));
        document.querySelectorAll('td[style="color: red;"]').forEach(cell => cell.removeAttribute("style"));

        rowData.forEach(cellData => {
            var cell = row.insertCell();
            cell.innerHTML = cellData;
            
            if (cellData === "area") {
                cell.style = "color: greenyellow;";
            } else if (cellData === "miss") {
                cell.style = "color: red;";
            }
        });

        var lastData = this.localStorage.getItem(resultsDataKey);
        this.localStorage.setItem(resultsDataKey, rowData.toString() + (lastData ? ";" + lastData : ""));
    }
}

const app = new Application();