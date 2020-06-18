import {gc, init_heap} from "./g1_svg.js";
import {run, action} from "./g1_controller.js";

let stompClient = null;

function connect() {
    let socket = new SockJS('/jvm');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/gc/new', function (data) {
            console.log('new: ' + data.body);
        });
        stompClient.subscribe('/topic/gc/mark', function (data) {
            console.log('mark: ' + data.body);
        });
        stompClient.subscribe('/topic/gc/copy', function (data) {
            console.log('copy: ' + data.body);
        });
        stompClient.subscribe('/topic/gc/sweep', function (data) {
            console.log('sweep: ' + data.body);
        });
        stompClient.subscribe('/topic/gc/promotion', function (data) {
            console.log('promotion: ' + data.body);
        });
        stompClient.subscribe('/topic/supervisor/pause', function (data) {
            console.log('pause: ' + data.body);
            $("#debug_alert").text(data.body);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

$(function () {
    connect();
    init_heap();
    $("#run").click(run.bind(null, false));
    $("#debug").click(run.bind(null, true));

    $("#go").click(action.bind(null, 'go'));
    $("#step").click(action.bind(null, 'step'));
    $("#pause").click(action.bind(null, 'pause'));
});