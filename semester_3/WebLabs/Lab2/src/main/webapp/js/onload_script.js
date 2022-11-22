function fillTable(arrayOfDots) {
    arrayOfDots.forEach(dotInfo => {
        app.addTableResults(
            [
                dotInfo.x, dotInfo.y, dotInfo.r,
                dotInfo.currentTime, dotInfo.executionTime,
                dotInfo.result === "true" ? "area" : "miss"
            ]
        )
    });
}

function fillGraph(arrayOfDots) {
    arrayOfDots.forEach(dotInfo => {
        board.create('point', [dotInfo.x, dotInfo.y], oldDotsProperties);
    });
}

async function fillDotsOnLoad(e) {
    try {
        const response = await fetch("./controller?" + new URLSearchParams({session: "get-hits"}));
        const responseData = await response.json();

        if (response.status == 200) {
            console.log(responseData);

            fillGraph(responseData['result']);
            fillTable(responseData['result']);
        }

        app.setRequestsCounter(responseData['requestsCount']);
    } catch (error) {
        console.log(error);
        app.showErrorMessage("Server unreachable :(\nTry again later");
    }
}

addEventListener("load", fillDotsOnLoad);