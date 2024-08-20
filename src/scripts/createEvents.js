const submitButton = document.querySelector('#submitButton');
//
submitButton.addEventListener('click', e => {
    //
    e.preventDefault();
    const fs = require('fs');
    const path = require('path');
    const filePath = path.join(__dirname, 'src', 'eventos', 'EventsAdded.json');
    //
    if (fs.existsSync(filePath)) {
        console.log('O arquivo EventsAdded.json já existe.');
    } else {

        if (!fs.existsSync(path.dirname(filePath))) {
            fs.mkdirSync(path.dirname(filePath), { recursive: true });
        }


        const data = {
            events: []
        };


        fs.writeFile(filePath, JSON.stringify(data, null, 2), (err) => {
            if (err) throw err;
           addEventToJSON();


        });
    }
});

//
//
const addEventToJSON = ()=>{

}



