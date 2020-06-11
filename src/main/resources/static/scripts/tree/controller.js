const NS_API = "/tree/rb"

export function run(debug = false) {
    let request = {
        "nodes": [
            1, 3, 2
        ],
        "delay": 100,
        "reset": true,
        "debug": debug
    }
    let url = `${NS_API}/insert`;
    $.postJSON(url, request, (data) => console.log(`${url}: ${data}`));
}

export function action(action = 'go') {
    let url = `${NS_API}/debug/${action}`;
    $.postJSON(url, {}, (data) => console.log(`${url}: ${data}`));
}