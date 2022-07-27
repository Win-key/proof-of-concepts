(() => {
    let curUtility = "";

    chrome.runtime.onMessage.addListener((obj, sender, response) => {
        const { utility } = obj;
        console.log(obj)
        
        switch (utility) {
            case 'g-meet':
                meetingUtility();
                break;
            case 'todo-reminder':
                console.log("Yet to be developed")
                break;
            case 'screener':
                console.log("Yet to be developed")
                break;
        }
        response({
            msg : "Message received"
        })
        return true;
    });

    const meetingUtility =  () => {
        const ctrl = document.querySelectorAll('.U26fgb')
        document.onkeydown = (e) => {
            // code is empty, e.keycode is 179 but it's deprecated. so using e.key
            if (e.key == 'MediaPlayPause') {
                debugger
                let element = ctrl && ctrl.length > 0 ? ctrl[0] : document.querySelector('.Tmb7Fd button');
                element.click()
                e.preventDefault()
            }
        }
        if(ctrl && ctrl.length > 0){
            ctrl[0].click()
            ctrl[1].click()
        }
        console.log("meeing utility loaded in content");
    }

    meetingUtility();
})();
