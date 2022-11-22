const figuresProperties = {
    strokeColor: '#4b61cf',
    highlightStrokeColor: '#5a73ed',
    fillColor: '#4b61cf',
    highlightFillColor: '#5a73ed',
    fixed: true
};
const pointProperties = function (visible) {
    return {
        strokeColor: 'green',
        highlightStrokeColor: '#5ee667',
        fillColor: 'green',
        highlightFillColor: '#5ee667',
        name: '',
        size: 2,
        fixed: true,
        visible: visible
    }
}
const oldDotsProperties = {
    strokeColor: '#506e3b',
    highlightStrokeColor: '#7aa15f',
    fillColor: '#506e3b',
    highlightFillColor: '#7aa15f',
    name: '',
    size: 2,
    fixed: true
}

function initBoard() {
    return JXG.JSXGraph.initBoard('jxgbox', {boundingbox: [-4, 4, 4, -4], axis:true});
}

let board = initBoard();


function setVerticesInvisible(figure) {
    for (let i = 0; i < figure.vertices.length - 1; i++) {
        figure.vertices[i].setAttribute({visible: false});
    }
}

function wrapCoordinate(coordinate) {
    return Math.round(coordinate*100)/100
}

function create_point(x, y, visible = true) {
    return board.create('point', [x, y], pointProperties(visible));
}

function createTriangle(R) {
    let p1 = create_point(0, 0);
    let p2 = create_point(0, R/2);
    let p3 = create_point(-R, 0);
    let triangle = board.create('polygon', [p1, p2, p3], figuresProperties);
    setVerticesInvisible(triangle);
    return triangle;
}

function createRectangle(R) {
    let p1 = create_point(0, 0);
    let p2 = create_point(R, 0);
    let p3 = create_point(R, R);
    let p4 = create_point(0, R);
    let rectangle = board.create('polygon', [p1, p2, p3, p4], figuresProperties);
    setVerticesInvisible(rectangle);
    return rectangle;
}

function createCircle(R) {
    let p1 = create_point(0, 0, false);
    let p2 = create_point(0, -R, false);
    let p3 = create_point(R, 0, false);
    return board.create('sector', [p1, p2, p3], figuresProperties);
}

let getMouseCoords = function(e, i) {
    let cPos = board.getCoordsTopLeftCorner(e, i),
        absPos = JXG.getPosition(e, i),
        dx = absPos[0]-cPos[0],
        dy = absPos[1]-cPos[1];
    return new JXG.Coords(JXG.COORDS_BY_SCREEN, [dx, dy], board);
}

let down = function(e) {
    if (e.button === 2 || e.target.className === 'JXG_navigation_button') {
        return;
    }
    let canCreate = true, i, coords, el;

    if (e[JXG.touchProperty]) {
        // index of the finger that is used to extract the coordinates
        i = 0;
    }
    coords = getMouseCoords(e, i);
    let R = app.validateR($('#r-value').val().trim())
    if (canCreate && R != null) {
        app.makeRequest(wrapCoordinate(coords.usrCoords[1]), wrapCoordinate(coords.usrCoords[2]), R);
    }
}

board.on("down", down);

let previousObjects = null;

$('#r-value').change(function () {
    $("#jxgbox_licenseText").remove();

    let R = app.validateR($('#r-value').val().trim());
    console.log("Change R: " + R)

    if (R) {
        if (previousObjects) {
            board.removeObject(previousObjects);
        }

        let triangle = createTriangle(R),
            rect = createRectangle(R),
            circle = createCircle(R);

        previousObjects = [triangle, rect, circle];
    }
})