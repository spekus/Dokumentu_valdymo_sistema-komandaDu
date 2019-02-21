import React, {Component} from 'react';

import FileSaver from "file-saver";
import axios from 'axios';

class DownloadZip extends Component {
    state = {
    };


    componentWillMount() {

    }

    componentDidMount() {
    }


    downloadZip = () => {
        // neparasius pilno adreso su localhostu programa atsiuncia nesamone.
        //speju cia kazkas susije su security,
        fetch("http://localhost:8181/api/files/zip/")
            .then(response => {
                // const fileNameHeader = "x-suggested-filename";
                // const suggestedFileName = response.headers[fileNameHeader];
                // const effectiveFileName = (suggestedFileName === undefined
                //     ? "document.zip"
                //     : "duomenys.zip");
                // console.log("Received header [" + fileNameHeader + "]: " + suggestedFileName
                //     + ", effective fileName: " + effectiveFileName);
                // Let the user save the file.
                const suggestedFileName = "duomenys.zip";
                FileSaver.saveAs(response.url, suggestedFileName);


            }).catch((response) => {
            console.error("Could not Download the Excel report from the backend.", response);
        });
    }

    render() {
        return (
        <React.Fragment>
        < button className="btn btn-danger btn-sm"
        onClick={this.downloadZip}>Zippo
        </button>
        </React.Fragment>
        );
    }
}

export default DownloadZip;