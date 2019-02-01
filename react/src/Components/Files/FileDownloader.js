import {Component} from "react";
import FileSaver from "file-saver";
import React from "react";

export default class FileUploader extends Component {

    state = {
        file: '',
        error: '',
        msg: ''
    }

    downloadFile = () => {
        fetch("http://localhost:8181/api/files/download/" + "4-uzduoties-egzamine-pvz.pdf")
            .then(response => {
                console.log(response);
                console.log("downloadRandomStuff");
                // Log somewhat to show that the browser actually exposes the custom HTTP header
                const fileNameHeader = "x-suggested-filename";
                const suggestedFileName = response.headers[fileNameHeader];
                const effectiveFileName = (suggestedFileName === undefined
                    ? "document.txt"
                    : suggestedFileName);
                console.log("Received header [" + fileNameHeader + "]: " + suggestedFileName
                    + ", effective fileName: " + effectiveFileName);

                // Let the user save the file.
                FileSaver.saveAs(response.url, effectiveFileName);

            }).catch((response) => {
            console.error("Could not Download the Excel report from the backend.", response);
        });
    }

    render() {
        return (
            <React.Fragment>
                <h6>Download</h6>
                <button onClick={this.downloadFile}>Download a random file</button>

            </React.Fragment>
        );
    }
}