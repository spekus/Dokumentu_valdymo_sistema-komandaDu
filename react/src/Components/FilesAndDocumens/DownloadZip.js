import React, {Component} from 'react';
import FileSaver from "file-saver";
import '../../App.css'
import axios from 'axios';

class DownloadZip extends Component {
    state = {
    };


    componentWillMount() {

    }

    downloadZip = () => {
        console.log("zip runssss");
        fetch(`http://localhost:8181/api/files/zip`)
         .then((response) => {
            FileSaver.saveAs(response.url);
            // FileSaver.saveAs(response.url, "suggestedFileName");
            }).catch((response) => {
        console.error("Could not Download zip file from the server.", response);
    });
    }

    render() {
        return (
        <React.Fragment>
        < button className="btn button1"
        onClick={this.downloadZip}>Zippo
        </button>
        </React.Fragment>
        );
    }
}

export default DownloadZip;