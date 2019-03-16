import React, {Component} from 'react';
import FileSaver from "file-saver";
import '../../App.css'
import axios from 'axios';

class DownloadZip extends Component {
    state = {
    };


    componentWillMount() {

    }

    componentDidMount() {
    }


    downloadZip = () => {
        const FileDownload = require('js-file-download');
        console.log("zip run");
        axios.get(`http://localhost:8181/api/files/zip`)
         .then((response) => {
            FileDownload(response.data, 'report.zip');
            // FileSaver.saveAs(response.url, "suggestedFileName");
            }).catch((response) => {
        console.error("Could not Download zip file from the server.", response);
    });
    }
//     downloadZip = () => {
//         // neparasius pilno adreso su localhostu programa atsiuncia nesamone.
//         //speju cia kazkas susije su security,
//         const FileDownload = require('js-file-download');
//         fetch('http://localhost:8181/api/files/zip')
//             .then(response => {
//                 // console.log(response)
//                 // console.log(response.url)
//                 console.log(response)
//                 FileDownload(response.data);

//             }).catch((response) => {
//             console.error("Could not Download zip file from the server.", response);
// });
//     }

    render() {
        return (
        <React.Fragment>
        < button className="btn button1"
        onClick={this.downloadZip}>ZippoSS
        </button>
        </React.Fragment>
        );
    }
}

export default DownloadZip;