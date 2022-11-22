<%@ page contentType="text/html; charset=UTF-8" buffer="8kb" language="java" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
    <head>
    <meta charset="UTF-8">
	<title>Super Matthew's Lab</title>

	<link rel="stylesheet" type="text/css" href="styles/body.css" />
	<link rel="stylesheet" type="text/css" href="styles/header.css" />
	<link rel="stylesheet" type="text/css" href="styles/user_input.css" />
	<link rel="stylesheet" type="text/css" href="styles/table.css" />
	<link rel="stylesheet" type="text/css" href="styles/other.css" />
	<link rel="stylesheet" type="text/css" href="styles/jsx.css" />
	<link rel="icon" href="files/myshitlab_logo.png">

    </head>
    <body>
        <div id="error-message-block" style="display: none;">
            <span id="error-message"></span>
            <button class="action-button" id="close-error-button">Close</button>
        </div>
        <div>
            <video style="display: none;" id="bomb-video" src="files/bomb_speed.mp4" controls="true" crossorigin="anonymous"></video>
            <canvas id="tmp" style="display: none;" width="1280" height="720"></canvas>
		    <canvas id="bomb" class="overlay" width="1280" height="720"></canvas>
        </div>
        <div class="header">
            <h1>Matthew Ivanov</h1>
            <h3>P32111 / 11552</h3>
        </div>
        <div class="controller">
            <!--
                Buttons: X, Y, R
            -->
            <label id="controller-title">Control pane</label>
            <form id="user-request">
                <div class="x-buttons">
                    <input class="invisible" id="x-value" name="x" type="text" value="" required>
                    <button class="x" id="x5" type="button">5</button>
                    <button class="x" id="x-3" type="button">-3</button>
                    <button class="x" id="x-2" type="button">-2</button>
                    <button class="x" id="x-1" type="button">-1</button>
                    <button class="x" id="x0" type="button">0</button>
                    <button class="x" id="x1" type="button">1</button>
                    <button class="x" id="x2" type="button">2</button>
                    <button class="x" id="x3" type="button">3</button>
                    <button class="x" id="x4" type="button">4</button>
                </div>
                <br>
                <div class="y-input">
                    <label id="y-label">Y: </label>
                    <input id="y-value" name="y" type="text" pattern="([\-][0-4]([\.,][0-9]+)?|[+]?[0-2]([\.,][0-9]+)?)" required title="Y must be in range (-5, 3)">
                </div>
                <br>
                <div class="r-checkbox">
                    <label id="r-label">R: </label>
                    <input class="invisible" id="r-value" name="r" type="text" value=""
                        pattern="([1-3]|[1-2]\.5)" required title="Only one checkbox must be selected"
                    >
                    <div class="r-chexbox-column-edge">
                        <input class="r" name="r" id="r1" type="checkbox" value="1">
                        <label for="r1">1</label>
                        <input class="r" name="r" id="r2" type="checkbox" value="1.5">
                        <label for="r2">1.5</label>
                    </div>
                    <div class="r-chexbox-column-center">
                        <input class="r" name="r" id="r3" type="checkbox" value="2">
                        <label for="r3">2</label>
                    </div>
                    <div class="r-chexbox-column-edge">
                        <input class="r" name="r" id="r4" type="checkbox" value="2.5">
                        <label for="r4">2.5</label>
                        <input class="r" name="r" id="r5" type="checkbox" value="3">
                        <label for="r5">3</label>
                    </div>
                </div>
                <br>
                <div class="submit-button-block">
                    <button type="submit" class="action-button" id="submit-button">Check</button>
                </div>
            </form>
        </div>
        <div id="jxgbox" class="graph" style="height: 300px; width: 300px; float:left; margin: 0 5% 8% 5%"></div>
        <div class="results">
            <!--
                Table with previos shots
            -->
            <table id="results-table">
                <thead>
                    <tr>
                        <th width="10%">X</th> <!-- 7% -->
                        <th width="10%">Y</th> <!-- 7% -->
                        <th width="5%">R</th> <!-- 7% -->
                        <th width="35%">Current Time</th> <!-- 36% -->
                        <th width="30%">Script Time(ms)</th> <!-- 27% -->
                        <th width="10%">Result</th> <!-- 16% -->
                    </tr>
                </thead>
                <tbody id="results-content">
                </tbody>
            </table>
            <div id="clear-button-block">
                <%
                    Integer numRequests = (Integer) request.getSession().getAttribute("numRequests");
                    int numRequestsNotNull = numRequests != null ? numRequests : 0;
                %>
                <p>Servlet requests count:</p>
                <span id="requests-counter"><%= numRequestsNotNull %></span>
                <button class="action-button" id="clear-button" onclick="app.clearResults()">Clear</button>
            </div>
        </div>
        <input id="disable-video" type="checkbox" value="" checked>

        <script type="text/javascript" src="js/jquery-3.6.0.js"></script>
        <script type="text/javascript" src="js/onload_script.js"></script>

        <script type="text/javascript" src="js/animation.js"></script>
        <script type="text/javascript" src="js/main.js"></script>

        <script type="text/javascript" src="js/jsxgraphcore.js"></script>
        <script type="text/javascript" src="js/graph.js"></script>
    </body>
</html>
