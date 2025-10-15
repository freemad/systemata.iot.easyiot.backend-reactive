<script>
    const ws = new WebSocket("ws://localhost:8091/ws/mqtt");

    ws.onopen = () => {
    console.log("ws open");

    // subscribe to wildcard
    const sub = {action: "subscribe", topic: "devices/+/telemetry"};
    ws.send(JSON.stringify(sub));
};

    ws.onmessage = (ev) => {
    try {
    const obj = JSON.parse(ev.data);
    console.log("incoming:", obj);
} catch (e) {
    console.log("raw:", ev.data);
}
};

    // publish example
    function publish(topic, payload) {
    const p = {action: "publish", topic, payload, qos: 1, retained: false};
    ws.send(JSON.stringify(p));
}

    // unsubscribe example
    function unsubscribe(topic) {
    ws.send(JSON.stringify({action: "unsubscribe", topic}));
}
</script>